package abell.mapreduce;

import abell.conf.Paths;
import abell.mapreduce.normalize.DictionaryMapper;
import abell.mapreduce.normalize.DictionaryReducer;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class DictionaryDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        Job job = new Job(getConf());
        job.setInputFormatClass(KeyValueTextInputFormat.class);;

        job.setMapperClass(DictionaryMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(DictionaryReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, Paths.ITEMS);
        FileOutputFormat.setOutputPath(job, Paths.DICTIONARY);

        job.setJarByClass(DictionaryDriver.class);
        FileSystem.get(getConf()).delete(Paths.DICTIONARY, true);
        job.waitForCompletion(true);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new DictionaryDriver(), args);
    }

}
