package abell.engine.tf.idf;

import abell.engine.tf.TermFequency;
import abell.engine.tokenizer.Tokenizer;
import abell.engine.tokenizer.Tokenizer.Token;
import abell.engine.tokenizer.Tokenizer.TokenIterator;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Map;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public class IDFTermFequency implements TermFequency {
	
	private Tokenizer tokenizer;
	
	private ContextFactory contextFactory;
	
	IDFTermFequency(Tokenizer tokenizer, ContextFactory contextFactory) {
		this.tokenizer = tokenizer;
		this.contextFactory = contextFactory;
	}

    @Override
    public Map<String, Float> parse(Reader reader) throws IOException {
    	Context context = contextFactory.getContext();
    	Hashtable<String, Float> result = new Hashtable<String, Float>();
    	//increate document count;
    	context.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(reader);
    			i.next();) {
    		mapping(result, i.token(), context);
    	}
    	reduce(result, context);
    	//submit;
    	context.resolve();
    	return result;
    }
    
    @Override
    public Map<String, Float> parse(CharSequence chars) throws IOException {
    	Context context = contextFactory.getContext();
    	Hashtable<String, Float> result = new Hashtable<String, Float>();
    	//increate document count;
    	context.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(chars);
    			i.next();) {
    		mapping(result, i.token(), context);
    	}
    	reduce(result, context);
    	//submit;
    	context.resolve();
    	return result;
    }
    
    private void mapping(Hashtable<String, Float> result, Token token, Context context) {
		String text = token.text();
    	Float feq = result.get(text);
    	if(feq == null) {
    		feq = 1.0f;
    	} else {
    		feq = feq + 1;
    	}
    	result.put(text, feq);
    }
    
    private  void reduce(Hashtable<String, Float> result, Context context){
    	for(String term : result.keySet()) {
    		context.increaseTerm(term);
    	}
    	for(Map.Entry<String, Float> entry : result.entrySet()) {
    		int dc = context.countDocument();
    		int dw = context.countTerm(entry.getKey());
    		float idf = (float)Math.log((float)dc / (float)dw);
    		entry.setValue(entry.getValue() * idf);
    	}
    }
    
}
