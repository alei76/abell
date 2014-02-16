package abell.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import abell.conf.Paths;
import abell.mapreduce.tfidf.InverseMapper;
import abell.mapreduce.tfidf.InverseReducer;
import abell.mapreduce.tfidf.TermFequenceMapper;
import abell.mapreduce.tfidf.TermFequenceReducer;

public class TermFequence {

	public static void main(String[] args) {
		Job step1, step2;
		Configuration conf = new Configuration();
        try {
        	clean(conf);
            step1 = prepareTermFequence(conf);
            step2 = prepareInverse(conf);
            step1.waitForCompletion(true);
            step2.waitForCompletion(true);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	private static Job prepareTermFequence(Configuration conf) throws IOException {
		Job job = new Job(conf, "abell.tfidf.step1");
		job.setJarByClass(TermFequence.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        
		job.setMapperClass(TermFequenceMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setReducerClass(TermFequenceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        
        FileInputFormat.setInputPaths(job, Paths.ITEMS);
        FileOutputFormat.setOutputPath(job, Paths.TF_INF_STEP1);
        return job;
	}
	
	private static Job prepareInverse(Configuration conf) throws IOException {
		Job job = new Job(conf, "abell.tfidf.step2");
		job.setJarByClass(TermFequence.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        
		job.setMapperClass(InverseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setReducerClass(InverseReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.setInputPaths(job, Paths.TF_INF_STEP1);
        FileOutputFormat.setOutputPath(job, Paths.TF_INF_STEP2);
        return job;
	}
	
	private static void clean(Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		fs.delete(Paths.TF_IDF, true);
	}
	
}
