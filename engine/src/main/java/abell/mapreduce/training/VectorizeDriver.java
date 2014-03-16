package abell.mapreduce.training;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.math.VectorWritable;

import abell.conf.Paths;
import abell.driver.TopicTrainingDriver;

public class VectorizeDriver extends Configured implements Tool {
	
	@Override
	public int run(String[] arg0) throws Exception {
		Job vectoringJob = new Job(getConf(), "abell.training.vectorize");
        FileSystem.get(getConf()).delete(Paths.ITEMS_VECTOR, true);

        vectoringJob.setMapperClass(VectoringMapper.class);
        vectoringJob.setMapOutputKeyClass(Text.class);
        vectoringJob.setMapOutputValueClass(VectorWritable.class);
        vectoringJob.setOutputKeyClass(Text.class);
        vectoringJob.setOutputValueClass(VectorWritable.class);

        vectoringJob.setJarByClass(TopicTrainingDriver.class);
        vectoringJob.setInputFormatClass(KeyValueTextInputFormat.class);
        vectoringJob.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.setInputPaths(vectoringJob, Paths.ITEMS_ORIGIN);
        FileOutputFormat.setOutputPath(vectoringJob, Paths.ITEMS_VECTOR);
        vectoringJob.waitForCompletion(true);
        return 0;
	}

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new VectorizeDriver(), args);
    }

}
