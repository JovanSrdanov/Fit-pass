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
import beans.Facility;
import beans.Gender;
import beans.Role;

public class CustomerDao {
	private static HashMap<Integer, Customer> customers;
	private String path;
	
	public CustomerDao() {
	}
	
	public CustomerDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}
	
	private void loadCustomers() {
		//neki json load
		customers = new HashMap<Integer, Customer>();
		customers.put(1, new Customer("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}
	
	private void writeFile() {
		File theFile = new File(path + "WebProjekat/Data/Customers.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(customers, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(path + "WebProjekat/Data/Customers.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Customer>>(){}.getType();
			customers = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	public Collection<Customer> getAll() {
		return customers.values();
	}
	
	public Customer getById(int id) {
		return customers.containsKey(id) ? customers.get(id) : null;
	}
	
	public Customer addNew(Customer newCustomer) {
		Integer maxId = -1;
		for (int id : customers.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newCustomer.setId(maxId);
		customers.put(newCustomer.getId(), newCustomer);
		writeFile();
		return newCustomer;
	}
	
	public Customer update(int id, Customer updatedCustomer) {
		Customer customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedCustomer);
		}
		
		customerToUpdate.update(updatedCustomer);
		writeFile();
		return customerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.customers.remove(id);
		writeFile();
	}
}
