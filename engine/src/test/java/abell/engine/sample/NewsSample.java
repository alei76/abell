package abell.engine.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NewsSample {
	
	static final Log log = LogFactory.getLog(NewsSample.class);
	
	static final XMLInputFactory FACTORY = XMLInputFactory.newFactory();

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

	private static final String RESOURCE_NAME = "abell/engine/sample/news_tensite_xml.smarty.dat";
	
	public static Iterator<NewsSample> listAll() throws IOException{
	    try {
			InputStream inStream = NewsSample.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
			return new NewsIterator(new InputStreamReader(inStream, "gb2312"));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	private static class NewsIterator implements Iterator<NewsSample>{

		BufferedReader reader;
		
		NewsSample next;
		
		private NewsIterator(Reader reader){
			this.reader = new BufferedReader(reader);
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
			} catch (IOException e) {
				e.printStackTrace();
				next = null;
			}
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
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
