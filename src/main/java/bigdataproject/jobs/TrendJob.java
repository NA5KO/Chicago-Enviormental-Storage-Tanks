package bigdataproject.jobs;

import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;

import static org.apache.spark.sql.functions.*;

public class TrendJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Trend Analysis")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", true)
                .csv("src/main/resources/file.csv");

        Dataset<Row> installs = df.filter(col("INSTALLATION DATE").isNotNull())
                .withColumn("year", year(to_date(col("INSTALLATION DATE"), "MM/dd/yyyy")));

        Dataset<Row> yearly = installs.groupBy("year").count().orderBy("year");

        Dataset<Row> trend = yearly.withColumn("avg5",
                avg("count").over(Window.orderBy("year").rowsBetween(-2, 2)));

        trend.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("src/main/resources/output/suburb");
    }
}
