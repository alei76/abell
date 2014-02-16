package abell.mapreduce.tfidf;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TermFequenceReducer extends Reducer<Text, Text, Text, FloatWritable> {
	
	static final Text KEY_ALL = new Text("!all");
	
	@SuppressWarnings("unused")
	@Override
    protected void reduce(Text key, Iterable<Text> counts, Context context) throws IOException, InterruptedException {
		float count = 0;
		float all = 1;
		if (key.equals(TermFequenceMapper.KEY_ITEM)) {
			//handle as item ["i", "i"]
			int itemCount = 0;
			for(Text value : counts) {
				itemCount ++;
			}
			context.write(KEY_ALL, new FloatWritable(itemCount));
			return;
		}
		// handle as word@item, ["w", "w"]
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
