package hbase.user;

import hbase.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * LoadUsersTest
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class LoadUsersTest {
	@Test
	public void loadUsers() throws Exception {
		loadUsers.loadUsers(10);
	}


	@Autowired
	private LoadUsers loadUsers;
}