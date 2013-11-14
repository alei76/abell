package abell.engine;

import org.apache.mahout.math.Vector;

/**
 * Author: GuoYu
 * Date: 13-11-13
 */
public interface IdentifiedVector extends Vector {

    long getId();

}
