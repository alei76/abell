package abell.mapreduce;

import java.io.IOException;

import abell.conf.Paths;
import abell.mapreduce.normalize.DictionaryMapper;
import abell.mapreduce.normalize.DictionaryReducer;
import abell.mapreduce.normalize.VectoringMapper;

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
import org.apache.mahout.math.VectorWritable;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class NormalizeDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
    	FileSystem fs = FileSystem.get(getConf());
        Job dictJob = prepareDictJob(fs);
        Job vectoringJob = prepareVectoringJob(fs);
        dictJob.waitForCompletion(true);
        vectoringJob.waitForCompletion(true);
        return 0;
    }
    
    private Job prepareDictJob(FileSystem fs) throws IOException {
        Job job = new Job(getConf());
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(DictionaryMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(DictionaryReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, Paths.ITEMS);
        FileOutputFormat.setOutputPath(job, Paths.DICTIONARY);
        fs.delete(Paths.DICTIONARY, true);

        job.setJarByClass(NormalizeDriver.class);
        return job;
    }
    
    private Job prepareVectoringJob(FileSystem fs) throws IOException {
        Job job = new Job(getConf());
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(VectoringMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(VectorWritable.class);

        FileInputFormat.setInputPaths(job, Paths.ITEMS);
        FileOutputFormat.setOutputPath(job, Paths.MATRIX_ITEMS);
        fs.delete(Paths.MATRIX_ITEMS, true);

        job.setJarByClass(NormalizeDriver.class);
        return job;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new NormalizeDriver(), args);
    }

}
