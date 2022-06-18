package beans;

public class FacilityType {
	private String id;
	private boolean isDeleted;
	private String type;
	
	public FacilityType(String id, boolean isDeleted, String type) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.type = type;
	}
	public FacilityType() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
	
