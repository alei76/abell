package abell.engine.model;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface Vector extends Iterable<Vector.Entry>{
    
    double getValue(int index);
    
    void setValue(int index, double value);
    
    int getDimension();

    void add(Vector vector);
    
    void substract(Vector vector);
    
    Vector copy();
    
    interface Entry {
    	
    	int getIndex();
    	
    	double getValue();
    	
    	void setValue(double value);
    	
    }

}
