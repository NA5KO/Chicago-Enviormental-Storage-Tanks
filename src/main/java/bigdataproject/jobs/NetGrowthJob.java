package bigdataproject.jobs;

import org.apache.spark.sql.*;
import static org.apache.spark.sql.functions.*;

public class NetGrowthJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Install vs Removal")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", true)
                .csv("src/main/resources/file.csv");

        Dataset<Row> installs = df.filter(col("INSTALLATION DATE").isNotNull())
                .withColumn("year", year(to_date(col("INSTALLATION DATE"), "MM/dd/yyyy")))
                .groupBy("year").count().withColumnRenamed("count", "installs");

        Dataset<Row> removals = df.filter(col("REMOVAL DATE").isNotNull())
                .withColumn("year", year(to_date(col("REMOVAL DATE"), "MM/dd/yyyy")))
                .groupBy("year").count().withColumnRenamed("count", "removals");

        Dataset<Row> netGrowth = installs.join(removals, "year").orderBy("year");

        netGrowth.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("src/main/resources/output/removals");
    }
}
