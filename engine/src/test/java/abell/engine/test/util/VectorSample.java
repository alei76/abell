package abell.engine.test.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import abell.engine.model.Vector;

public class VectorSample {

	public static List<NewsVector> list(int count) throws IOException{
		List<NewsVector> vectors = new ArrayList<NewsVector>(count);
		List<NewsSample> samples = NewsSample.list(count);
		for(NewsSample sample : samples) {
			vectors.add(
				new NewsVector(sample,
					Components.TF_IDF.parse(sample.getContent())
				)
			);
		}
		return vectors;
	}
	
	public static class NewsVector implements Vector {

		private Vector vector;
		
		private NewsSample news;
		
		private NewsVector(NewsSample news, Vector vector) {
			this.news = news;
			this.vector = vector;
		}
		
		public NewsSample getNews() {
			return news;
		}
		
		public double getValue(int index) {
			return vector.getValue(index);
		}

		public void setValue(int index, double value) {
			vector.setValue(index, value);
		}

		public int getDimension() {
			return vector.getDimension();
		}

		public void add(Vector vector) {
			vector.add(vector);
		}

		public void substract(Vector vector) {
			vector.substract(vector);
		}

		public Vector copy() {
			return vector.copy();
		}

		public Iterator<Entry> iterator() {
			return vector.iterator();
		}
		
		public String toString() {
			return String.format("%s%s", news.getTitle(), vector);
		}
		
	}
	
}
