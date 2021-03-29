package securityjwt.User;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

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

}
