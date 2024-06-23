package in.ashokit.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String fullName;
	private String emailId;
	private Long mobileNo;
	private String gender;
	private LocalDate dob;
	private String ssn;

}
