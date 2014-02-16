package abell.item;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import abell.conf.Paths;

public abstract class ItemHandler {
	
	private static final int DEFAULT_PAGE_SIZE = 1000;
	
	private String spliter;
	
	private Configuration conf;
	
	private FSDataOutputStream outStream;
	
	private FileSystem fs;
	
	private int pageSize, offset, page;
	
	protected ItemHandler(Configuration conf, int pageSize) {
		this.conf = conf;
		this.pageSize = pageSize;
		spliter = conf.get("mapreduce.input.keyvaluelinerecordreader.key.value.separator");
		offset = 0;
		page = 0;
	}
	
	protected ItemHandler(Configuration conf) {
		this(conf, DEFAULT_PAGE_SIZE);
	}
	
	public void push(String id, Reader reader) throws IOException{
		ensureFileSystem();
		push(id, iterator(id, reader));
	}
	
	public void close() {
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract Iterator<String> iterator(String id, Reader reader) throws IOException;
	
	private void push(String id, Iterator<String> itr) throws IOException{
		StringBuffer line;
		nextOutput();
		Path path = new Path(Paths.ITEMS, "page-" + page);
		if (!fs.exists(path)) {
			return;
		} 
		line = new StringBuffer(id);
		line.append(spliter);
		while (itr.hasNext()) {
			String term = itr.next();
			line.append(term);
			if (itr.hasNext())
				line.append(' ');
		}
		line.append("\n");
		outStream.write(line.toString().getBytes());
	}
	
	private void nextOutput() throws IOException {
		if (offset == pageSize) {
			offset = -1;
			page++;
			if (outStream != null) {
				outStream.close();
			}
			outStream = null;
		} else {
			offset ++;
		}
		if (outStream == null) {
			Path path = new Path(Paths.ITEMS, "page-" + page);
			outStream = fs.create(path);
		}
	}
	
	private synchronized void ensureFileSystem() throws IOException{
		if (fs == null) {
			fs = FileSystem.get(conf);
		}
	}
	
	
}
