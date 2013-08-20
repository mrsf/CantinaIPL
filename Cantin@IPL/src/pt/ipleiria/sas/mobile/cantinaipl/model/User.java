package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.io.Serializable;

/**
 * Model class to store a user.
 * 
 * This class, allows user data storing. Accessing this class,
 * your data can be used for other classes. It is a serialized class.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class User implements Serializable {

	// [REGION] Constants
	
	private static final long serialVersionUID = 1L;
	
	// [ENDREGION] Constants
	
	// [REGION] Fields

	private String login;
	private String bi;
	private String name;
	private String course;
	private boolean regime;
	private String photo;
	private int nif;
	private String email;
	private boolean type;
	private boolean active;
	private String school;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public User(String login, String bi, String name, String course,
			boolean regime, String photo, int nif, String email, boolean type,
			boolean active, String school) {
		this.login = login;
		this.bi = bi;
		this.name = name;
		this.course = course;
		this.regime = regime;
		this.photo = photo;
		this.nif = nif;
		this.email = email;
		this.type = type;
		this.active = active;
		this.school = school;
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getBi() {
		return bi;
	}

	public void setBi(String bi) {
		this.bi = bi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public boolean isRegime() {
		return regime;
	}

	public void setRegime(boolean regime) {
		this.regime = regime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getNif() {
		return nif;
	}

	public void setNif(int nif) {
		this.nif = nif;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	// [ENDREGION] GetAndSet_Methods

}
