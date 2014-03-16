package abell.mapreduce.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import abell.conf.Paths;

public class Dictionary extends LinkedList<String>{
	
	private static final long serialVersionUID = -1696389280754245350L;

	public static Dictionary read(Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] ss = fs.listStatus(Paths.MODEL_DICT, filter);
        Dictionary dict = new Dictionary();
		for(FileStatus status : ss) {
			Path thisPath = status.getPath();
			if (thisPath.getName().startsWith("_")) {
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
				dict.add(index, word);
			}
		}
		return dict;
	}

    private static PathFilter filter = new PathFilter() {

        @Override
        public boolean accept(Path path) {
            return path.getName().startsWith("part-");
        }

    };
	
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://aliyun-s1:9000");
		Dictionary dict = read(new Configuration());
		System.out.println(dict);
	}

}
