package abell.mapreduce.training;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class DictionaryMapper extends Mapper<Text, Text, Text, Text> {

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(value.toString());
        while (st.hasMoreTokens()) {
            String wordStr = st.nextToken();
            if (wordStr.trim().length() == 0) {
                continue;
            }
            Text word = new Text(wordStr);
            context.write(word, word);
        }
    }

}
