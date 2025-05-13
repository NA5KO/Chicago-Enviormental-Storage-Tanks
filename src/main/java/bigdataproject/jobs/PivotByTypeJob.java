package bigdataproject.jobs;

import org.apache.spark.sql.*;
import static org.apache.spark.sql.functions.*;

public class PivotByTypeJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Pivot Aboveground/Underground")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", true)
                .csv("src/main/resources/file.csv");

        Dataset<Row> typed = df.withColumn("year", year(to_date(col("INSTALLATION DATE"), "MM/dd/yyyy")));

        Dataset<Row> pivoted = typed.groupBy("year")
                .pivot("Tank Type")
                .count()
                .na().fill(0);

        pivoted.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("src/main/resources/output/type");
    }
}
