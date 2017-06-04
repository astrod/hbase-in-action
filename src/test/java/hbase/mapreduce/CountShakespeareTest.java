package hbase.mapreduce;

import hbase.Application;
import hbase.table.InitTable;
import hbase.twit.LoadTwits;
import hbase.user.LoadUsers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * CountShakespeareTest
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CountShakespeareTest {

	@Before
	public void before() throws Exception {
		initTable.initTable(false);
		loadUsers.loadUsers(100);
		loadTwits.loadTwits(100);
	}

	@Test
	public void count() throws Exception {
		countShakespeare.count();
	}

	@Autowired
	private CountShakespeare countShakespeare;
	@Autowired
	private LoadUsers loadUsers;
	@Autowired
	private LoadTwits loadTwits;
	@Autowired
	private InitTable initTable;

}