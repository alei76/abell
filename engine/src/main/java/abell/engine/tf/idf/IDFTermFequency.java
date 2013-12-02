package abell.engine.tf.idf;

import abell.engine.model.SparseVector;
import abell.engine.model.Vector;
import abell.engine.tf.DimensionMapper;
import abell.engine.tf.TermFequency;
import abell.engine.tokenizer.Tokenizer;
import abell.engine.tokenizer.Tokenizer.Token;
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
	
	private HandlerFactory handlerFactory;
	
	IDFTermFequency(Tokenizer tokenizer, HandlerFactory handlerFactory) {
		this.tokenizer = tokenizer;
		this.handlerFactory = handlerFactory;
	}

    @Override
    public Vector parse(Reader reader, DimensionMapper mapper) throws IOException {
    	Handler handler = handlerFactory.getHandler();
    	SparseVector vector = new SparseVector();
        HashSet<String> terms = new HashSet<String>();
    	//increate document count;
        handler.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(reader);
    			i.next();) {
    		String term = i.token().text();
            addFequency(vector, term, mapper);
            terms.add(term);
    	}
        reduceByIDF(temp, terms, handler, mapper);
    	//submit;
    	handler.resolve();
    	//return result;
        //TODO convert into RealVector
        return toVector(temp);
    }

    @Override
    public Vector parse(String src, DimensionMapper mapper) throws IOException {
        return parse(new StringReader(src), mapper);
    }

    private void addFequency(SparseVector vector, String term, DimensionMapper mapper) {
        int index = mapper.indexOf(term);
        Dimension dimension = new Dimension(index, term);
        double score = vector.getValue(index);
        vector.setValue(index, score + 1.0d);
    }
    
    private void reduceByIDF(TreeMap<Dimension, Double> temp, HashSet<String> terms, Handler handler, DimensionMapper mapper){
    	for(String term : terms) {
            handler.increaseTerm(term);
    	}
        for(Map.Entry<Dimension, Double> e : temp.entrySet()) {
            Double score = e.getValue();
            Dimension dimension = e.getKey();
    		int dc = handler.countDocument();
    		int dw = handler.countTerm(dimension.term);
    		float idf = (float)Math.log((float)dc / (float)dw);
    		e.setValue(score * idf);
    	}
    }

    private Vector toVector(TreeMap<Dimension, Double> temp) {
        if(temp.size() == 0) {
            return new SparseVector();
        }
        Dimension maxDimension = temp.lastKey();
        OpenMapRealVector vector = new OpenMapRealVector(maxDimension.index + 1);
        for(Map.Entry<Dimension, Double> e :temp.entrySet()){
            Dimension dimension = e.getKey();
            double score = e.getValue();
            vector.setEntry(dimension.index, score);
        }
        return vector;
    }

    private static class Dimension implements Comparable<Dimension> {

        private int index;

        private String term;

        private Dimension(int index, String term){
            this.index = index;
            this.term = term;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dimension dimension = (Dimension) o;

            if (index != dimension.index) return false;
            if (term != null ? !term.equals(dimension.term) : dimension.term != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return index;
        }

        @Override
        public int compareTo(Dimension o) {
            return index - o.index;
        }
    }
    
}
