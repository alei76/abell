package abell.engine.tf;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface TermFequency {

    Map<String, Float> parse(Reader reader) throws IOException ;

    Map<String, Float> parse(CharSequence chars) throws IOException ;

}
