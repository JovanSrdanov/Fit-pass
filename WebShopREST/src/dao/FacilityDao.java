package dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import beans.Facility;
import dto.FacilityDto;

public class FacilityDao {
	private static HashMap<Integer, Facility> facilitys;
	
	public FacilityDao() {
		loadFacilitys();
	}

	private void loadFacilitys() {
		facilitys = new HashMap<Integer, Facility>();
		List<Integer> facilityActivityIds = new ArrayList<Integer>();
		facilityActivityIds.add(1);
		facilityActivityIds.add(2);
		facilitys.put(1, new Facility(1, false,"Red Gym", "Teretana", facilityActivityIds,
			true, 1, "slika.png", 69, LocalTime.now(), LocalTime.now()));
		
		facilitys.put(2, new Facility(2, false,"Red Gym2", "Teretanka", facilityActivityIds,
				true, 1, "slika.png", 69, LocalTime.now(), LocalTime.now()));
		
	}
	
	public Collection<Facility> getAll() {
		return facilitys.values();
	}
	
	public Collection<FacilityDto> getAllTable() {
		ArrayList<FacilityDto> facilityTables = new ArrayList<FacilityDto>();
		for(Facility facility : facilitys.values()) {
			facilityTables.add(new FacilityDto(facility));
		}
		
		return facilityTables;
		
	}
	
	public Facility getById(int id) {
		return facilitys.containsKey(id) ? facilitys.get(id) : null;
	}
	
	public Facility addNew(Facility newFacility) {
		Integer maxId = -1;
		for (int id : facilitys.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newFacility.setId(maxId);
		facilitys.put(newFacility.getId(), newFacility);
		return newFacility;
	}
	
	public Facility update(int id, Facility updatedFacility) {
		Facility facilityToUpdate = this.getById(id);
		
		if(facilityToUpdate == null) {
			return this.addNew(updatedFacility);
		}
		
		facilityToUpdate.update(updatedFacility);
		return facilityToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.facilitys.remove(id);
	}
}
