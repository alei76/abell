package abell.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Author: GuoYu
 * Date: 14-2-13
 */
public class TermFequenceTask implements Runnable {

    public static String PREFIX_DOC = "doc";
    public static String PREFIX_DW = "dw:";
    public static String PREFIX_WD = "wd:";
    public static String PREFIX_WC = "wc:";

    private Configuration conf;

    public TermFequenceTask(Configuration conf) {
        this.conf = conf;
    }

    public void run() {
        Job step1, step2;
        try {
            step1 = new Job(conf, "abell.tf.step2");
            step1.setMapperClass(Step1Mapper.class);
            step1.setReducerClass(Step1Reducer.class);
            step1.setJarByClass(TermFequenceTask.class);
            step2 = new Job(conf, "abell.tf.step2");
            step1.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Step1Mapper extends Mapper<Text, Text, Text, Text> {

        @Override
        protected void map(Text itemId, Text content, Context context) throws IOException, InterruptedException {
            String[] terms;
            terms = content.toString().split("\\s");
            for(String term : terms) {
                context.write(new Text(PREFIX_WD + term), itemId);
            }
            context.write(new Text(PREFIX_DOC), itemId);
        }

    }

    static class Step1Reducer extends Reducer<Text, Text, Text, IntWritable> {

        private int documentCount = 0;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String keyStr = key.toString();
            if (PREFIX_DOC.equals(keyStr)) {
                for(Iterator<Text> i = values.iterator();i.hasNext();) {
                    documentCount ++;
                }
            } else if (keyStr.startsWith(PREFIX_WD)) {
                int termCount = 0;
                for(Iterator<Text> i = values.iterator();i.hasNext();) {
                    termCount ++;
                }
                context.write(new Text(keyStr.replace(PREFIX_WD, PREFIX_WC)),
                        new IntWritable(termCount));
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text(PREFIX_DOC), new IntWritable(documentCount));
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        TermFequenceTask task = new TermFequenceTask(conf);
        conf.set("mapred.job.tracker", "aliyun-s1:9001");
        conf.set("hadoop.job.ugi", "hadoop,superuser");
        task.run();
    }

}
