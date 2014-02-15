package abell.item;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import abell.Param;

public abstract class ItemHandler {
	
	private static final Path BASE_PATH = new Path(Param.PATH_ITEMS);
	
	private String spliter;
	
	private Configuration conf;
	
	private FileSystem fs;
	
	protected ItemHandler(Configuration conf) {
		this.conf = conf;
		spliter = conf.get("mapreduce.input.keyvaluelinerecordreader.key.value.separator");
	}
	
	public void push(String id, Reader reader) throws IOException{
		ensureFileSystem();
		push(id, iterator(id, reader));
	}
	
	protected abstract Iterator<String> iterator(String id, Reader reader) throws IOException;
	
	private void push(String id, Iterator<String> itr) throws IOException{
		FSDataOutputStream outStream;
		Path path = new Path(BASE_PATH, id);
		if (fs.exists(path) ) {
			return;
		} 
		outStream = fs.create(path);
		outStream.writeChars(id);
		outStream.writeChars(spliter);
		while (itr.hasNext()) {
			String term = itr.next();
			outStream.writeChars(term);
			if (itr.hasNext())
				outStream.writeChar(' ');
		}
		outStream.close();
	}
	
	private synchronized void ensureFileSystem() throws IOException{
		if (fs == null) {
			fs = FileSystem.get(conf);
		}
	}
	
	
}
