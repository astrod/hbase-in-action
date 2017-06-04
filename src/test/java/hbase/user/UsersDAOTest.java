package hbase.user;

import hbase.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * UsersDAOTest
 *
 * @author jongUn
 * @since 2017. 05. 31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class UsersDAOTest {
	@Before
	public void setUp() throws Exception {

	}


	@Test
	public void testPutRowKey() {

	}

	@Autowired
	private UsersDAO usersDAO;

}