package bigdataproject.jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TankCapacityAnalysis {
    public static class Map extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private IntWritable capacityWritable = new IntWritable();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",", -1);
            if (fields.length > 14) {
                String capacityStr = fields[14].trim(); // CAPACITY
                try {
                    int capacity = Integer.parseInt(capacityStr);
                    capacityWritable.set(capacity);
                    context.write(capacityWritable, one);
                } catch (NumberFormatException e) {}
            }
        }
    }

    public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable val : values) count += val.get();
            context.write(key, new IntWritable(count));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Tank Capacity Analysis");
        job.setJarByClass(TankCapacityAnalysis.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
