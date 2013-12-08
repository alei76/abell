package abell.engine.model;

public abstract class Measure {

	public abstract double distance(Vector v1, Vector v2);
	
	private static CosineMeasure cosingMeasure;
	
	static {
		cosingMeasure = new CosineMeasure();
	}
	
	public static Measure cosine() {
		return cosingMeasure;
	}
	
}
