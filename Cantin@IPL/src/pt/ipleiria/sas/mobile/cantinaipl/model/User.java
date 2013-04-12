package pt.ipleiria.sas.mobile.cantinaipl.model;

public class User {

	// [REGION] Fields

	private int user_id;
	private String username;
	private String password;
	private String group;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(int user_id, String username, String password, String group) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.group = group;
	}

	// [ENDREGION] Constructors

	// [REGION] Properties

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	// [ENDREGION] Properties

}
