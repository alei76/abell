package abell.engine.test.tf.idf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import abell.engine.test.util.Components;
import abell.engine.test.util.NewsSample;
import abell.engine.tf.idf.IDFTermFequency;

import org.junit.Test;

import abell.engine.model.Vector;

public class IDFTermFequencyTest {
	
	IDFTermFequency termFequency = Components.TF_IDF;

	@Test
	public void testParse() throws MalformedURLException, IOException {
		Iterator<NewsSample> samples = NewsSample.iterator();
		while(samples.hasNext()){
			NewsSample news = samples.next();
			System.out.println(news.getTitle());
			Vector vector = termFequency.parse(news.getContent());
			int dimension = vector.getDimension();
			if(dimension == 0) {
				continue;
			}
			System.out.println(vector);
			System.out.println("");
		}
	}
}
