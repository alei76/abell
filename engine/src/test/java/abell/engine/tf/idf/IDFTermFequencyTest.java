package abell.engine.tf.idf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import abell.engine.sample.NewsSample;
import abell.engine.tf.idf.Context;
import abell.engine.tf.idf.IDFTermFequency;
import abell.engine.tokenizer.LuceneBasedTokenizer;

public class IDFTermFequencyTest {
	
	IDFTermFequency termFequency;
	
	ContextFactory contextFactory = new ContextFactory() {

		@Override
		public Context getContext() {
			return context;
		}
		
	};
	
	Context context = new Context() {
		
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
				new LuceneBasedTokenizer(), contextFactory);
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
