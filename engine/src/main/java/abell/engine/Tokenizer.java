package abell.engine;

import java.io.IOException;
import java.io.InputStream;

public interface Tokenizer {

	TokenIterator iterator(CharSequence chars) throws IOException ;

	TokenIterator iterator(InputStream inStream) throws IOException ;
	
	interface Token {
		
		int index();
		
		String text();
		
		int count();
		
	}
	
	interface TokenIterator {
		
		boolean next() throws IOException;
		
		Token token() throws IOException;
		
	}
	
}
