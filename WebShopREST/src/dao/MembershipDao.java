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

import beans.Membership;
import main.Startup;

public class MembershipDao {
	private static HashMap<Integer, Membership> memberships;
	
	private static HashMap<Integer, Membership> allMemberships;
	
	public MembershipDao() {
		readFile();
	}
	
	/*public MembershipDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}*/
	
	/*private void loadMemberships() {
		//neki json load
		memberships = new HashMap<Integer, Membership>();
		memberships.put(1, new Membership("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}*/
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Memberships.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(allMemberships, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Memberships.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Membership>>(){}.getType();
			allMemberships = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	private void filterDeleted() {
		memberships = new HashMap<Integer, Membership>();
		
		for(Membership cust : allMemberships.values()) {
			if(!cust.isDeleted()) {
				memberships.put(cust.getId(), cust);
			}
		}
		
	}

	public Collection<Membership> getAll() {
		return memberships.values();
	}
	
	public Membership getById(int id) {
		return memberships.containsKey(id) ? memberships.get(id) : null;
	}
	
	public Membership addNew(Membership newMembership) {
		Integer maxId = -1;
		for (int id : memberships.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newMembership.setId(maxId);
		memberships.put(newMembership.getId(), newMembership);
		allMemberships.put(newMembership.getId(), newMembership);
		writeFile();
		return newMembership;
	}
	
	public void removeById(int id) {
		this.memberships.get(id).setDeleted(true);
		this.memberships.remove(id);
		writeFile();
	}
	
	public Membership getForCustomerId(int id) {
		for(Membership membership : memberships.values()) {
			if(membership.getCustomerId() == id) {
				return membership;
			}
		}
		
		return null;
	}
	
	/*public Membership update(int id, Membership updatedMembership) {
		Membership customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedMembership);
		}
		
		customerToUpdate.update(updatedMembership);
		writeFile();
		return customerToUpdate;
	}*/
	

	/*public Membership getByUsername(String username) {
		for(Membership cust : memberships.values()) {
			if(cust.getUsername().equals(username)) {
				return cust;
			}
		}
		return null;
	}*/
}
