package abell.engine.tf;

import java.io.IOException;
import java.io.Reader;

import abell.engine.model.Vector;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface TermFequency {

    Vector parse(Reader reader) throws IOException ;

    Vector parse(String src) throws IOException ;

}
