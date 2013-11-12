package abell.engine;

import java.io.InputStream;
import java.util.Map;

/**
 * Author: GuoYu
 * Date: 13-11-12
 */
public interface TermFequency {

    Map<String, Integer> parse(InputStream inStream);

}
