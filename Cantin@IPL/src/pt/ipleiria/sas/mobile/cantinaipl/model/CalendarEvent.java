package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.io.Serializable;

public class CalendarEvent implements Serializable {

	// [REGION] Constants

	private static final long serialVersionUID = 1L;

	// [ENDREGION] Constants

	// [REGION] Fields

	private String title;
	private String description;
	private String refeicaoType;
	private String userType;
	private String location;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public CalendarEvent(String title, String description, String refeicaoType,
			String userType, String location) {
		super();
		this.title = title;
		this.description = description;
		this.refeicaoType = refeicaoType;
		this.userType = userType;
		this.location = location;
	}

	// [ENDREGION] Constructors

	// [REGION] GettersAndSetters_Methods

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefeicaoType() {
		return refeicaoType;
	}

	public void setRefeicaoType(String refeicaoType) {
		this.refeicaoType = refeicaoType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// [ENDREGION] GettersAndSetters_Methods

}
