package abell.engine.test.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import abell.engine.tf.DimensionMapper;
import abell.engine.tf.idf.IDFHandler;
import abell.engine.tf.idf.IDFTermFequency;
import abell.engine.tokenizer.LuceneBasedTokenizer;

public class Components {
	
	public static LuceneBasedTokenizer TOKENIZER_LUCENE = new LuceneBasedTokenizer();
	
	public static DimensionMapper DIMENSION_MAPPER = new DimensionMapper() {

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

	};
	
	private static IDFHandler TF_IDF_HANDLER = new IDFHandler() {

		//all document count
		long documentCount = 0;
		
		//term counts in all document
		Map<Integer, Long> termCounts = new Hashtable<Integer, Long>(); 

		@Override
		public void increaseDocument() {
			documentCount++;
		}

		@Override
		public void increaseDocument(int count) {
			documentCount = documentCount + count;
		}

		@Override
		public long countDocument() {
			return documentCount;
		}

		@Override
		public void increaseTerm(int index) {
			increaseTerm(index, 1);
		}

		@Override
		public void increaseTerm(int index, int step) {
			Long count = termCounts.get(index);
			if(count == null) {
				termCounts.put(index, new Long(step));
			} else {
				termCounts.put(index, count + step);
			}
		}

		@Override
		public long countTerm(int index) {
			Long count = termCounts.get(index);
	        return count == null ? 0 : count.longValue();
		}

		@Override
		public void resolve() {}
	};
	
	public static IDFTermFequency TF_IDF = new IDFTermFequency(
			TOKENIZER_LUCENE,
			TF_IDF_HANDLER,
			DIMENSION_MAPPER);
	
	
}
