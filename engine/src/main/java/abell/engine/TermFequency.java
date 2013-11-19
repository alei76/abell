package abell.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface TermFequency {

    Map<String, Float> parse(InputStream inStream) throws IOException ;

}
