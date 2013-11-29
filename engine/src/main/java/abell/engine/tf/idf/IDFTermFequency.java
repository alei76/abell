package abell.engine.tf.idf;

import abell.engine.tf.DimensionMapper;
import abell.engine.tf.TermFequency;
import abell.engine.tokenizer.Tokenizer;
import abell.engine.tokenizer.Tokenizer.Token;
import abell.engine.tokenizer.Tokenizer.TokenIterator;
import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVector;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

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
    public RealVector parse(Reader reader, DimensionMapper mapper) throws IOException {
    	Handler handler = handlerFactory.getHandler();
        TreeMap<Dimension, Double> temp = new TreeMap<Dimension, Double>();
        HashSet<String> terms = new HashSet<String>();
    	//increate document count;
        handler.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(reader);
    			i.next();) {
            addFequency(temp, i.token(), mapper);
    	}
        reduceByIDF(temp, terms, handler, mapper);
    	//submit;
    	handler.resolve();
    	//return result;
        //TODO convert into RealVector
        return toVector(temp);
    }

    @Override
    public RealVector parse(String src, DimensionMapper mapper) throws IOException {
        return parse(new StringReader(src), mapper);
    }

    private void addFequency(TreeMap<Dimension, Double> temp, Token token, DimensionMapper mapper) {
        String term = token.text();
        int index = mapper.indexOf(term);
        Dimension dimension = new Dimension(index, term);
        Double score = temp.get(dimension);
        if(score == null) {
            temp.put(dimension, 1d);
        } else {
            temp.put(dimension, score + 1d);
        }
    }
    
    private void reduceByIDF(TreeMap<Dimension, Double> temp, HashSet<String> terms, Handler handler, DimensionMapper mapper){
    	for(String term : terms) {
            handler.increaseTerm(term);
    	}
        Iterator<RealVector.Entry> i;
        for(Map.Entry<Dimension, Double> e : temp.entrySet()) {
            Double score = e.getValue();
            Dimension dimension = e.getKey();
    		int dc = handler.countDocument();
    		int dw = handler.countTerm(dimension.term);
    		float idf = (float)Math.log((float)dc / (float)dw);
    		e.setValue(score * idf);
    	}
    }

    private RealVector toVector(TreeMap<Dimension, Double> temp) {
        Dimension maxDimension = temp.lastKey();
        RealVector vector = new OpenMapRealVector(maxDimension.index);
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
