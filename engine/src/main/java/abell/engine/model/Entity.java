package abell.engine.model;

import java.util.Iterator;

public class Entity implements Vector{
	
	private long id;

	private Vector vector;
	
	public Entity(long id, Vector vector) {
		this.id = id;
		this.vector = vector;
	}
	
	public long getId(){
		return id;
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
	
	public Entity copy(long newId) {
		return new Entity(newId, vector.copy());
	}

	public Iterator<Entry> iterator() {
		return vector.iterator();
	}
		
}
