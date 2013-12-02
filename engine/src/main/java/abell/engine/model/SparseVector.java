package abell.engine.model;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SparseVector implements Vector {

	TreeMap<Integer, Double> vectorMap;
	
	public SparseVector() {
		vectorMap = new TreeMap<Integer, Double>();
	}
	
	public SparseVector(Vector source) {
		this();
		if(SparseVector.class.isInstance(source)) {
			vectorMap.putAll(((SparseVector)source).vectorMap);
		} else {
			for(Entry entry : source) {
				vectorMap.put(entry.getIndex(), entry.getValue());
			}
		}
	}
	
	@Override
	public Iterator<Entry> iterator() {
		return null;
	}

	@Override
	public double getValue(int index) {
		if(!vectorMap.containsKey(index)){
			return 0.0d;
		}
		return vectorMap.get(index).doubleValue();
	}

	@Override
	public void setValue(int index, double value) {
		vectorMap.put(index, value);
	}

	@Override
	public int getDimension() {
		if(vectorMap.isEmpty()) {
			return 0;
		}
		return vectorMap.lastKey().intValue();
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
		return new SparseVector(this);
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
