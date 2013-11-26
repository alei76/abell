package abell.engine.tf.idf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import abell.engine.sample.NewsSample;
import abell.engine.tokenizer.LuceneBasedTokenizer;

public class IDFTermFequencyTest {
	
	IDFTermFequency termFequency;
	
	HandlerFactory handlerFactory = new HandlerFactory() {

		@Override
		public Handler getHandler() {
			return handler;
		}
		
	};
	
	Handler handler = new Handler() {
		
		//all document count
		int documentCount = 0;
		
		//term counts in all document
		Map<CharSequence, Integer> termCounts = new Hashtable<CharSequence, Integer>(); 

		@Override
		public void increaseDocument() {
			documentCount++;
		}

		@Override
		public void increaseDocument(int count) {
			documentCount = documentCount + count;
		}

		@Override
		public int countDocument() {
			return documentCount;
		}

		@Override
		public void increaseTerm(CharSequence chars) {
			increaseTerm(chars, 1);
		}

		@Override
		public void increaseTerm(CharSequence chars, int step) {
			Integer count = termCounts.get(chars);
			if(count == null) {
				termCounts.put(chars, step);
			} else {
				termCounts.put(chars, count + step);
			}
		}

		@Override
		public int countTerm(CharSequence chars) {
			return termCounts.get(chars);
		}

		@Override
		public void resolve() {}
		
	};
	
	@Before
	public void setUp() {
		termFequency = new IDFTermFequency(
				new LuceneBasedTokenizer(), handlerFactory);
	}

	@Test
	public void testParse() throws MalformedURLException, IOException {
		Iterator<NewsSample> samples = NewsSample.listAll();
		while(samples.hasNext()){
			NewsSample sample = samples.next();
			Map<String, Float> termFequencies = termFequency.parse(sample.getContent());
			System.out.println(sample.getTitle());
			System.out.println(termFequencies);
			System.out.println("");
		}
	}
}
