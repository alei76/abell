package abell.mapreduce.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import abell.conf.Paths;

public class Dictionary extends java.util.Dictionary<Integer, String>{
	
	private HashMap<Integer, String> innerMap = new HashMap<Integer, String>();
	
	private HashMap<String, Integer> reverseMap = new HashMap<String, Integer>();

	@Override
	public int size() {
		return innerMap.size();
	}

	@Override
	public boolean isEmpty() {
		return innerMap.isEmpty();
	}

	@Override
	public Enumeration<Integer> keys() {
		return new IterableEnumeration<Integer>(innerMap.keySet());
	}

	@Override
	public Enumeration<String> elements() {
		return new IterableEnumeration<String>(innerMap.values());
	}

	@Override
	public String get(Object key) {
		return innerMap.get(key);
	}

	@Override
	public String put(Integer key, String value) {
		reverseMap.put(value, key);
		return innerMap.put(key, value);
	}

	@Override
	public String remove(Object key) {
		reverseMap.remove(get(key));
		return innerMap.remove(key);
	}
	
	public int indexOf(String word) {
		Integer index = reverseMap.get(word);
		return index != null ? index.intValue() : -1;
	}
	
	public static Dictionary read(Configuration conf, Path path) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] ss = fs.listStatus(Paths.DICTIONARY);
		Dictionary dict = new Dictionary();
		for(FileStatus status : ss) {
			Path thisPath = status.getPath();
			if (path.getName().startsWith("_")) {
				continue;
			}
			if (status.isDir()) {
				continue;
			}
			FSDataInputStream inStream = fs.open(thisPath);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(inStream));
			String line;
			while((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line);
				if (token.countTokens() != 2) {
					continue;
				}
				Integer index = Integer.parseInt(token.nextToken());
				String word = token.nextToken();
				dict.put(index, word);
			}
		}
		return dict;
	}
	
	private static class IterableEnumeration<T> implements Enumeration<T>{

		private Iterator<T> iterator;
		
		private IterableEnumeration(Iterable<T> target) {
			this.iterator = target.iterator();
		}
		
		@Override
		public boolean hasMoreElements() {
			return iterator.hasNext();
		}

		@Override
		public T nextElement() {
			return iterator.next();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://aliyun-s1:9000");
		Dictionary dict = read(new Configuration(), Paths.DICTIONARY);
		System.out.println(dict);
	}

}
