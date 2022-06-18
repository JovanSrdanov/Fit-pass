package dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import beans.Location;
import beans.Gender;
import beans.Location;
import beans.Role;

public class LocationDao {
	private static HashMap<Integer, Location> locations;
	
	public LocationDao() {
		loadLocations();
	}
	
	private void loadLocations() {
		//neki json load
		locations = new HashMap<Integer, Location>();
		locations.put(1, new Location(1, false, "100", "100", "Adresa gas"));
	}
	
	public Collection<Location> getAll() {
		return locations.values();
	}
	
	public Location getById(int id) {
		return locations.containsKey(id) ? locations.get(id) : null;
	}
	
	public Location addNew(Location newLocation) {
		Integer maxId = -1;
		for (int id : locations.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newLocation.setId(maxId);
		locations.put(newLocation.getId(), newLocation);
		return newLocation;
	}
	
	public Location update(int id, Location updatedLocation) {
		Location locationToUpdate = this.getById(id);
		
		if(locationToUpdate == null) {
			return this.addNew(updatedLocation);
		}
		
		locationToUpdate.update(updatedLocation);
		return locationToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.locations.remove(id);
	}
}
