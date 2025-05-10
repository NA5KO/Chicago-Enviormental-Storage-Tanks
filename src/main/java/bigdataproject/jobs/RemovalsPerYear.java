package bigdataproject.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RemovalsPerYear {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        private final static IntWritable one = new IntWritable(1);
        private Text year = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",", -1);
            if (fields.length > 11) {
                String dateStr = fields[11].trim(); // REMOVAL DATE
                try {
                    if (!dateStr.isEmpty()) {
                        Date date = dateFormat.parse(dateStr);
                        String y = new SimpleDateFormat("yyyy").format(date);
                        year.set(y);
                        context.write(year, one);
                    }
                } catch (Exception e) {}
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) sum += val.get();
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Removals Per Year");
        job.setJarByClass(RemovalsPerYear.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
