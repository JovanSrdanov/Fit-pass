package dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import beans.Customer;
import beans.Gender;
import beans.Role;

public class CustomerDao {
	private static HashMap<Integer, Customer> customers;
	
	public CustomerDao() {
		loadCustomers();
	}
	
	private void loadCustomers() {
		//neki json load
		customers = new HashMap<Integer, Customer>();
		customers.put(1, new Customer("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}
	
	//const sa context path?
	
	
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
		return newCustomer;
	}
	
	public Customer update(int id, Customer updatedCustomer) {
		Customer customerToUpdate = this.getById(id);
		
		if(customerToUpdate == null) {
			return this.addNew(updatedCustomer);
		}
		
		customerToUpdate.update(updatedCustomer);
		return customerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.customers.remove(id);
	}
}
