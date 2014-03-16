package abell.mapreduce.training;

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

import abell.conf.Paths;
import abell.driver.TopicTrainingDriver;

public class DictionaryDriver extends Configured implements Tool {
	
	@Override
	public int run(String[] arg0) throws Exception {
        Job dictJob = new Job(getConf(), "abell.training.dictionary");
        FileSystem.get(getConf()).delete(Paths.MODEL_DICT, true);

        dictJob.setMapperClass(DictionaryMapper.class);
        dictJob.setMapOutputKeyClass(Text.class);
        dictJob.setMapOutputValueClass(Text.class);
        dictJob.setReducerClass(DictionaryReducer.class);
        dictJob.setOutputKeyClass(IntWritable.class);
        dictJob.setOutputValueClass(Text.class);

        dictJob.setJarByClass(TopicTrainingDriver.class);
        dictJob.setInputFormatClass(KeyValueTextInputFormat.class);
        FileInputFormat.setInputPaths(dictJob, Paths.ITEMS_ORIGIN);
        FileOutputFormat.setOutputPath(dictJob, Paths.MODEL_DICT);
        dictJob.waitForCompletion(true);
		return 0;
	}

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new DictionaryDriver(), args);
    }

}
