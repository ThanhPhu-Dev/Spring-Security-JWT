package securityjwt;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import securityjwt.User.CustomUserDetails;
import securityjwt.User.User;
import securityjwt.User.UserRepository;
import securityjwt.User.UserService;
import securityjwt.jwt.JwtTokenProvider;
import securityjwt.payload.LoginRequest;
import securityjwt.payload.LoginResponse;
import securityjwt.payload.RandomStuff;
import securityjwt.payload.SingupRequest;

@RestController
@RequestMapping("/api")
public class LodaRestController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = null;
		String jwt=null;
		try {
			// Xác thực thông tin người dùng Request lên
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (Exception e) {
			System.err.println("Lổi sai pass");
		}
		try {
			// Nếu không xảy ra exception tức là thông tin hợp lệ
			// Set thông tin authentication vào Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Trả về jwt cho người dùng.
			jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		} catch (Exception e) {
			System.err.println("loi conetxt");
		}

		
		return new LoginResponse(jwt);
	}
	
	@PostMapping("/signup")
	public RandomStuff registerUser(@Valid @RequestBody SingupRequest singupRequest) {
		if(userRepository.existsByUsername(singupRequest.getUsername())) {
			return new RandomStuff("Error: Username is Already is use!");
		}
		//create account
		User myuser = new User(singupRequest.getUsername(),
				passwordEncoder.encode(singupRequest.getPassword()));
		userRepository.save(myuser);
		return new RandomStuff("Đăng ký thành công");
	}

	// Api /api/random yêu cầu phải xác thực mới có thể request
	@GetMapping("/random")
	public RandomStuff randomStuff() {
		return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
	}
}
