package abell.mapreduce.tfidf;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InverseReducer extends Reducer<Text, Text, Text, Text> {

	private float allCount;

	@SuppressWarnings("unused")
	@Override
    protected void reduce(Text key, Iterable<Text> counts, Context context) throws IOException, InterruptedException {
		if (TermFequenceReducer.KEY_ALL.equals(key)) {
			String countStr = counts.iterator().next().toString();
			allCount = Float.parseFloat(countStr);
			return;
		}
		int countInItem = 0;
		String word = key.toString();
		for(Text value : counts) {
			countInItem ++ ;
		}
		for(Text value : counts) {
			String[] split = value.toString().split("=");
			String itemId = split[0];
			float feq = Float.parseFloat(split[1]);
			feq = feq * (float)Math.log(allCount / countInItem);
			context.write(new Text(itemId),
				new Text(word + "=" + feq));
		}
	}
	
}
