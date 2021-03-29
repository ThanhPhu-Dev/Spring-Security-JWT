package securityjwt.payload;

//có thể thay thế cho loginRespones
public class JwtReponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String userName;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JwtReponse(String token, String type, Long id, String userName) {
		super();
		this.token = token;
		this.type = type;
		this.id = id;
		this.userName = userName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
