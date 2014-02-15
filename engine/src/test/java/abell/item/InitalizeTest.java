package abell.item;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import abell.engine.test.util.NewsSample;

public class InitalizeTest {

	static ItemHandler handler;
	
	@BeforeClass
	public static void cleanup() {
		Configuration conf = new Configuration();
		handler = new LuceneBasedHandler(conf);
	}
	
	@Test
	public void test() throws IOException {
		for(Iterator<NewsSample> itr = NewsSample.iterator();itr.hasNext();){
			NewsSample news = itr.next();
			handler.push(news.getId(), new StringReader(news.getContent()));
		}
	}

}
