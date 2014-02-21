package abell.mapreduce;

import abell.conf.Paths;
import abell.mapreduce.training.DictionaryMapper;
import abell.mapreduce.training.DictionaryReducer;
import abell.mapreduce.training.VectoringMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.lda.LDADriver;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class TrainingDriver extends Configured implements Tool {

    private static final int MAX_ITER = 5;

    private static final int TOPICS = 20;

    public static final String CONF_LDA_STATE = "abell.lda.state";

    @Override
    public int run(String[] args) throws Exception {
        FileSystem fs = FileSystem.get(getConf());
        buildDict(fs);
        buildVector(fs);
        runLda(TOPICS);
        buildTopic(fs, TOPICS);
        return 0;
    }

    private void buildDict(FileSystem fs) throws Exception {
        Job dictJob = new Job(getConf());
        fs.delete(Paths.MODEL_DICT, true);

        dictJob.setMapperClass(DictionaryMapper.class);
        dictJob.setMapOutputKeyClass(Text.class);
        dictJob.setMapOutputValueClass(Text.class);
        dictJob.setReducerClass(DictionaryReducer.class);
        dictJob.setOutputKeyClass(IntWritable.class);
        dictJob.setOutputValueClass(Text.class);

        dictJob.setJarByClass(TrainingDriver.class);
        dictJob.setInputFormatClass(KeyValueTextInputFormat.class);
        FileInputFormat.setInputPaths(dictJob, Paths.ITEMS_ORIGIN);
        FileOutputFormat.setOutputPath(dictJob, Paths.MODEL_DICT);
        dictJob.waitForCompletion(true);
    }

    private void buildVector(FileSystem fs) throws Exception {

        Job vectoringJob = new Job(getConf());
        fs.delete(Paths.ITEMS_VECTOR, true);

        vectoringJob.setMapperClass(VectoringMapper.class);
        vectoringJob.setMapOutputKeyClass(Text.class);
        vectoringJob.setMapOutputValueClass(VectorWritable.class);
        vectoringJob.setOutputKeyClass(Text.class);
        vectoringJob.setOutputValueClass(VectorWritable.class);

        vectoringJob.setJarByClass(TrainingDriver.class);
        vectoringJob.setInputFormatClass(KeyValueTextInputFormat.class);
        vectoringJob.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.setInputPaths(vectoringJob, Paths.ITEMS_ORIGIN);
        FileOutputFormat.setOutputPath(vectoringJob, Paths.ITEMS_VECTOR);
        vectoringJob.waitForCompletion(true);
    }

    private void runLda(int iter) throws Exception {
        Configuration ldaConf = new Configuration(getConf());
        String[] ldaArgs = new String[]{
            "--input", Paths.ITEMS_VECTOR.toString(),
            "--output", Paths.MODEL_LDA.toString(),
            "--overwrite",
            "--numTopics", String.valueOf(TOPICS),
            "--maxIter", String.valueOf(iter)
        };
        ToolRunner.run(ldaConf, new LDADriver(), ldaArgs);
    }

    private void buildTopic(FileSystem fs, int iter) throws IOException {
        Configuration conf = new Configuration(getConf());
        conf.setInt(CONF_LDA_STATE, iter);
        Job topicJob = new Job(conf);
        fs.delete(Paths.MODEL_TOPIC, true);

    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new TrainingDriver(), args);
    }
}