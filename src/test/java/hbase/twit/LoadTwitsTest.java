package hbase.twit;

import hbase.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * LoadTwitsTest
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class LoadTwitsTest {
	@Test
	public void loadTwits() throws Exception {
		loadTwits.loadTwits(100);
	}

	@Autowired
	private LoadTwits loadTwits;
}