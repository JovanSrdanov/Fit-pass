package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Location;
import beans.Customer;
import beans.Gender;
import beans.Location;
import beans.Role;

public class LocationDao {
	private static HashMap<Integer, Location> locations;
	
	private String path;
	
	public LocationDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		//loadLocations();
		//writeFile();
		readFile();
	}
	
	public LocationDao() {
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
		writeFile();
		return newLocation;
	}
	
	public Location update(int id, Location updatedLocation) {
		Location locationToUpdate = this.getById(id);
		
		if(locationToUpdate == null) {
			return this.addNew(updatedLocation);
		}
		
		locationToUpdate.update(updatedLocation);
		writeFile();
		return locationToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.locations.remove(id);
		writeFile();
	}
	
	private void writeFile() {
		File theFile = new File(path + "WebProjekat/Data/Locations.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(locations, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(path + "WebProjekat/Data/Locations.json");
		System.out.println(theFile.getAbsolutePath());
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Location>>(){}.getType();
			locations = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
}
