package abell.engine.cluster;

import java.util.Collection;

import abell.engine.model.Vector;

public class CanopyClusterer<V extends Vector> implements Clusterer<V>{

	private double t1;
	
	private double t2;
	
	public CanopyClusterer(double t1, double t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	@Override
	public Collection<? extends Clustor<V>> cluster(Collection<V> items) {
		return null;
	}
	
	private static class Canopy<V> implements Clusterer.Clustor<V> {

		@Override
		public Vector centre() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<V> items() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
