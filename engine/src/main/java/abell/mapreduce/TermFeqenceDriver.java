package abell.mapreduce;

import java.io.IOException;

import abell.conf.CounterEnum;
import abell.mapreduce.tfidf.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import abell.conf.Paths;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TermFeqenceDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        Job step1, step2, step3;
        Configuration conf = new Configuration();
        try {
            clean(conf);
            step1 = prepareTermFequence(conf);
            step1.waitForCompletion(true);
            long itemCount = step1.getCounters().findCounter(CounterEnum.Items).getValue();
            step2 = prepareInverse(conf, itemCount);
            step2.waitForCompletion(true);
            step3 = prepareRebuild(conf);
            step3.waitForCompletion(true);
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
	
	private Job prepareTermFequence(Configuration conf) throws IOException {
		Job job = new Job(conf, "abell.tfidf.step1");
		job.setJarByClass(TermFeqenceDriver.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        
		job.setMapperClass(TermFequenceMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setReducerClass(TermFequenceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        
        FileInputFormat.setInputPaths(job, Paths.ITEMS);
        FileOutputFormat.setOutputPath(job, Paths.TF_IDF_STEP1);
        return job;
	}
	
	private Job prepareInverse(Configuration conf, long itemCount) throws IOException {
		Job job = new Job(conf, "abell.tfidf.step2|" + itemCount);
		job.setJarByClass(TermFeqenceDriver.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        
		job.setMapperClass(InverseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setReducerClass(InverseReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.setInputPaths(job, Paths.TF_IDF_STEP1);
        FileOutputFormat.setOutputPath(job, Paths.TF_IDF_STEP2);
        return job;
	}

    private Job prepareRebuild(Configuration conf) throws IOException {
        Job job = new Job(conf, "abell.tfidf.step3");
        job.setJarByClass(TermFeqenceDriver.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(RebuildMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(RebuildReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, Paths.TF_IDF_STEP2);
        FileOutputFormat.setOutputPath(job, Paths.TF_IDF_RESULT);
        return job;
    }
	
	private void clean(Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		fs.delete(Paths.TF_IDF, true);
	}

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new TermFeqenceDriver(), args);
    }
}
