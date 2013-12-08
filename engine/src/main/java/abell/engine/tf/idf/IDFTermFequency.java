package abell.engine.tf.idf;

import abell.engine.model.SparseVector;
import abell.engine.model.Vector;
import abell.engine.tf.DimensionMapper;
import abell.engine.tf.TermFequency;
import abell.engine.tokenizer.Tokenizer;
import abell.engine.tokenizer.Tokenizer.TokenIterator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public class IDFTermFequency implements TermFequency {
	
	private Tokenizer tokenizer;
	
	private IDFHandler handler;
	
	private DimensionMapper mapper;
	
	public IDFTermFequency(Tokenizer tokenizer,
			IDFHandler handler,
			DimensionMapper mapper) {
		this.tokenizer = tokenizer;
		this.handler = handler;
		this.mapper = mapper;
	}

    @Override
    public Vector parse(Reader reader) throws IOException {
    	SparseVector vector = new SparseVector();
        HashSet<Integer> indexes = new HashSet<Integer>();
        int termCount = 0;
        handler.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(reader);
    			i.next();) {
    		String term = i.token().text();
    		int index = mapper.indexOf(term);
            double score = vector.getValue(index);
            vector.setValue(index, score + 1.0d);
            indexes.add(index);
            termCount ++ ;
    	}
        reduceByIDF(vector, termCount, indexes, handler);
    	handler.resolve();
        return vector;
    }

    @Override
    public Vector parse(String src) throws IOException {
        return parse(new StringReader(src));
    }
    
    private void reduceByIDF(Vector vector, int termCount, HashSet<Integer> indexes, IDFHandler handler){
    	for(int index : indexes) {
            handler.increaseTerm(index);
    	}
        for(Vector.Entry e : vector) {
            double score = e.getValue() / (double)termCount;
            int index = e.getIndex();
    		long dc = handler.countDocument();
    		long dw = handler.countTerm(index);
    		double idf = Math.log((double)dc / (double)dw);
    		e.setValue(score * idf);
    	}
    }
    
}
