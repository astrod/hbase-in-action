package hbase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * UserController
 *
 * @author jongUn
 * @since 2017. 05. 28.
 */
@RestController
public class UserController {
	@RequestMapping("/users/get/{user}")
	@ResponseBody
	public String get(@PathVariable String user) throws IOException {
		User u = dao.getUser(user);
		return u.user;
	}

	@RequestMapping("/users/add")
	@ResponseBody
	public String add() throws IOException {
		dao.addUser("user1", "name1", "email1", "password1");
		return "success add";
	}

	@RequestMapping("/users")
	@ResponseBody
	public String table() throws IOException {
		return dao.getTableName() + " is table Name";
	}

	@Autowired
	private UsersDAO dao;
}
