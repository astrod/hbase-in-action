package hbase.user;

/**
 * AbstractUser
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
public abstract class AbstractUser {
	public String user;
	public String name;
	public String email;
	public String password;
	public long tweetCount;

	@Override
	public String toString() {
		return String.format("<AbstractUser: %s, %s, %s>",user, name, email);
	}

}
