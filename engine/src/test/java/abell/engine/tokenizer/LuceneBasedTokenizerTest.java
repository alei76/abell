package abell.engine.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import abell.engine.tokenizer.LuceneBasedTokenizer;
import abell.engine.tokenizer.Tokenizer;
import abell.engine.tokenizer.Tokenizer.Token;
import abell.engine.tokenizer.Tokenizer.TokenIterator;

public class LuceneBasedTokenizerTest {
	
	Tokenizer tokenizer;
	
	static String RESOURCE = "abell/engine/tokenizer/sample_chinese.dat";
	
	@Before
	public void setUp() {
		tokenizer = new LuceneBasedTokenizer();
	}

	@Test
	public void testParse() {
		try {
			InputStream sampleStream = getClass().getClassLoader().getResourceAsStream(RESOURCE);
			TokenIterator ti = tokenizer.iterator(new InputStreamReader(sampleStream, "utf8"));
			while(ti.next()){
				Token token = ti.token();
				System.out.println(String.format("%s:%s", token.text(), token.count()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
