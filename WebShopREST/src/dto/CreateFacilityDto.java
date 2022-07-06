package dto;

import java.util.List;

public class CreateFacilityDto {
	private String name;
	private String facilityType;
	private String workStart;
	private String workEnd;
	
	private String latitude;
	private String longitude;
	private String street;
	private String streetNumber;
	private String city;
	private String postCode;
	
	private int managerId;

	public CreateFacilityDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreateFacilityDto(String name, String facilityType, String workStart, String workEnd, String latitude,
			String longitude, String street, String streetNumber, String city, String postCode, int managerId) {
		super();
		this.name = name;
		this.facilityType = facilityType;
		this.workStart = workStart;
		this.workEnd = workEnd;
		this.latitude = latitude;
		this.longitude = longitude;
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postCode = postCode;
		this.managerId = managerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	
}
