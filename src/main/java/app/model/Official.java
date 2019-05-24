package app.model;

/**
 * Represents an elected official.
 * 
 * @author matt
 */
public class Official {
	
	private String name;
	
	private String officeName;
	
	public Official(String name, String officeName) {
		this.name = name;
		this.officeName = officeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
}
