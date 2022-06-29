package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Admin;
import main.Startup;

public class AdminDao {
	private static HashMap<Integer, Admin> admins;
	
	public AdminDao() {
		readFile();
	}
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Admins.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(admins, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Admins.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Admin>>(){}.getType();
			admins = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	public Collection<Admin> getAll() {
		return admins.values();
	}
	
	public Admin getById(int id) {
		return admins.containsKey(id) ? admins.get(id) : null;
	}
	
	public Admin addNew(Admin newAdmin) {
		Integer maxId = -1;
		for (int id : admins.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newAdmin.setId(maxId);
		admins.put(newAdmin.getId(), newAdmin);
		writeFile();
		return newAdmin;
	}
	
	public Admin update(int id, Admin updatedAdmin) {
		Admin customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedAdmin);
		}
		
		customerToUpdate.update(updatedAdmin);
		writeFile();
		return customerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.admins.remove(id);
		writeFile();
	}

	public Admin getByUsername(String username) {
		for(Admin cust : admins.values()) {
			if(cust.getUsername().equals(username)) {
				return cust;
			}
		}
		return null;
	}
}
