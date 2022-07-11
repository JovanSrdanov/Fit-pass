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

import beans.Customer;
import beans.Manager;
import main.Startup;

public class ManagerDao {
	private static HashMap<Integer, Manager> managers;
	
	private static HashMap<Integer, Manager> allManagers;
	
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
			gson.toJson(allManagers, writer);
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
			allManagers = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	private void filterDeleted() {
		managers = new HashMap<Integer, Manager>();
		
		for(Manager cust : allManagers.values()) {
			if(!cust.isDeleted()) {
				managers.put(cust.getId(), cust);
			}
		}
		
	}
	
	public Collection<Manager> getAll() {
		return managers.values();
	}
	
	public Manager getById(int id) {
		if(managers.containsKey(id)) {
			return managers.get(id);
		}
		else if(allManagers.containsKey(id) || id == -99) {
			return Startup.deletedManager;
		}
		else {
			return null;
		}
	}
	
	public Manager addNew(Manager newManager) {
		Integer maxId = -1;
		for (int id : allManagers.keySet()) {
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
		this.managers.get(id).setDeleted(true);
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
