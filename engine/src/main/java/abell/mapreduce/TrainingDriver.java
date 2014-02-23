package abell.mapreduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import abell.mapreduce.training.DictionaryDriver;
import abell.mapreduce.training.LdaModelingDriver;
import abell.mapreduce.training.TopicModelingDriver;
import abell.mapreduce.training.VectorizeDriver;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class TrainingDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        ToolRunner.run(new DictionaryDriver(), args);
        ToolRunner.run(new VectorizeDriver(), args);
        ToolRunner.run(new LdaModelingDriver(), args);
        ToolRunner.run(new TopicModelingDriver(), args);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new TrainingDriver(), args);
    }
}