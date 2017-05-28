package hbase.user;

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;

/**
 * User
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
public abstract class User {
	public String user;
	public String name;
	public String email;
	public String password;

	@Override
	public String toString() {
		return String.format("<User: %s, %s, %s>",user, name, email);
	}

}
