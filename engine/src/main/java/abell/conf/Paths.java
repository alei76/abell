package abell.conf;

import org.apache.hadoop.fs.Path;

public interface Paths {

	Path ITEMS = new Path("/abell/items");
    Path DICTIONARY = new Path("/abell/dictionary");

	Path TF_IDF = new Path("/abell/tfidf");
	Path TF_IDF_STEP1 = new Path(TF_IDF, "step1");
    Path TF_IDF_STEP2 = new Path(TF_IDF, "step2");
    Path TF_IDF_RESULT = new Path(TF_IDF, "result");
	
}
