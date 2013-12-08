package abell.engine.cluster;

import java.util.Collection;

import abell.engine.model.Vector;

public interface Clusterer<V extends Vector> {

	Collection<? extends Clustor<V>> cluster(Collection<V> items);
	
	interface Clustor<I> {
		
		Vector centre();
		
		Collection<I> items();
		
	}
	
}
