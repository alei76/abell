package abell.item;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class LuceneBasedHandler extends ItemHandler {

	private static Log LOG = LogFactory.getLog(LuceneBasedHandler.class);
	private Analyzer analyzer;
	private String FIELD_NAME = "abell.item";
	
	protected LuceneBasedHandler(Configuration conf) {
		super(conf);
		analyzer = new SmartChineseAnalyzer(Version.LUCENE_45);
	}

	@Override
	public Iterator<String> iterator(String id, Reader reader) throws IOException {
		TokenStream tokenStream = analyzer.tokenStream(FIELD_NAME, reader);
		return new IteratorImpl(tokenStream);
	}
	
	private static class IteratorImpl implements Iterator<String> {
		
		private TokenStream tokenStream;
		
		private String next;
		
		private IteratorImpl(TokenStream tokenStream) {
			this.tokenStream = tokenStream;
			fetchNext();
		}

		@Override
		public boolean hasNext() {
			try {
				return tokenStream.incrementToken();
			} catch (IOException e) {
				LOG.warn(e);
				return false;
			}
		}

		@Override
		public String next() {
			String ret = next;
			fetchNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
		
		private void fetchNext() {
			try {
				if (tokenStream.incrementToken()) {
					CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
					next = term.toString();
				} else {
					next = null;
				}
			} catch (IOException e) {
				LOG.warn(e);
				next = null;
			}
		}
		
	}

}
