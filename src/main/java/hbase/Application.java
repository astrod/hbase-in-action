package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * hbase.Application
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Connection connectAdmin() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "127.0.0.1");
		conf.set("zookeeper.znode.parent", "/hbase-unsecure");
		Connection conn = ConnectionFactory.createConnection(conf);
		return conn;
	}
}
