package abell.engine.cluster;

import java.util.Collection;

import org.apache.commons.math.linear.RealVector;

import abell.engine.Vector;

public class CanopyClusterer<V extends Vector> implements Clusterer<V>{

	@Override
	public Collection<? extends Clustor<V>> cluster(Collection<V> items, int count) {
		return null;
	}
	
	private static class Canopy<V> implements Clusterer.Clustor<V> {

		@Override
		public RealVector centre() {
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
