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
	
	private HandlerFactory handlerFactory;
	
	IDFTermFequency(Tokenizer tokenizer, HandlerFactory handlerFactory) {
		this.tokenizer = tokenizer;
		this.handlerFactory = handlerFactory;
	}

    @Override
    public Map<String, Float> parse(Reader reader) throws IOException {
    	Handler context = handlerFactory.getHandler();
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
    	Handler handler = handlerFactory.getHandler();
    	Hashtable<String, Float> result = new Hashtable<String, Float>();
    	//increate document count;
        handler.increaseDocument();
    	for(TokenIterator i = tokenizer.iterator(chars);
    			i.next();) {
    		mapping(result, i.token(), handler);
    	}
    	reduce(result, handler);
    	//submit;
        handler.resolve();
    	return result;
    }
    
    private void mapping(Hashtable<String, Float> result, Token token, Handler context) {
		String text = token.text();
    	Float feq = result.get(text);
    	if(feq == null) {
    		feq = 1.0f;
    	} else {
    		feq = feq + 1;
    	}
    	result.put(text, feq);
    }
    
    private  void reduce(Hashtable<String, Float> result, Handler handler){
    	for(String term : result.keySet()) {
            handler.increaseTerm(term);
    	}
    	for(Map.Entry<String, Float> entry : result.entrySet()) {
    		int dc = handler.countDocument();
    		int dw = handler.countTerm(entry.getKey());
    		float idf = (float)Math.log((float)dc / (float)dw);
    		entry.setValue(entry.getValue() * idf);
    	}
    }
    
}
