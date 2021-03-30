package securityjwt.User;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import securityjwt.CustommerNotFoundException;

@Service
public class UserService implements UserDetailsService, INewAccountService, IResetPassword{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		// Kiểm tra xem user có tồn tại trong database không?
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}

	// JWTAuthenticationFilter sẽ sử dụng hàm này
	//kiểm tra tồn tại user không thông qua id
	@Transactional
	public UserDetails loadUserById(Long id) {

//		User user = userRepository.findById(id)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

		User user = Optional.ofNullable(userRepository.findOne(id))
					.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
		return new CustomUserDetails(user);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public User save(String username, String password) {
		return userRepository.save(new User(username,passwordEncoder.encode(password)));
	}

	@Override
	@Transactional
	public void updateResetPasswordToken(String token, String username) throws CustommerNotFoundException{
		User user = userRepository.findByUsername(username);
		if(user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		}else {
			throw new CustommerNotFoundException("could not find any customer with username"+username);
		}
		
	}

	@Override
	public User getByResetPasswordToken(String resetpasswordToken) {
		return userRepository.findByResetPasswordToken(resetpasswordToken);
	}

	@Override
	public void updatePassword(User user, String newpassword) {
		String encodedPassword = passwordEncoder.encode(newpassword);
		user.setPassword(encodedPassword);
		user.setResetPasswordToken(null);
		userRepository.save(user);
		
	}

	
}
