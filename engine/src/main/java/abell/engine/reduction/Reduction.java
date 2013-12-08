package abell.engine.reduction;

import java.util.Collection;

import abell.engine.model.Vector;

public interface Reduction {
	
	public Collection<? extends Vector> reduceAll(Collection<? extends Vector> vector);
	
	public Vector reduce(Vector vector);

}
