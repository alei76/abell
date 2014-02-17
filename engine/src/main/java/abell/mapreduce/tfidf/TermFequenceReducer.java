package abell.mapreduce.tfidf;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TermFequenceReducer extends Reducer<Text, Text, Text, FloatWritable> {
	
	@SuppressWarnings("unused")
	@Override
    protected void reduce(Text key, Iterable<Text> counts, Context context) throws IOException, InterruptedException {
		float count = 0;
		float all = 1;
		for(Text value : counts) {
			if (value.equals(TermFequenceMapper.ONE_WORD)){
				count ++ ;
			} else {
				all = Float.parseFloat(value.toString());
			}
        }
		context.write(key, new FloatWritable(count / all));
    }
}
