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

import beans.Customer;
import beans.FacilityActivity;
import beans.Gender;
import beans.Role;

public class FacilityActivityDao {
	private static HashMap<Integer, FacilityActivity> facilityActivitys;
	
	private String path;
	
	public FacilityActivityDao() {
		//loadFacilityActivitys();
	}
	
	public FacilityActivityDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
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
		writeFile();
		return newFacilityActivity;
	}
	
	public FacilityActivity update(int id, FacilityActivity updatedFacilityActivity) {
		FacilityActivity facilityActivityToUpdate = this.getById(id);
		
		if(facilityActivityToUpdate == null) {
			return this.addNew(updatedFacilityActivity);
		}
		
		facilityActivityToUpdate.update(updatedFacilityActivity);
		writeFile();
		return facilityActivityToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.facilityActivitys.remove(id);
		writeFile();
	}
	
	private void writeFile() {
		File theFile = new File(path + "WebProjekat/Data/FacilitysActivitys.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(facilityActivitys, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(path + "WebProjekat/Data/FacilitysActivitys.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, FacilityActivity>>(){}.getType();
			facilityActivitys = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
}
