package dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import beans.FacilityActivity;
import beans.Gender;
import beans.Role;

public class FacilityActivityDao {
private static HashMap<Integer, FacilityActivity> facilityActivitys;
	
	public FacilityActivityDao() {
		loadFacilityActivitys();
	}
	
	private void loadFacilityActivitys() {
		//neki json load
		facilityActivitys = new HashMap<Integer, FacilityActivity>();
		facilityActivitys.put(1, new FacilityActivity(1, false, "grupni treninzi"));
		facilityActivitys.put(2, new FacilityActivity(2, false, "pojedinacni treninzi"));
	}
	
	//const sa context path?
	
	
	public Collection<FacilityActivity> getAll() {
		return facilityActivitys.values();
	}
	
	public FacilityActivity getById(int id) {
		return facilityActivitys.containsKey(id) ? facilityActivitys.get(id) : null;
	}
	
	public FacilityActivity addNew(FacilityActivity newFacilityActivity) {
		Integer maxId = -1;
		for (int id : facilityActivitys.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newFacilityActivity.setId(maxId);
		facilityActivitys.put(newFacilityActivity.getId(), newFacilityActivity);
		return newFacilityActivity;
	}
	
	public FacilityActivity update(int id, FacilityActivity updatedFacilityActivity) {
		FacilityActivity facilityActivityToUpdate = this.getById(id);
		
		if(facilityActivityToUpdate == null) {
			return this.addNew(updatedFacilityActivity);
		}
		
		facilityActivityToUpdate.update(updatedFacilityActivity);
		return facilityActivityToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.facilityActivitys.remove(id);
	}
}
