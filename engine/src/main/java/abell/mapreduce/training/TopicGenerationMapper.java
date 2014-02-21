package abell.mapreduce.training;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.common.IntPairWritable;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Author: GuoYu
 * Date: 14-2-21
 */
public class TopicGenerationMapper extends Mapper<IntPairWritable, DoubleWritable, IntWritable, VectorWritable> {

    @Override
    protected void map(IntPairWritable key, DoubleWritable value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
