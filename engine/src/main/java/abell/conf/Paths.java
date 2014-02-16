package abell.conf;

import org.apache.hadoop.fs.Path;

public interface Paths {

	Path ITEMS = new Path("/abell/items");
	Path TF_IDF = new Path("/abell/tfidf");
	Path TF_INF_STEP1 = new Path(TF_IDF, "step1");
	Path TF_INF_STEP2 = new Path(TF_IDF, "step2");
	
}
