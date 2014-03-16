package abell.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import abell.conf.Paths;

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

	private static final String Path = "E:\\data\\sohunews";
	
	public static List<News> list(File file) throws IOException{
        try {
            InputStream inStream = new FileInputStream(file);
            Iterator<News> samples = new NewsIterator(new InputStreamReader(inStream, "gbk"));
            List<News> res = new ArrayList<News>();
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
	
	private static final int FILE_LIMIT = 5;
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		clear(conf);
		File folder = new File(Path);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2000);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		LuceneBasedWriter handler = new LuceneBasedWriter(conf, "news-samples-all");
		int i = 0;
		for(File file : folder.listFiles()) {
			appendFile(file, cal, handler);
			i ++ ;
			if (i == FILE_LIMIT) {
				break;
			}
		}
        handler.close();
	}
	
	private static void clear(Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		fs.delete(Paths.ITEMS_ORIGIN, true);
	}
	
	private static void appendFile(File file, Calendar cal, LuceneBasedWriter handler)
			throws IOException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for(News news : list(file)) {
            if (news.getContent().length() < 10) {
                continue;
            }
    		String suffix = format.format(cal.getTime());
			handler.append(news.getId() + "/" + suffix,
				new StringReader(news.getContent()));
			cal.add(Calendar.HOUR_OF_DAY, 3);
		}
		System.out.println(file.getName() + " finished.");
		handler.flush();
	}
	
}
