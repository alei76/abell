package abell.engine.tf;

/**
 * Author: GuoYu
 * Date: 13-11-27
 */
public interface DimensionMapper extends Iterable<String> {

    int indexOf(String term);

    int size();

    String termAt(int index);

}
