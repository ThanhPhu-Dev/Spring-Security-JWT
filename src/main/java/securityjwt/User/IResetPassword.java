package securityjwt.User;

import securityjwt.CustommerNotFoundException;

public interface IResetPassword {
	void updateResetPasswordToken(String token,String email) throws CustommerNotFoundException;
	User getByResetPasswordToken(String resetpasswordToken);
	void updatePassword(User user, String newpassword);
}
