package abell.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import abell.conf.Paths;
import abell.item.LuceneBasedHandler;

public class News {
	
	String id;

	String title;
	
	String content;

	String url;
	
	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getUrl() {
		return url;
	}
	
	public String getId() {
		return id;
	}

	private static final String RESOURCE_NAME = "abell/samples/news_tensite_xml.smarty.dat";
	
	public static Iterator<News> iterator() throws IOException{
	    try {
			InputStream inStream = News.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
			return new NewsIterator(new InputStreamReader(inStream, "gb2312"));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static List<News> list(int count) throws IOException{
	    try {
			InputStream inStream = News.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
			Iterator<News> samples = new NewsIterator(new InputStreamReader(inStream, "gb2312"), count);
			List<News> res = new ArrayList<News>(count);
			while(samples.hasNext()) {
				res.add(samples.next());
			}
			return res;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	private static class NewsIterator implements Iterator<News>{

		BufferedReader reader;
		
		News next;
		
		int max;
		
		int cursor = -1;
		
		private NewsIterator(Reader reader){
			this(reader, Integer.MAX_VALUE);
		}
		
		private NewsIterator(Reader reader, int max){
			this.reader = new BufferedReader(reader);
			this.max = max;
			fetchNext();
		}
		
		private void fetchNext(){
			try {
				News next = null;
				String line = reader.readLine();
				while(line != null){
					if("<doc>".equals(line)) {
						next = new News();
					}else if("</doc>".equals(line)) {
						break;
					}else if(line.startsWith("<docno>") && next != null) {
						next.id = line.substring(
								"<docno>".length(), line.lastIndexOf("</docno>"));
					}else if(line.startsWith("<contenttitle>") && next != null) {
						next.title = line.substring(
							"<contenttitle>".length(), line.lastIndexOf("</contenttitle>"));
					}else if(line.startsWith("<url>") && next != null) {
						next.url = line.substring(
							"<url>".length(), line.lastIndexOf("</url>"));
					}else if(line.startsWith("<content>") && next != null) {
						next.content = line.substring(
							"<content>".length(), line.lastIndexOf("</content>"));
					}
					line = reader.readLine();
				}
				this.next = next;
				cursor ++ ;
			} catch (IOException e) {
				e.printStackTrace();
				next = null;
			}
		}
		
		@Override
		public boolean hasNext() {
			return next != null && cursor < max;
		}

		@Override
		public News next() {
			News value = next;
			fetchNext();
			return value;
		}

		@Override
		public void remove() {}
		
	}
	
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		LuceneBasedHandler handler = new LuceneBasedHandler(conf);
		FileSystem fs = FileSystem.get(conf);
		fs.delete(Paths.ITEMS, true);
		for(News news : list(200)) {
			handler.push(news.getId(), new StringReader(news.getContent()));
		}
	}
	
}
