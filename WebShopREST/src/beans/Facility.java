package beans;

import java.util.ArrayList;
import java.util.List;

public class Facility {
	private int id;
	private boolean isDeleted;
	private String name;
	private String facilityType;
	private List<Integer> facilityActivityIds;
	private boolean status;
	private int locationId;
	private String logo;
	private int rating;
	private String workStart;
	private String workEnd;
	
	public Facility(int id, boolean isDeleted, String name, String facilityType, List<Integer> facilityActivityIds,
			boolean status, int locationId, String logo, int rating, String workStart, String workEnd) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.name = name;
		this.facilityType = facilityType;
		this.facilityActivityIds = facilityActivityIds;
		this.status = status;
		this.locationId = locationId;
		this.logo = logo;
		this.rating = rating;
		this.workStart = workStart;
		this.workEnd = workEnd;
	}
	
	public Facility(String name, String facilityType, String workStart, String workEnd) {
		super();
		this.isDeleted = false;
		this.name = name;
		this.facilityType = facilityType;
		this.workStart = workStart;
		this.workEnd = workEnd;
		this.facilityActivityIds = new ArrayList<Integer>();
	}

	public Facility() {
		super();
	}
	
	public String getFacilityType() {
		return facilityType;
	}
	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}
	public List<Integer> getFacilityActivityIds() {
		return facilityActivityIds;
	}
	public void setFacilityActivityIds(List<Integer> facilityActivityIds) {
		this.facilityActivityIds = facilityActivityIds;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getWorkStart() {
		return workStart;
	}
	public void setWorkStart(String workStart) {
		this.workStart = workStart;
	}
	public String getWorkEnd() {
		return workEnd;
	}
	public void setWorkEnd(String workEnd) {
		this.workEnd = workEnd;
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
	public void update(Facility updatedFacility) {
		this.id = updatedFacility.id;
		this.isDeleted = updatedFacility.isDeleted;
		this.name = updatedFacility.name;
		this.facilityType = updatedFacility.facilityType;
		this.facilityActivityIds = updatedFacility.facilityActivityIds;
		this.status = updatedFacility.status;
		this.locationId = updatedFacility.locationId;
		this.logo = updatedFacility.logo;
		this.rating = updatedFacility.rating;
		this.workStart = updatedFacility.workStart;
		this.workEnd = updatedFacility.workEnd;
		
	}
	
	
}
