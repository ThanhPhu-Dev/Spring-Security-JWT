package securityjwt.User;

public interface INewAccountService {
	Boolean existsByUsername(String username);
	User save(String username, String password);
}
