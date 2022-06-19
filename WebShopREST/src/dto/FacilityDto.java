package dto;

import java.util.ArrayList;
import java.util.List;

import beans.Facility;
import beans.FacilityActivity;
import beans.FacilityType;
import beans.Location;
import dao.*;


public class FacilityDto {
	private Facility facility;
	private Location location;
	private List<FacilityActivity> activitys;
	
	public FacilityDto() {
		
	}

	
	public FacilityDto(Facility facility, Location location, List<FacilityActivity> activitys) {
		super();
		this.facility = facility;
		this.location = location;
		this.activitys = activitys;
	}
	
	public Facility getFacility() {
		return facility;
	}


	public void setFacility(Facility facility) {
		this.facility = facility;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}

	public List<FacilityActivity> getActivitys() {
		return activitys;
	}

	public void setActivitys(List<FacilityActivity> activitys) {
		this.activitys = activitys;
	}


	public FacilityDto(Facility facility, String contextPath) {
		super();
		this.facility = facility;
		
		LocationDao locationDao = new LocationDao(contextPath);
		this.location = locationDao.getById(facility.getLocationId());
		
		FacilityActivityDao facilityActivityDao = new FacilityActivityDao(contextPath);
		this.activitys = new ArrayList<FacilityActivity>();
		for(int id : facility.getFacilityActivityIds()) {
			activitys.add(facilityActivityDao.getById(id));
		}
		
	}
}
