package beans;

import java.util.ArrayList;

public class FacilityType {
	private String id;
	private boolean isDeleted;
	private String type;
	private ArrayList<Integer> activityIds;
	public FacilityType(String id, boolean isDeleted, String type, ArrayList<Integer> activityIds) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.type = type;
		this.activityIds = activityIds;
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
	public ArrayList<Integer> getActivityIds() {
		return activityIds;
	}
	public void setActivityIds(ArrayList<Integer> activityIds) {
		this.activityIds = activityIds;
	}
	
}
	
