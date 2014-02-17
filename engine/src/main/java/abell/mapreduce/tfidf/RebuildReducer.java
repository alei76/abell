package abell.mapreduce.tfidf;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class RebuildReducer extends Reducer<Text, Text, Text, Text> {
	
	@SuppressWarnings("unused")
	@Override
    protected void reduce(Text itemId, Iterable<Text> termFequencies, Context context) throws IOException, InterruptedException {
		StringBuffer buff = new StringBuffer();
        boolean first = true;
		for(Text termFequency : termFequencies) {
            if (!first) {
                buff.append("&");
            } else {
                first = true;
            }
            buff.append(termFequency.toString());
        }
		context.write(itemId, new Text(buff.toString()));
    }
}
