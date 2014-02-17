package abell.mapreduce.tfidf;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RebuildMapper extends Mapper<Text, Text, Text, Text> {

	@Override
    protected void map(Text itemId, Text termFequency, Context context) throws IOException, InterruptedException {
        context.write(itemId, termFequency);
    }

}
