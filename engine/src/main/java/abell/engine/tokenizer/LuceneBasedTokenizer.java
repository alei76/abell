package abell.engine.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class LuceneBasedTokenizer implements Tokenizer {
	
	private Analyzer analyzer;
	
	private String fieldName = "";
	
	public LuceneBasedTokenizer() {
		analyzer = new SmartChineseAnalyzer(Version.LUCENE_45);
	}

	@Override
	public TokenIterator iterator(CharSequence chars) throws IOException {
		TokenStream ts = analyzer.tokenStream(fieldName, chars.toString());
		return iterator(ts);
	}

	@Override
	public TokenIterator iterator(Reader reader) throws IOException {
		TokenStream ts = analyzer.tokenStream(fieldName, reader);
		return iterator(ts);
	}
	
	private TokenIterator iterator(TokenStream ts){
		return new TokenIteratorImpl(ts);
	}
	
	private static class TokenIteratorImpl implements TokenIterator{
		
		private TokenStream ts;
		
		private Hashtable<String, Integer> counter;
		
		private int index = -1;
		
		private Token token;
		
		private TokenIteratorImpl(TokenStream ts) {
			counter = new Hashtable<String, Integer>();
			this.ts = ts;
		}

		@Override
		public boolean next() throws IOException {
			boolean hasNext = ts.incrementToken();
			if(hasNext) {
				prepare();
			}
			return hasNext;
		}

		@Override
		public Token token() throws IOException {
			return token;
		}
		
		private void prepare(){
			CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
			token = new TokenImpl(cta.toString(), ++ index, counter);
			incr(token.text());
		}
		
		private synchronized void incr(String word) {
			Integer count = counter.get(word);
			if(count == null) {
				counter.put(word, 1);
			} else {
				counter.put(word, count + 1);
			}
		}
		
	}
	
	private static class TokenImpl implements Token {

		private String text;
		
		private int index;
		
		private Hashtable<String, Integer> counter;
		
		private TokenImpl(String text, int index, Hashtable<String, Integer> counter) {
			this.text = text;
			this.index = index;
			this.counter = counter;
		}
		
		@Override
		public String text() {
			return text;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public int count() {
			return counter.get(text);
		}
		
	}

}
