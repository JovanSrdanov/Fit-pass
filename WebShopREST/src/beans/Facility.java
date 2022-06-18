package beans;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Facility {
	private int id;
	private boolean isDeleted;
	private String name;
	private int facilityTypeId;
	private List<Integer> facilityActivityIds;
	private boolean status;
	private int locationId;
	private String logo;
	private int rating;
	private LocalTime workStart;
	private LocalTime workEnd;
	
	public Facility(int id, boolean isDeleted, String name, int facilityTypeId, List<Integer> facilityActivityIds,
			boolean status, int locationId, String logo, int rating, LocalTime workStart, LocalTime workEnd) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.name = name;
		this.facilityTypeId = facilityTypeId;
		this.facilityActivityIds = facilityActivityIds;
		this.status = status;
		this.locationId = locationId;
		this.logo = logo;
		this.rating = rating;
		this.workStart = workStart;
		this.workEnd = workEnd;
	}
	public Facility() {
		super();
	}
	
	public int getFacilityTypeId() {
		return facilityTypeId;
	}
	public void setFacilityTypeId(int facilityTypeId) {
		this.facilityTypeId = facilityTypeId;
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
	public LocalTime getWorkStart() {
		return workStart;
	}
	public void setWorkStart(LocalTime workStart) {
		this.workStart = workStart;
	}
	public LocalTime getWorkEnd() {
		return workEnd;
	}
	public void setWorkEnd(LocalTime workEnd) {
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
		this.facilityTypeId = updatedFacility.facilityTypeId;
		this.facilityActivityIds = updatedFacility.facilityActivityIds;
		this.status = updatedFacility.status;
		this.locationId = updatedFacility.locationId;
		this.logo = updatedFacility.logo;
		this.rating = updatedFacility.rating;
		this.workStart = updatedFacility.workStart;
		this.workEnd = updatedFacility.workEnd;
		
	}
	
	
}
