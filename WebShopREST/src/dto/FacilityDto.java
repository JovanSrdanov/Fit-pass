package dto;

import java.util.List;

import beans.Facility;
import beans.FacilityActivity;
import beans.FacilityType;
import beans.Location;

public class FacilityDto {
	private Facility facility;
	private Location location;
	private FacilityType type;
	private List<FacilityActivity> activitys;
	
	public FacilityDto() {
		
	}

	
	public FacilityDto(Facility facility, Location location, FacilityType type, List<FacilityActivity> activitys) {
		super();
		this.facility = facility;
		this.location = location;
		this.type = type;
		this.activitys = activitys;
	}



	public FacilityDto(Facility facility) {
		super();
		this.facility = facility;
		
		
	}
}
