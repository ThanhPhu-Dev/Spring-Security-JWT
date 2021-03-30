package securityjwt.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity 
@Table(name = "user")
@Data // lombok
public class User {

	@Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    @Column(name = "resetpasswordtoken", columnDefinition = "VARCHAR(45)")
    private String resetPasswordToken;
    
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public User() {
		super();
	}
    
	
    
}
