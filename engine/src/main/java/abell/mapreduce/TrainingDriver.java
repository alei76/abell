package abell.mapreduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Author: GuoYu
 * Date: 14-2-20
 */
public class TrainingDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new TrainingDriver(), args);
    }
}