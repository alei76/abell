package abell.engine.algorithm;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;

import abell.engine.Tokenizer;

public class LuceneBasedTokenizer implements Tokenizer {
	
	private Analyzer analyzer;
	
	private String fieldName;
	
	private LuceneBasedTokenizer(String name) {
		fieldName = name;
	}

	@Override
	public Iterator<Token> parse(CharSequence chars) {
		//try {
			//analyzer.tokenStream(fieldName, chars.toString());
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		return null;
	}

	@Override
	public Iterator<Token> parseStream(InputStream inStream) {
		// TODO Auto-generated method stub
		return null;
	}

}
