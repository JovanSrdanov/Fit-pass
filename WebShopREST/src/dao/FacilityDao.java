package dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import beans.Facility;
import dto.FacilityDto;

public class FacilityDao {
	private static HashMap<Integer, Facility> facilitys;
	
	private String path;
	
	public FacilityDao() {
		//loadFacilitys();
		//readFile();
	}
	
	public FacilityDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}

	private void loadFacilitys() {
		facilitys = new HashMap<Integer, Facility>();
		List<Integer> facilityActivityIds = new ArrayList<Integer>();
		facilityActivityIds.add(1);
		facilityActivityIds.add(2);
		facilitys.put(1, new Facility(1, false,"Red Gym", "Teretana", facilityActivityIds,
			true, 1, "slika.png", 69, "09:00", "21:00"));
		
		facilitys.put(2, new Facility(2, false,"Red Gym2", "Teretanka", facilityActivityIds,
				true, 1, "slika.png", 69,"09:00", "21:00"));
		
	}
	
	private void writeFile() {
		File theFile = new File(path + "WebProjekat/Data/Facilitys.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(facilitys, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		File theFile = new File(path + "WebProjekat/Data/Facilitys.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Facility>>(){}.getType();
			facilitys = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	public Collection<Facility> getAll() {
		readFile();
		return facilitys.values();
	}
	
	public Collection<FacilityDto> getAllTable(String contextPath) {
		readFile();
		ArrayList<FacilityDto> facilityTables = new ArrayList<FacilityDto>();
		for(Facility facility : facilitys.values()) {
			facilityTables.add(new FacilityDto(facility, contextPath));
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
		writeFile();
		return newFacility;
	}
	
	public Facility update(int id, Facility updatedFacility) {
		Facility facilityToUpdate = this.getById(id);
		
		if(facilityToUpdate == null) {
			return this.addNew(updatedFacility);
		}
		
		facilityToUpdate.update(updatedFacility);
		writeFile();
		return facilityToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.facilitys.remove(id);
		writeFile();
	}
}
