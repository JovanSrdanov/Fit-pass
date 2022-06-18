package beans;

public class FacilityActivity {
	private int id;
	private boolean isDeleted;
	private String name;
	public FacilityActivity(int id, boolean isDeleted, String name) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.name = name;
	}
	public FacilityActivity() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void update(FacilityActivity updatedFacilityActivity) {
		this.id = updatedFacilityActivity.id;
		this.isDeleted = updatedFacilityActivity.isDeleted;
		this.name = updatedFacilityActivity.name;
		
	}
	
	
}
