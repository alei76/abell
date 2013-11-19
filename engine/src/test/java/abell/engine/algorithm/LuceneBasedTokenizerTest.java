package abell.engine.algorithm;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import abell.engine.Tokenizer;
import abell.engine.Tokenizer.Token;
import abell.engine.Tokenizer.TokenIterator;

public class LuceneBasedTokenizerTest {
	
	Tokenizer tokenizer;
	
	static String RESOURCE = "abell/engine/algorithm/sample_chinese.txt";
	
	@Before
	public void setUp() {
		tokenizer = new LuceneBasedTokenizer();
	}

	@Test
	public void testParse() {
		try {
			InputStream sampleStream = getClass().getClassLoader().getResourceAsStream(RESOURCE);
			TokenIterator ti = tokenizer.iterator(sampleStream);
			while(ti.next()){
				Token token = ti.token();
				System.out.println(String.format("%s:%s:%s",
					token.index(), token.text(), token.count()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
