package bigdataproject.jobs;

import org.apache.spark.sql.*;

import static org.apache.spark.sql.functions.*;

public class TopProductsJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Top Products Stored")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", true)
                .csv("src/main/resources/file.csv");

        Dataset<Row> cleaned = df.withColumn("product",
                        trim(upper(col("PRODUCT STORED"))))
                .filter(col("product").isNotNull())
                .groupBy("product")
                .count()
                .orderBy(col("count").desc())
                .limit(10);

        cleaned.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("src/main/resources/output/Products");
    }
}
