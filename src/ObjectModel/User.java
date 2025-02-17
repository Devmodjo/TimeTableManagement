package ObjectModel;

public class User {
	
	private Integer idUser;
	private String username;
	private String userStatus;
	private String password;
	
	public User(Integer idUser, String username,String userStatus, String password) {
		this.idUser = idUser;
		this.username = username;
		this.setUserStatus(userStatus);
		this.password = password;
		
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}
