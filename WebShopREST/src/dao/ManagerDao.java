package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Manager;
import main.Startup;

public class ManagerDao {
private static HashMap<Integer, Manager> managers;
	
	public ManagerDao() {
		readFile();
	}
	
	public void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Managers.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(managers, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Managers.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Manager>>(){}.getType();
			managers = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	public Collection<Manager> getAll() {
		return managers.values();
	}
	
	public Manager getById(int id) {
		return managers.containsKey(id) ? managers.get(id) : null;
	}
	
	public Manager addNew(Manager newManager) {
		Integer maxId = -1;
		for (int id : managers.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newManager.setId(maxId);
		newManager.setFacilityId(-1);
		managers.put(newManager.getId(), newManager);
		writeFile();
		return newManager;
	}
	
	public Manager update(int id, Manager updatedManager) {
		Manager customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedManager);
		}
		
		customerToUpdate.update(updatedManager);
		writeFile();
		return customerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.managers.remove(id);
		writeFile();
	}

	public Manager getByUsername(String username) {
		for(Manager cust : managers.values()) {
			if(cust.getUsername().equals(username)) {
				return cust;
			}
		}
		return null;
	}
	
	public Collection<Manager> getAvailable() {
		ArrayList<Manager> availbleManager = new ArrayList<>();
		
		for(Manager manager : managers.values()) {
			if(manager.getFacilityId() == -1) {
				availbleManager.add(manager);
			}
		}
		
		return availbleManager;
	}
}
