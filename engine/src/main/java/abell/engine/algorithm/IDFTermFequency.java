package abell.engine.algorithm;

import abell.engine.TermFequency;
import abell.engine.TermFequencyStatistics;
import abell.engine.Tokenizer;
import abell.engine.Tokenizer.Token;
import abell.engine.Tokenizer.TokenIterator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public class IDFTermFequency implements TermFequency {
	
	private Tokenizer tokenizer;
	
	IDFTermFequency(Tokenizer tokenizer, TermFequencyStatistics stat) {
		this.tokenizer = tokenizer;
	}

    @Override
    public Map<String, Float> parse(InputStream inStream) throws IOException {
    	for(TokenIterator i = tokenizer.iterator(inStream);
    			i.next();) {
    		Token token = i.token();
    	}
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
}
