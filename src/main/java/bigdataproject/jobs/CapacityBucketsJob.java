package bigdataproject.jobs;

import org.apache.spark.ml.feature.Bucketizer;
import org.apache.spark.sql.*;

import static org.apache.spark.sql.functions.*;

public class CapacityBucketsJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Capacity Buckets")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", true)
                .csv("src/main/resources/file.csv")
                .withColumn("capacity", col("TANK CAPACITY").cast("double"));

        double[] splits = new double[]{Double.NEGATIVE_INFINITY, 1000, 5000, 10000, Double.POSITIVE_INFINITY};
        Bucketizer bucketizer = new Bucketizer()
                .setInputCol("capacity")
                .setOutputCol("capBucket")
                .setSplits(splits);

        Dataset<Row> bucketed = bucketizer.transform(df);

        Dataset<Row> result = bucketed.groupBy("capBucket")
                .count()
                .orderBy("capBucket");

        result.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("src/main/resources/output/capacity");
    }
}
