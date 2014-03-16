package abell.mapreduce.model;

import java.util.HashMap;
import java.util.Map;


public class Topic {

	private Dictionary dict;
	
	private int id;
	
	private Map<Integer, Double> scores;
	
	Topic(int id, Dictionary dict) {
		this.id = id;
		this.dict = dict;
		scores = new HashMap<Integer, Double>();
	}
	
	void score(int wordIndex, double score) {
		scores.put(wordIndex, score);
	}
	
	public int id(){
		return id;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("[Topic=" + id + "]");
		buff.append("[scores={");
		int i = 0;
		for(Map.Entry<Integer, Double> entry : scores.entrySet()) {
			String word = dict.get(entry.getKey());
			buff.append((i ++ == 0 ? "" : ",") + word + "=" + entry.getValue());
		}
		buff.append("}]");
		return buff.toString();
	}

	public int size() {
		return scores.size();
	}
	
	public double score(int wordIndex) {
		Double value = scores.get(wordIndex);
		return value == null ? 0 : value.doubleValue();
	}
	
	public double score(String word) {
		int wordIndex = dict.indexOf(word);
		Double value = scores.get(wordIndex);
		return value == null ? 0 : value.doubleValue();
	}
	
}