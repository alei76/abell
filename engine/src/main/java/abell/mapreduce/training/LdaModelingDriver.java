package abell.mapreduce.training;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.lda.LDADriver;

import abell.conf.Paths;

public class LdaModelingDriver extends Configured implements Tool {

    static final int MAX_ITER = 10;

    static final int TOPICS = 100;
	@Override
	public int run(String[] arg0) throws Exception {
        Configuration ldaConf = new Configuration(getConf());
        String[] ldaArgs = new String[]{
            "--input", Paths.ITEMS_VECTOR.toString(),
            "--output", Paths.MODEL_LDA.toString(),
            "--overwrite",
            "--numTopics", String.valueOf(TOPICS),
            "--maxIter", String.valueOf(MAX_ITER)
        };
        return ToolRunner.run(ldaConf, new LDADriver(), ldaArgs);
	}

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new LdaModelingDriver(), args);
    }

}
