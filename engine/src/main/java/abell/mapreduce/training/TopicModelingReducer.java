package abell.mapreduce.training;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;

import abell.mapreduce.common.ScoredWordWritable;

/**
 * Author: GuoYu
 * Date: 14-2-21
 */
public class TopicModelingReducer extends Reducer<IntWritable, ScoredWordWritable, IntWritable, MapWritable> {

	private static final int TOP_WORDS = 10;

	@Override
	protected void reduce(IntWritable key, Iterable<ScoredWordWritable> scores, Context context)
			throws IOException, InterruptedException {
		int counter = 0;
		double sum = 0;
		Iterator<ScoredWordWritable> iter = scores.iterator();
		List<Holder> tmp = new LinkedList<Holder>();
		while(iter.hasNext()) {
			ScoredWordWritable wordScore = iter.next();
			int wordIndex = wordScore.wordIndex();
			double score = wordScore.score();
			sum += Math.exp(score);
			tmp.add(new Holder(wordIndex, score));
		}
		Collections.sort(tmp);
		if (tmp.size() >  TOP_WORDS) {
			tmp = tmp.subList(0, TOP_WORDS);
		}
		MapWritable output = new MapWritable();
		for(Holder holder : tmp) {
			int wordIndex = holder.wordIndex;
			double score = holder.score;
			score = Math.exp(score) / sum;
			output.put(new IntWritable(wordIndex), new DoubleWritable(score));
		}
		context.write(key, output);
	}
	
	private static final class Holder implements Comparable<Holder> {

		private int wordIndex;
		private double score;
		
		private Holder(int wordIndex, double score) {
			this.wordIndex = wordIndex;
			this.score = score;
		}
		
		@Override
		public int compareTo(Holder o) {
			if(score > o.score) {
				return -1;
			} else if (score < o.score) {
				return 1;
			} else {
				return 0;
			}
		}
		
	}
	
}
