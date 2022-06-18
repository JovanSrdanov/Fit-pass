package beans;

public class Location {
	
	private int id;
	private boolean isDeleted;
	private String latitude;
	private String longitude;
	private String address;
	
	public Location() {
		
	}

	public Location(int id, boolean isDeleted, String latitude, String longitude, String address) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void update(Location updatedLocation) {
		this.id = updatedLocation.id;
		this.isDeleted = updatedLocation.isDeleted;
		this.latitude = updatedLocation.latitude;
		this.longitude = updatedLocation.longitude;
		this.address = updatedLocation.address;
	}
	
	
}
