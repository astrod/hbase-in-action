package hbase.user;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * UsersDAO
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
@Service
public class UsersDAO {
	public static final byte[] TABLE_NAME = Bytes.toBytes("users");
	public static final byte[] INFO_FAM = Bytes.toBytes("info");
	public static final byte[] USER_COL = Bytes.toBytes("user");
	public static final byte[] NAME_COL = Bytes.toBytes("name");
	public static final byte[] EMAIL_COL = Bytes.toBytes("email");
	public static final byte[] PASS_COL = Bytes.toBytes("password");
	public static final byte[] TWEETS_COL = Bytes.toBytes("tweetcount");

	private static Get mkGet(String user) {
		Get g = new Get(Bytes.toBytes(user));
		g.addFamily(INFO_FAM);
		return g;
	}

	private static Put mkPut(User u) {
		Put p = new Put(Bytes.toBytes(u.user));
		p.addColumn(INFO_FAM, USER_COL, Bytes.toBytes(u.user));
		p.addColumn(INFO_FAM, NAME_COL, Bytes.toBytes(u.name));
		p.addColumn(INFO_FAM, EMAIL_COL, Bytes.toBytes(u.email));
		p.addColumn(INFO_FAM, PASS_COL, Bytes.toBytes(u.password));
		return p;
	}

	private static Delete mkDel(String user) {
		Delete d = new Delete(Bytes.toBytes(user));
		return d;
	}

	public void addUser(String user, String name, String email, String password) throws IOException {
		TableName tableName = TableName.valueOf(TABLE_NAME);
		// use table as needed, the table returned is lightweight
		Table table = connection.getTable(tableName);
		Put p = mkPut(new User(user, name, email, password));
		table.put(p);
		table.close();
	}

	public User getUser(String user) throws IOException {
		TableName tableName = TableName.valueOf(TABLE_NAME);
		Table table = connection.getTable(tableName);

		Get g = mkGet(user);
		Result result = table.get(g);

		if(result.isEmpty()) {
			return null;
		}

		User u =  new User(result);
		table.close();
		return u;
	}

	public void deleteUser(String user) throws IOException {
		TableName tableName = TableName.valueOf(TABLE_NAME);
		Table table = connection.getTable(tableName);

		Delete d = mkDel(user);
		table.delete(d);

		table.close();
	}

	public String getTableName() throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		return table.getName().getNameAsString();
	}

	private static class User extends hbase.user.User {
		public Long tweetCount;

		private User(Result r) {
			this(r.getValue(INFO_FAM, USER_COL), r.getValue(INFO_FAM, NAME_COL), r.getValue(INFO_FAM, EMAIL_COL), r.getValue(INFO_FAM, PASS_COL), r.getValue(INFO_FAM, TWEETS_COL) == null ? Bytes.toBytes(0L) : r.getValue(INFO_FAM, TWEETS_COL));
		}

		private User(byte[] user, byte[] name, byte[] email, byte[] password, byte[] tweetCount) {
			this(Bytes.toString(user), Bytes.toString(name), Bytes.toString(email), Bytes.toString(password));
			this.tweetCount = Bytes.toLong(tweetCount);
		}

		private User(String user, String name, String email, String password) {
			this.user = user;
			this.name = name;
			this.email = email;
			this.password = password;
		}
	}



	@Autowired
	private Connection connection;
}
