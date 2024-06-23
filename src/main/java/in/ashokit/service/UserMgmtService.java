package in.ashokit.service;

import java.util.List;

import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;

public interface UserMgmtService {

	public boolean saveUser(User user);

	public boolean activateAccount(ActivateAccount activateAccount);

	public User getUserById(Integer userId);

	public List<User> getAllUsers();

	public boolean deleteUserById(Integer userId);

	public boolean changeAccountStatus(Integer userId, String status);

	public String login(Login login);

	public String forgotPass(String emailId);

}
