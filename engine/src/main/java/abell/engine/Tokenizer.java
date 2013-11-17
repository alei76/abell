package abell.engine;

import java.io.InputStream;
import java.util.Iterator;

public interface Tokenizer {

	Iterator<Token> parse(CharSequence chars);

	Iterator<Token> parseStream(InputStream inStream);
	
	interface Token {
		
		String text();
		
	}
	
}
