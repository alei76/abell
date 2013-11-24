package abell.engine.tokenizer;

import java.io.IOException;
import java.io.Reader;

public interface Tokenizer {

	TokenIterator iterator(CharSequence chars) throws IOException ;

	TokenIterator iterator(Reader reader) throws IOException ;
	
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
