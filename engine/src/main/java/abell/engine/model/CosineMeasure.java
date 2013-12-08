package abell.engine.model;

public class CosineMeasure extends Measure {

	CosineMeasure() {}

	@Override
	//TODO optimizee
	public double distance(Vector v1, Vector v2) {
		int d1 = v1.getDimension();
		int d2 = v2.getDimension();
		double sqrtSum1 = 0.0d;
		double sqrtSum2 = 0.0d;
		double multiSum = 0.0d;
		for(int index = 0, dimension = Math.max(d1, d2);
				index < dimension; index++) {
			multiSum += (v1.getValue(index) * v2.getValue(index));
			sqrtSum1 += Math.pow(v1.getValue(index), 2);
			sqrtSum2 += Math.pow(v2.getValue(index), 2);
		}
		return multiSum / (Math.sqrt(sqrtSum1) * Math.sqrt(sqrtSum2));
	}
	
}
