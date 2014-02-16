package abell.mapreduce.tfidf;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InverseMapper extends Mapper<Text, Text, Text, Text> {

	@Override
	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {
		if (key.equals(TermFequenceReducer.KEY_ALL)) {
			context.write(key, value);
			return;
		}
		String[] split = key.toString().split("@");
		String word = split[0];
		String itemId = split[1];
		float feq = Float.parseFloat(value.toString());
		context.write(new Text(word),
			new Text(itemId + "=" + feq));
	}

}
