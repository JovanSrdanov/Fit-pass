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

import beans.BaseMembership;
import main.Startup;

public class BaseMembershipDao {
	
	private static HashMap<Integer, BaseMembership> baseMemeberships;
	
	private static HashMap<Integer, BaseMembership> allBaseMemberships;
	
	public BaseMembershipDao() {
		readFile();
	}
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/BaseMemberships.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(allBaseMemberships, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/BaseMemberships.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, BaseMembership>>(){}.getType();
			allBaseMemberships = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	private void filterDeleted() {
		baseMemeberships = new HashMap<Integer, BaseMembership>();
		
		for(BaseMembership cust : allBaseMemberships.values()) {
			if(!cust.isDeleted()) {
				baseMemeberships.put(cust.getId(), cust);
			}
		}
		
	}

	public Collection<BaseMembership> getAll() {
		return baseMemeberships.values();
	}
	
	public BaseMembership getById(int id) {
		return baseMemeberships.containsKey(id) ? baseMemeberships.get(id) : null;
	}
	
	public BaseMembership getByCode(String code) {
		for(BaseMembership mem : baseMemeberships.values()) {
			if(mem.getCode().equals(code)) {
				return mem;
			}
		}
		
		return null;
	}
	
	public BaseMembership addNew(BaseMembership newBaseMembership) {
		Integer maxId = -1;
		for (int id : baseMemeberships.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newBaseMembership.setId(maxId);
		baseMemeberships.put(newBaseMembership.getId(), newBaseMembership);
		allBaseMemberships.put(newBaseMembership.getId(), newBaseMembership);
		writeFile();
		return newBaseMembership;
	}
	
	public void removeById(int id) {
		this.baseMemeberships.get(id).setDeleted(true);
		this.baseMemeberships.remove(id);
		writeFile();
	}
}
