package abell.mapreduce.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ScoredWordWritable implements Writable {

	private int wordIndex;
	private double score;
	
	public ScoredWordWritable() {}
	
	public ScoredWordWritable(int wordIndex, double score) {
		this.wordIndex = wordIndex;
		this.score = score;
	} 
	
	@Override
	public void readFields(DataInput input) throws IOException {
		wordIndex = input.readInt();
		score = input.readDouble();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(wordIndex);
		output.writeDouble(score);
	}
	
	public int wordIndex() {
		return wordIndex;
	}
	
	public double score() {
		return score;
	}

}
