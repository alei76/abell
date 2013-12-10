package abell.engine.test.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import abell.engine.model.Measure;
import abell.engine.test.util.Components;
import abell.engine.test.util.VectorSample;
import abell.engine.test.util.VectorSample.NewsVector;
import abell.engine.tf.idf.IDFTermFequency;

public class CosineMeasureTest {
	
	IDFTermFequency termFequency = Components.TF_IDF;
	
	private Measure measure = Measure.cosine();
	
	private NewsVector base;
	
	private List<NewsVector> samples;
	
	private HashMap<NewsVector, Double> distances;
	
	@Before
	public void setUp() throws IOException {
		distances = new HashMap<NewsVector, Double>();
		samples = VectorSample.list(199);
		base = samples.remove(119);
	}

	@Test
	public void testParse() throws MalformedURLException, IOException {
		calulate();
		sort();
		for(NewsVector vector : samples) {
			System.out.println(vector);
			System.out.println(distances.get(vector));
		}
	}
	
	private void sort() {
		Collections.sort(samples, new Comparator<NewsVector>() {
			@Override
			public int compare(NewsVector v1, NewsVector v2) {
				double d1 = distances.get(v1);
				double d2 = distances.get(v2);
				return -Double.compare(d1, d2);
			}
			
		});
	}
	
	private void calulate(){
		for(NewsVector vector : samples) {
			distances.put(vector, measure.distance(base, vector));
		}
	}
	
}
