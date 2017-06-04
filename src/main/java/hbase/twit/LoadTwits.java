package hbase.twit;

import hbase.user.AbstractUser;
import hbase.user.UsersDAO;
import hbase.util.LoadUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * LoadTwits
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@Service
public class LoadTwits {

	public static final String usage =
		"loadtwits count\n" +
			"  help - print this message and exit.\n" +
			"  count - add count random twits to all TwitBase users.\n";

	private String randTwit(List<String> words) {
		String twit = "";
		for (int i = 0; i < 12; i++) {
			twit += LoadUtils.randNth(words) + " ";
		}
		return twit;
	}

	private DateTime randDT() {
		int year = 2010 + LoadUtils.randInt(5);
		int month = 1 + LoadUtils.randInt(12);
		int day = 1 + LoadUtils.randInt(28);
		return new DateTime(year, month, day, 0, 0, 0, 0);
	}

	public void loadTwits(int count) throws IOException {
		Admin admin = connection.getAdmin();


		if (!admin.tableExists(TableName.valueOf(UsersDAO.TABLE_NAME)) ||
			!admin.tableExists(TableName.valueOf(TwitsDAO.TABLE_NAME))) {
			System.out.println("Please use the InitTables utility to create " +
				"destination tables first.");
			System.exit(0);
		}

		List<String> words = LoadUtils.readResource(LoadUtils.WORDS_PATH);

		for(AbstractUser u : users.getUsers()) {
			for (int i = 0; i < count; i++) {
				twits.postTwit(u.user, randDT(), randTwit(words));
			}
		}
	}

	@Autowired
	private UsersDAO users;
	@Autowired
	private Connection connection;
	@Autowired
	private TwitsDAO twits;
}
