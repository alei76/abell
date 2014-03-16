package abell.content;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import abell.conf.Paths;

public abstract class ContentWriter {
	
	private String spliter;
	
	private Configuration conf;
	
	private FSDataOutputStream outStream;
	
	private FileSystem fs;

    private Path target;
	
	protected ContentWriter(Configuration conf, String name) {
		this.conf = conf;
		spliter = conf.get("mapreduce.input.keyvaluelinerecordreader.key.value.separator");
        target = new Path(Paths.ITEMS_ORIGIN, name);
	}
	
	public void append(String id, Reader reader) throws IOException{
		ensureFileSystem();
		push(id, iterator(id, reader));
	}
	
	public void flush() {
		if (outStream == null) {
			return;
		}	
		try {
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (outStream == null) {
			return;
		}	
		try {
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract Iterator<String> iterator(String id, Reader reader) throws IOException;
	
	private void push(String id, Iterator<String> itr) throws IOException{
		StringBuffer line;
        if (outStream == null) {
            outStream = fs.create(target);
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
        outStream.write(line.toString().getBytes("utf8"));
	}
	
	private synchronized void ensureFileSystem() throws IOException{
		if (fs == null) {
			fs = FileSystem.get(conf);
		}
	}
	
	
}
