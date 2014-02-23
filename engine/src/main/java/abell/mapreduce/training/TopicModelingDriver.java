package abell.mapreduce.training;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileAsTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import abell.conf.Paths;
import abell.mapreduce.common.ScoredWordWritable;

public class TopicModelingDriver extends Configured implements Tool {

	@Override
	public int run(String[] arg0) throws Exception {
		Job job = new Job(getConf(), "abell.training.topic");
		FileSystem.get(getConf()).delete(Paths.MODEL_TOPIC, true);

		job.setMapperClass(TopicModelingMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(ScoredWordWritable.class);
		job.setReducerClass(TopicModelingReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(MapWritable.class);
		
		job.setJarByClass(TopicModelingDriver.class);
		job.setInputFormatClass(SequenceFileAsTextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.setInputPaths(job, new Path(Paths.MODEL_LDA, "state-" + LdaModelingDriver.MAX_ITER));
        FileOutputFormat.setOutputPath(job, Paths.MODEL_TOPIC);
        job.waitForCompletion(true);
		return 0;
	}

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new TopicModelingDriver(), args);
    }

}
