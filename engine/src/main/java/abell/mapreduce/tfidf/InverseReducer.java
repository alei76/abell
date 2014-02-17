package abell.mapreduce.tfidf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InverseReducer extends Reducer<Text, Text, Text, Text> {

	private float countAll;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        String jobName = context.getJobName();
        String countStr = jobName.substring(jobName.lastIndexOf('|') + 1);
        countAll = Float.parseFloat(countStr);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> frequencies, Context context) throws IOException, InterruptedException {
		String word = key.toString();
        HashMap<String, Float> feqMap = new HashMap<String, Float>();
		for(Text frequency : frequencies) {
            String[] split = frequency.toString().split("=");
            String itemId = split[0];
            Float tf = Float.valueOf(split[1]);
            feqMap.put(itemId, tf);
		}
        int countOfTerm = feqMap.size();
        for(Map.Entry<String, Float> e : feqMap.entrySet()) {
            String itemId = e.getKey();
            float tf = e.getValue();
            tf = tf * (float)Math.log(countAll / countOfTerm);
            context.write(new Text(itemId),
                    new Text(word + "=" + tf));
        }
	}
	
}
