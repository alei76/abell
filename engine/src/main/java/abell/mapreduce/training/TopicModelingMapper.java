package abell.mapreduce.training;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import abell.mapreduce.common.ScoredWordWritable;

/**
 * Author: GuoYu
 * Date: 14-2-21
 */
public class TopicModelingMapper extends Mapper<Text, Text, IntWritable, ScoredWordWritable> {
	
	private static final Pattern pattern = Pattern.compile("\\((\\d+), (\\d+)\\)");
	
    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
    	int topic, wordIndex;
    	double score;
    	Matcher matcher = pattern.matcher(key.toString());
    	if(!matcher.find()) {
    		return;
    	}
    	topic = Integer.parseInt(matcher.group(1));
    	wordIndex = Integer.parseInt(matcher.group(2));
    	score = Double.parseDouble(value.toString());
        context.write(new IntWritable(topic),
    		new ScoredWordWritable(wordIndex, score));
    }
    
    public static void main(String[] args) {
    	Matcher matcher = pattern.matcher("(19, 4432)");
    	matcher.find();
    	System.out.println(matcher.group(0));
    	System.out.println(matcher.group(1));
    	System.out.println(matcher.group(2));
    }

}
