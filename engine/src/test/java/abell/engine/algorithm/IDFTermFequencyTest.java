package abell.engine.algorithm;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class IDFTermFequencyTest {

	private Analyzer analyzer;
	
	@Before
	public void setUp() {
		analyzer = new SmartChineseAnalyzer(Version.LUCENE_45);
	}

	@Test
	public void testParse() {
		try {
			TokenStream ts = analyzer.tokenStream("test", "");
			ts.reset();
			while(ts.incrementToken()){
				CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
				System.out.println(cta.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
