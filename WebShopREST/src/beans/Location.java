package beans;

public class Location {
	
	private int id;
	private boolean isDeleted;
	private String latitude;
	private String longitude;
	private String street;
	private String streetNumber;
	private String city;
	private String postCode;
	
	public Location() {
		
	}

	public Location(int id, boolean isDeleted, String latitude, String longitude, String street, String streetNumber,
			String city, String postCode) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.latitude = latitude;
		this.longitude = longitude;
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postCode = postCode;
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

	public void update(Location updatedLocation) {
		this.id = updatedLocation.id;
		this.isDeleted = updatedLocation.isDeleted;
		this.latitude = updatedLocation.latitude;
		this.longitude = updatedLocation.longitude;
		this.street = updatedLocation.street;
		this.streetNumber = updatedLocation.streetNumber;
		this.city = updatedLocation.city;
		this.postCode = updatedLocation.postCode;
	}
	
	
}
