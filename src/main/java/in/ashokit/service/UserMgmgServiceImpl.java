package in.ashokit.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;
import in.ashokit.entity.UserMaster;
import in.ashokit.repo.UserMasterRepo;
import in.ashokit.utils.EmailUtils;

@Service
public class UserMgmgServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {

		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);
		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("In-Active");

		UserMaster save = userMasterRepo.save(entity);

		String subject = "Your Registration Success";
		String fileName = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);

		emailUtils.sendEmail(user.getEmailId(), subject, body);

		return save.getUserId() != null;
	}

	@Override
	public boolean activateAccount(ActivateAccount activateAccount) {

		UserMaster entity = new UserMaster();
		entity.setEmailId(activateAccount.getEmailId());
		entity.setPassword(activateAccount.getTempPass());

		// select * from user_master where email=? and pwd=?
		Example<UserMaster> of = Example.of(entity);

		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAccount.getNewPass());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}

	}

	@Override
	public User getUserById(Integer userId) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);

		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}

		return null;
	}

	@Override
	public List<User> getAllUsers() {

		List<UserMaster> findAll = userMasterRepo.findAll();
		List<User> users = new ArrayList<User>();

		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}

		return users;
	}

	@Override
	public boolean deleteUserById(Integer userId) {

		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String status) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);

		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(status);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {

		UserMaster entity = new UserMaster();
		entity.setEmailId(login.getEmailId());
		entity.setPassword(login.getPassword());

		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return "Invalid Credentials.";
		} else {
			UserMaster userMaster = findAll.get(0);
			if (userMaster.getAccStatus().equals("Active")) {
				return "Account Activated";
			} else {
				return "Account not activated";
			}
		}

	}

	@Override
	public String forgotPass(String emailId) {

		UserMaster entity = userMasterRepo.findByEmailId(emailId);
		if (entity == null) {
			return "Invalid Email";
		}

		String subject = "forgot Password";
		String fileName = "RECOVER-PWD-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		boolean sendEmail = emailUtils.sendEmail(emailId, subject, body);

		if (sendEmail) {
			return "password sent to your email";
		}
		return null;
	}

	private String generateRandomPwd() {

		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		int length = 6;

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(alphaNumeric.length());
			char randomChar = alphaNumeric.charAt(index);
			sb.append(randomChar);
		}
		return sb.toString();
	}

	private String readEmailBody(String fullName, String pwd, String fileName) {

		String url = "";
		String mailBody = null;

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer buffer = new StringBuffer();
			String line = br.readLine();

			while (line != null) {
				buffer.append(line);
				line = br.readLine();
			}

			br.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("PWD", pwd);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}

}
