package abell.conf;

import org.apache.hadoop.fs.Path;

public interface Paths {

    Path ITEMS = new Path("/abell/items");
    Path ITEMS_ORIGIN = new Path(ITEMS, "origin");
    Path ITEMS_SEQUENCE = new Path(ITEMS, "sequence");
    Path ITEMS_VECTOR = new Path(ITEMS, "vector");

    Path MODEL = new Path("/abell/model");
    Path MODEL_DICT = new Path(MODEL, "dict");
    Path MODEL_LDA = new Path(MODEL, "lda");
    Path MODEL_TOPIC = new Path(MODEL, "topic");
	
}
