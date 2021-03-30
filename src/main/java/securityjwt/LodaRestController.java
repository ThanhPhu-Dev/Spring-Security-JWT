package securityjwt;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import securityjwt.User.CustomUserDetails;
import securityjwt.User.INewAccountService;
import securityjwt.User.IResetPassword;
import securityjwt.User.User;
import securityjwt.User.UserRepository;
import securityjwt.jwt.JwtTokenProvider;
import securityjwt.payload.LoginRequest;
import securityjwt.payload.LoginResponse;
import securityjwt.payload.RandomStuff;
import securityjwt.payload.SingupRequest;
import securityjwt.utils.SendMailUtil;
import securityjwt.utils.Utility;

@RestController
@RequestMapping("/api")
public class LodaRestController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private INewAccountService newAccountService;
	
	@Autowired
	private IResetPassword resetPassword;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = null;
		String jwt=null;
		try {
			// Xác thực thông tin người dùng Request lên
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (Exception e) {
			return ResponseEntity.status(401).body(e.getMessage());
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

		
		return ResponseEntity.ok(new LoginResponse(jwt));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequest singupRequest) {
		if(newAccountService.existsByUsername(singupRequest.getUsername())) {
			return ResponseEntity.status(401).body(new RandomStuff("Tài Khoản đã tồn tại"));
		}
		//create account
		newAccountService.save(singupRequest.getUsername(), singupRequest.getPassword());
		return ResponseEntity.ok(new RandomStuff("Đăng ký thành công"));
	}
	
	@PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(HttpServletRequest req) {
		String username = req.getParameter("username");
		String token = RandomStringUtils.random(30,true,false);
		
		try {
	        resetPassword.updateResetPasswordToken(token, username);
	        String resetPasswordLink = Utility.getSiteURL(req) + "/reset_password?token=" + token;
	        SendMailUtil.sendEmail(username, resetPasswordLink);
	    } catch (CustommerNotFoundException ex) {
	        ex.getMessage();
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        e.getMessage();
	    }
		return ResponseEntity.ok(token);
    }
		
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     
	    User customer = resetPassword.getByResetPasswordToken(token);
	     
	    if (customer == null) {
	        return "message";
	    } else {           
	        resetPassword.updatePassword(customer, password);
	    }
	    return "message";
	}

	// Api /api/random yêu cầu phải xác thực mới có thể request
	@GetMapping("/random")
	public RandomStuff randomStuff() {
		return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
	}
}
