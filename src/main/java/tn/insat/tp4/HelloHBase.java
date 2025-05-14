package tn.insat.tp4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelloHBase {
    public static void main(String[] args) {
        // Load CSV from resources
        InputStream is = HelloHBase.class.getClassLoader().getResourceAsStream("Chicago_2.csv");
        if (is == null) {
            System.err.println("Fichier Chicago_2.csv introuvable dans les resources !");
            return;
        }

        // Load HBase configuration
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "localhost"); // Change if needed
        config.set("hbase.zookeeper.property.clientPort", "2181");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));
             Connection connection = ConnectionFactory.createConnection(config);
             Table table = connection.getTable(TableName.valueOf("tanks"))) {

            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false; // skip CSV header
                    continue;
                }
                String[] fields = line.split(",", -1); // -1 keeps empty strings

                // Extract relevant fields (example indexes — adjust as needed)
                String facilityId = fields[7].trim();
                String tankId = fields[10].trim();

                String owner = fields[8].trim();
                String facilityName = fields[9].trim();
                String tankType = fields[6].trim();
                String Material = fields[11].trim();
                String Construction = fields[12].trim();

                String address = fields[0].trim();
                String Suburb = fields[38].trim();

                String product = fields[13].trim();
                String capacity = fields[14].trim();

                String installDate = fields[15].trim();
                String removalDate = fields[16].trim();

                // Compose row key
                String rowKey = facilityId + "#" + tankId;

                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("owner"), Bytes.toBytes(owner));
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("tank_type"), Bytes.toBytes(tankType));
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("material"), Bytes.toBytes(Material));
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("construction"), Bytes.toBytes(Construction));
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("facility_name"), Bytes.toBytes(facilityName));

                put.addColumn(Bytes.toBytes("location"), Bytes.toBytes("address"), Bytes.toBytes(address));
                put.addColumn(Bytes.toBytes("location"), Bytes.toBytes("suburb"), Bytes.toBytes(Suburb));

                put.addColumn(Bytes.toBytes("status"), Bytes.toBytes("installation_date"), Bytes.toBytes(installDate));
                put.addColumn(Bytes.toBytes("status"), Bytes.toBytes("removal_date"), Bytes.toBytes(removalDate));

                put.addColumn(Bytes.toBytes("product"), Bytes.toBytes("tank_product"), Bytes.toBytes(product));
                put.addColumn(Bytes.toBytes("product"), Bytes.toBytes("capacity"), Bytes.toBytes(capacity));

                table.put(put);
            }

            System.out.println("Data inserted successfully into HBase.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}