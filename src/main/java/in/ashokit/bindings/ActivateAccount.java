package in.ashokit.bindings;

import lombok.Data;

@Data
public class ActivateAccount {

	private String emailId;
	private String tempPass;
	private String newPass;
	private String confirmPass;
}
