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

import beans.CustomerType;
import main.Startup;

public class CustomerTypeDao {
private static HashMap<Integer, CustomerType> types;
	
	public CustomerTypeDao() {
		readFile();
	}
	
	/*public CustomerTypeDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}*/
	
	/*private void loadCustomerTypes() {
		//neki json load
		types = new HashMap<Integer, CustomerType>();
		types.put(1, new CustomerType("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}*/
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/CustomerTypeTypes.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(types, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/CustomerTypeTypes.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, CustomerType>>(){}.getType();
			types = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	public Collection<CustomerType> getAll() {
		return types.values();
	}
	
	public CustomerType getById(int id) {
		return types.containsKey(id) ? types.get(id) : null;
	}
	
	public CustomerType addNew(CustomerType newCustomerType) {
		Integer maxId = -1;
		for (int id : types.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newCustomerType.setId(maxId);
		types.put(newCustomerType.getId(), newCustomerType);
		writeFile();
		return newCustomerType;
	}
	
	public CustomerType update(int id, CustomerType updatedCustomerType) {
		CustomerType customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedCustomerType);
		}
		
		customerToUpdate.update(updatedCustomerType);
		writeFile();
		return customerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.types.remove(id);
		writeFile();
	}
}
