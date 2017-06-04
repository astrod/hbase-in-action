package hbase.twit;

import org.joda.time.DateTime;

import java.time.ZonedDateTime;

/**
 * AbstractTwit
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
public abstract class AbstractTwit {
	public String user;
	public DateTime dt;
	public String text;

	@Override
	public String toString() {
		return String.format(
			"<AbstractTwit: %s %s %s>",
			user, dt, text);
	}
}
