package hbase.user;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UsersDAO
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
@Service
public class UsersDAO {
	public static final byte[] TABLE_NAME = Bytes.toBytes("users");
	public static final byte[] INFO_FAM   = Bytes.toBytes("info");

	public static final byte[] USER_COL   = Bytes.toBytes("user");
	public static final byte[] NAME_COL   = Bytes.toBytes("name");
	public static final byte[] EMAIL_COL  = Bytes.toBytes("email");
	public static final byte[] PASS_COL   = Bytes.toBytes("password");
	public static final byte[] TWEETS_COL = Bytes.toBytes("tweet_count");

	public static final byte[] HAMLET_COL  = Bytes.toBytes("hamlet_tag");

	private static final Logger log = Logger.getLogger(UsersDAO.class);

	private static Get mkGet(String user) throws IOException {
		log.debug(String.format("Creating Get for %s", user));

		Get g = new Get(Bytes.toBytes(user));
		g.addFamily(INFO_FAM);
		return g;
	}

	private static Put mkPut(User u) {
		log.debug(String.format("Creating Put for %s", u));

		Put p = new Put(Bytes.toBytes(u.user));
		p.add(INFO_FAM, USER_COL, Bytes.toBytes(u.user));
		p.add(INFO_FAM, NAME_COL, Bytes.toBytes(u.name));
		p.add(INFO_FAM, EMAIL_COL, Bytes.toBytes(u.email));
		p.add(INFO_FAM, PASS_COL, Bytes.toBytes(u.password));
		return p;
	}

	public static Put mkPut(String username,
		byte[] fam,
		byte[] qual,
		byte[] val) {
		Put p = new Put(Bytes.toBytes(username));
		p.add(fam, qual, val);
		return p;
	}

	private static Delete mkDel(String user) {
		log.debug(String.format("Creating Delete for %s", user));

		Delete d = new Delete(Bytes.toBytes(user));
		return d;
	}

	private static Scan mkScan() {
		Scan s = new Scan();
		s.addFamily(INFO_FAM);
		return s;
	}

	public void addUser(String user,
		String name,
		String email,
		String password)
		throws IOException {

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		Put p = mkPut(new User(user, name, email, password));
		table.put(p);

		table.close();
	}

	public User getUser(String user)
		throws IOException {

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		Get g = mkGet(user);
		Result result = table.get(g);
		if (result.isEmpty()) {
			log.info(String.format("user %s not found.", user));
			return null;
		}

		User u = new User(result);
		table.close();
		return u;
	}

	public void deleteUser(String user) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		Delete d = mkDel(user);
		table.delete(d);

		table.close();
	}

	public List<User> getUsers()
		throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		ResultScanner results = table.getScanner(mkScan());
		ArrayList<User> ret
			= new ArrayList<>();
		for(Result r : results) {
			ret.add(new User(r));
		}

		table.close();
		return ret;
	}

	public long incTweetCount(String user) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		long ret = table.incrementColumnValue(Bytes.toBytes(user),
			INFO_FAM,
			TWEETS_COL,
			1L);

		table.close();
		return ret;
	}

	private static class User
		extends AbstractUser {
		private User(Result r) {
			this(r.getValue(INFO_FAM, USER_COL),
				r.getValue(INFO_FAM, NAME_COL),
				r.getValue(INFO_FAM, EMAIL_COL),
				r.getValue(INFO_FAM, PASS_COL),
				r.getValue(INFO_FAM, TWEETS_COL) == null
					? Bytes.toBytes(0L)
					: r.getValue(INFO_FAM, TWEETS_COL));
		}

		private User(byte[] user,
			byte[] name,
			byte[] email,
			byte[] password,
			byte[] tweetCount) {
			this(Bytes.toString(user),
				Bytes.toString(name),
				Bytes.toString(email),
				Bytes.toString(password));
			this.tweetCount = Bytes.toLong(tweetCount);
		}

		private User(String user,
			String name,
			String email,
			String password) {
			this.user = user;
			this.name = name;
			this.email = email;
			this.password = password;
		}
	}

	@Autowired
	private Connection connection;
}
