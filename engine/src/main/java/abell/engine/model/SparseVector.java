package abell.engine.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SparseVector implements Vector {

	private HashMap<Integer, Double> vectorMap;
	
	private int dimension = 0;
	
	private double defaultValue;
	
	public SparseVector() {
		this(0.0d);
	}
	
	public SparseVector(double defaultValue) {
		this.defaultValue = defaultValue;
		vectorMap = new HashMap<Integer, Double>();
	}
	
	public SparseVector(double defaultValue, Vector source) {
		this(defaultValue);
		dimension = source.getDimension();
		if(SparseVector.class.isInstance(source)) {
			vectorMap.putAll(((SparseVector)source).vectorMap);
		} else {
			for(Entry entry : source) {
				vectorMap.put(entry.getIndex(), entry.getValue());
			}
		}
	}
	
	@Override
	public Iterator<Vector.Entry> iterator() {
		return new SparseIterator(vectorMap.entrySet().iterator());
	}

	@Override
	public double getValue(int index) {
		if(!vectorMap.containsKey(index)){
			return defaultValue;
		}
		return vectorMap.get(index).doubleValue();
	}

	@Override
	public void setValue(int index, double value) {
		assign(index);
		vectorMap.put(index, value);
	}

	@Override
	public int getDimension() {
		return dimension;
	}
	
	@Override
	public void add(Vector vector) {
		for(Entry e : vector){
			int index = e.getIndex();
			double value = e.getValue();
			setValue(index, getValue(index) + value);
		}
	}

	@Override
	public void substract(Vector vector) {
		for(Entry e : vector){
			int index = e.getIndex();
			double value = e.getValue();
			setValue(index, getValue(index) - value);
		}
	}

	@Override
	public Vector copy() {
		return new SparseVector(defaultValue, this);
	}
	
	@Override
	public String toString() {
		return vectorMap.toString();
	}
	
	private void assign(int index) {
		if(index < dimension) {
			return;
		}
		dimension = index + 1;
	}
	
	private static final class SparseIterator implements Iterator<Vector.Entry> {
		
		Iterator<Map.Entry<Integer, Double>> iterator;
		
		private SparseIterator(Iterator<Map.Entry<Integer, Double>> iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public SparseEntry next() {
			return new SparseEntry(iterator.next());
		}

		public void remove() {
			iterator.remove();
		}
		
	}
	
	private static final class SparseEntry implements Vector.Entry {

		private Map.Entry<Integer, Double> mapEntry;
		
		private SparseEntry(Map.Entry<Integer, Double> entry){
			mapEntry = entry;
		}
		
		@Override
		public int getIndex() {
			return mapEntry.getKey().intValue();
		}

		@Override
		public double getValue() {
			return mapEntry.getValue().doubleValue();
		}

		@Override
		public void setValue(double value) {
			mapEntry.setValue(value);
		}
		
	}

}
