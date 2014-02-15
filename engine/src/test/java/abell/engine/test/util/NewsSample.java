package abell.engine.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsSample {
	
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

	private static final String RESOURCE_NAME = "abell/engine/sample/news_tensite_xml.smarty.dat";
	
	public static Iterator<NewsSample> iterator() throws IOException{
	    try {
			InputStream inStream = NewsSample.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
			return new NewsIterator(new InputStreamReader(inStream, "gb2312"));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static List<NewsSample> list(int count) throws IOException{
	    try {
			InputStream inStream = NewsSample.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
			Iterator<NewsSample> samples = new NewsIterator(new InputStreamReader(inStream, "gb2312"), count);
			List<NewsSample> res = new ArrayList<NewsSample>(count);
			while(samples.hasNext()) {
				res.add(samples.next());
			}
			return res;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	private static class NewsIterator implements Iterator<NewsSample>{

		BufferedReader reader;
		
		NewsSample next;
		
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
				NewsSample next = null;
				String line = reader.readLine();
				while(line != null){
					if("<doc>".equals(line)) {
						next = new NewsSample();
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
		public NewsSample next() {
			NewsSample value = next;
			fetchNext();
			return value;
		}

		@Override
		public void remove() {}
		
	}
	
}
