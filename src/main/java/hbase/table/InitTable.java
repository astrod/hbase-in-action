package hbase.table;

import hbase.twit.TwitsDAO;
import hbase.user.UsersDAO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * InitTable
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@Service
public class InitTable {
	public void initTable(boolean drop) throws Exception {
		Admin admin = conn.getAdmin();

		// first do no harm
		if (drop) {
			System.out.println("!!! dropping tables in...");
			for (int i = 5; i > 0; i--) {
				System.out.println(i);
				Thread.sleep(1000);
			}

			if (admin.tableExists(TableName.valueOf(UsersDAO.TABLE_NAME))) {
				System.out.printf("Deleting %s\n", Bytes.toString(UsersDAO.TABLE_NAME));
				if (admin.isTableEnabled(TableName.valueOf(UsersDAO.TABLE_NAME)))
					admin.disableTable(TableName.valueOf(UsersDAO.TABLE_NAME));
				admin.deleteTable(TableName.valueOf(UsersDAO.TABLE_NAME));
			}

			if (admin.tableExists(TableName.valueOf(TwitsDAO.TABLE_NAME))) {
				System.out.printf("Deleting %s\n", Bytes.toString(TwitsDAO.TABLE_NAME));
				if (admin.isTableEnabled(TableName.valueOf(TwitsDAO.TABLE_NAME)))
					admin.disableTable(TableName.valueOf(TwitsDAO.TABLE_NAME));
				admin.deleteTable(TableName.valueOf(TwitsDAO.TABLE_NAME));
			}
		}

		if (admin.tableExists(TableName.valueOf(UsersDAO.TABLE_NAME))) {
			System.out.println("AbstractUser table already exists.");
		} else {
			System.out.println("Creating AbstractUser table...");
			HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(UsersDAO.TABLE_NAME));
			HColumnDescriptor c = new HColumnDescriptor(UsersDAO.INFO_FAM);
			desc.addFamily(c);
			admin.createTable(desc);
			System.out.println("AbstractUser table created.");
		}

		if (admin.tableExists(TableName.valueOf(TwitsDAO.TABLE_NAME))) {
			System.out.println("Twits table already exists.");
		} else {
			System.out.println("Creating Twits table...");
			HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(TwitsDAO.TABLE_NAME));
			HColumnDescriptor c = new HColumnDescriptor(TwitsDAO.TWITS_FAM);
			c.setMaxVersions(1);
			desc.addFamily(c);
			admin.createTable(desc);
			System.out.println("Twits table created.");
		}
	}

	@Autowired
	private Connection conn;

}
