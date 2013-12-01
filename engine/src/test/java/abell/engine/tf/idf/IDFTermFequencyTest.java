package abell.engine.tf.idf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import abell.engine.tf.DimensionMapper;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import abell.engine.sample.NewsSample;
import abell.engine.tokenizer.LuceneBasedTokenizer;

public class IDFTermFequencyTest {
	
	IDFTermFequency termFequency;

    DimensionMapper mapper = new DimensionMapper() {

        private int seq = 0;

        private HashMap<String, Integer> termToIndex = new HashMap<String, Integer>();

        private HashMap<Integer, String> indexToTerm = new HashMap<Integer, String>();

        @Override
        public int indexOf(String term) {
            Integer index = termToIndex.get(term);
            if(index == null) {
                index = seq ++ ;
                termToIndex.put(term, index);
                indexToTerm.put(index, term);
            }
            return index;
        }

        @Override
        public int size() {
            return termToIndex.size();
        }

        @Override
        public String termAt(int index) {
            return indexToTerm.get(index);
        }

        @Override
        public Iterator<String> iterator() {
            return null;
        }
    } ;
	
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
			Integer count = termCounts.get(chars);
            return count == null ? 0 : count.intValue();
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
		RealVector prev = null;
		while(samples.hasNext()){
			NewsSample news = samples.next();
			RealVector vector = termFequency.parse(
				news.getContent(), mapper);
			int dimension = vector.getDimension();
			if(dimension == 0) {
				continue;
			}
			if(prev == null) {
				prev = vector;
				continue;
			}
			OpenMapRealVector sample2 = new OpenMapRealVector(vector.getDimension());
			sample2.setSubVector(0, prev);
			System.out.println(news.getTitle());
			System.out.println(sample2.getDistance(vector));
			System.out.println("");
			prev = vector;
		}
	}
}
