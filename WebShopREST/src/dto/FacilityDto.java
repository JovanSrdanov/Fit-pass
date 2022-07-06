package dto;

import java.util.ArrayList;
import java.util.List;

import beans.Facility;
import beans.FacilityActivity;
import beans.FacilityType;
import beans.Location;
import beans.Manager;
import dao.*;


public class FacilityDto {
	private Facility facility;
	private Location location;
	private List<FacilityActivity> activitys;
	private Manager manager;
	
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

	public Manager getManager() {
		return manager;
	}


	public void setManager(Manager manager) {
		this.manager = manager;
	}


	public FacilityDto(Facility facility, Manager manager) {
		super();
		this.facility = facility;
		this.manager = manager;
		
		LocationDao locationDao = new LocationDao();
		this.location = locationDao.getById(facility.getLocationId());
		
		FacilityActivityDao facilityActivityDao = new FacilityActivityDao();
		this.activitys = new ArrayList<FacilityActivity>();
		for(int id : facility.getFacilityActivityIds()) {
			activitys.add(facilityActivityDao.getById(id));
		}
		
	}
}
