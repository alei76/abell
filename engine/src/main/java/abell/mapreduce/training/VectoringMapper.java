package abell.mapreduce.training;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import abell.mapreduce.model.Dictionary;

public class VectoringMapper extends Mapper<Text, Text, Text, VectorWritable> {

	private Dictionary dict;
	
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		dict = Dictionary.read(context.getConfiguration());
	}
	
	@Override
	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {
		Vector vector = new RandomAccessSparseVector(dict.size());
		StringTokenizer st = new StringTokenizer(value.toString());
        while (st.hasMoreTokens()) {
            String wordStr = st.nextToken();
            if (wordStr.trim().length() == 0) {
                continue;
            }
            int index = dict.indexOf(wordStr);
            if (index == -1) {
                continue;
            }
            double count = vector.get(index);
            vector.set(index, count + 1);
        }
        context.write(key, new VectorWritable(vector));
	}

}
