package abell.engine.reduction;

import java.util.Collection;

import org.apache.commons.math.linear.RealVector;

public interface Reduction {
	
	public Collection<? extends RealVector> reduceAll(Collection<? extends RealVector> vector);
	
	public RealVector reduce(RealVector vector);

}
