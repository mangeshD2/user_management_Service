package in.ashokit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;
import in.ashokit.service.UserMgmtService;

@RestController
public class UserRestController {

	@Autowired
	private UserMgmtService userMgmtService;

	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user) {

		boolean saveUser = userMgmtService.saveUser(user);
		if (saveUser) {
			return new ResponseEntity<>("User Registration Success", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("User Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount account) {

		boolean isActivated = userMgmtService.activateAccount(account);

		if (isActivated) {
			return new ResponseEntity<>("Account Activated", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getUsers")
	public ResponseEntity<List<User>> getAllUsers() {

		List<User> allUsers = userMgmtService.getAllUsers();

		return new ResponseEntity<>(allUsers, HttpStatus.OK);

	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {

		User userById = userMgmtService.getUserById(userId);
		return new ResponseEntity<>(userById, HttpStatus.OK);
	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {

		boolean deleteUserById = userMgmtService.deleteUserById(userId);
		if (deleteUserById) {
			return new ResponseEntity<>("User Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to delete", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String status) {

		boolean isChanged = userMgmtService.changeAccountStatus(userId, status);
		if (isChanged) {
			return new ResponseEntity<>("Status Changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to change Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {

		String loginStatus = userMgmtService.login(login);
		return new ResponseEntity<>(loginStatus, HttpStatus.OK);
	}

	@GetMapping("/forgot/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email) {

		String status = userMgmtService.forgotPass(email);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
}
