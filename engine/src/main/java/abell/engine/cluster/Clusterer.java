package abell.engine.cluster;

import java.util.Collection;

import org.apache.commons.math.linear.RealVector;

import abell.engine.Vector;

public interface Clusterer<V extends Vector> {

	Collection<? extends Clustor<V>> cluster(Collection<V> items, int count);
	
	interface Clustor<I> {
		
		RealVector centre();
		
		Collection<I> items();
		
	}
	
}
