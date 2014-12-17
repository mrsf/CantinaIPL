package pt.ipleiria.sas.mobile.cantinaipl.model;

public class SharedPreference {

	private String login;
	private String password;
	private boolean isMemorized;

	public SharedPreference(String login, String password, boolean isMemorized) {
		this.login = login;
		this.password = password;
		this.isMemorized = isMemorized;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isMemorized() {
		return isMemorized;
	}

	public void setMemorized(boolean isMemorized) {
		this.isMemorized = isMemorized;
	}

}
