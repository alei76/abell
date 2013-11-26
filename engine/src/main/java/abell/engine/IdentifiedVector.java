package abell.engine;

import org.apache.commons.math.linear.RealVector;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface IdentifiedVector extends RealVector {

    long getId();

}
