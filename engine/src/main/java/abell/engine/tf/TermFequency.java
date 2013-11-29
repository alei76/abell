package abell.engine.tf;

import org.apache.commons.math.linear.RealVector;

import java.io.IOException;
import java.io.Reader;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface TermFequency {

    RealVector parse(Reader reader, DimensionMapper mapper) throws IOException ;

    RealVector parse(String src, DimensionMapper mapper) throws IOException ;

}
