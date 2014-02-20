package abell.mapreduce.normalize;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class DictionaryReducer extends Reducer<Text, Text, IntWritable, Text> {

    private int index = 0;

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        context.write(new IntWritable(index++), key);
    }
}
