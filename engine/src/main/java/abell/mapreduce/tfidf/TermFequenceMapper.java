package abell.mapreduce.tfidf;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TermFequenceMapper extends Mapper<Text, Text, Text, Text> {

	static final Text KEY_ITEM = new Text("item");
	
	static final Text ONE_WORD = new Text("w");

	static final Text ONE_ITEM = new Text("d");

	@Override
    protected void map(Text itemId, Text content, Context context) throws IOException, InterruptedException {
        String[] words;
        int count = 0;
        words = content.toString().split("\\s");
        for(String word : words) {
        	count ++ ;
        	context.write(new Text(word + "@" + itemId),
    			ONE_WORD);
        }
        for(String word : words) {
        	context.write(new Text(word + "@" + itemId),
    			new Text(String.valueOf(count)));
        }
    	context.write(KEY_ITEM, ONE_ITEM);
    }

}
