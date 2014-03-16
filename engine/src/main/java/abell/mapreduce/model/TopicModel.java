package abell.mapreduce.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;

import abell.conf.Paths;

public class TopicModel implements Iterable<Topic>{

	private Dictionary dict;
	
	private List<Topic> topics = new LinkedList<Topic>();
	
	private TopicModel(Dictionary dict) {
		this.dict = dict;
	}
	
	public Dictionary getDictionary() {
		return dict;
	}
	
	public Topic getTopic(int index) {
		return topics.get(index);
	}

	@Override
	public Iterator<Topic> iterator() {
		return topics.iterator();
	}
	
	public static void main(String[] args) throws IOException {
		TopicModel model = read(new Configuration());
		for(Topic topic : model) {
			System.out.println(topic);
		}
	}
	
	public static TopicModel read(Configuration conf) throws IOException {
		Dictionary dict = Dictionary.read(conf);
		FileSystem fs = FileSystem.get(conf);
		TopicModel model = new TopicModel(dict);
		for(FileStatus file : fs.listStatus(Paths.MODEL_TOPIC, filter)) {
			readFile(file, model, dict, fs, conf);
		}
		return model;
	}
	
	private static final void readFile(FileStatus file, TopicModel model,
			Dictionary dict, FileSystem fs, Configuration conf) throws IOException {
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, file.getPath(), conf);
		IntWritable key = new IntWritable();
		MapWritable value = new MapWritable();
		while(reader.next(key, value)) {
			int id = key.get();
			Topic topic = new Topic(id, dict);
			for(Map.Entry<Writable, Writable> entry : value.entrySet() ) {
				IntWritable entryKey = (IntWritable)entry.getKey();
				DoubleWritable entryValue = (DoubleWritable)entry.getValue();
				topic.score(entryKey.get(), entryValue.get());
			}
			model.topics.add(id, topic);
		}
		reader.close();
	}
	
	private static final PathFilter filter = new PathFilter() {

		@Override
		public boolean accept(Path path) {
			return path.getName().startsWith("part-");
		}
		
	};
	
}
