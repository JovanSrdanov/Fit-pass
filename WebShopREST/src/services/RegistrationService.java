package services;

import beans.Admin;
import beans.Customer;
import beans.Manager;
import beans.Trainer;
import dao.AdminDao;
import dao.CustomerDao;
import dao.ManagerDao;
import dao.TrainerDao;

public class RegistrationService {
	
	public static boolean usernameExists(String username) {
		
		boolean exists = false;
		
		CustomerDao dao = new CustomerDao();
		for(Customer cust : dao.getAll()) {
			if(cust.getUsername().equals(username)) {
				//return Response.status(409).build();
				exists = true;
			}
		}
		
		if(exists == false) {
			AdminDao adminDao = new AdminDao();
			for(Admin admin : adminDao.getAll()) {
				if(admin.getUsername().equals(username)) {
					//return Response.status(409).build();
					exists = true;
				}
			}
		}
		
		if(exists == false) {
			ManagerDao managerDao = new ManagerDao();
			for(Manager manager : managerDao.getAll()) {
				if(manager.getUsername().equals(username)) {
					//return Response.status(409).build();
					exists = true;
				}
			}
		}
		
		if(exists == false) {
			TrainerDao trainerDao = new TrainerDao();
			for(Trainer trainer : trainerDao.getAll()) {
				if(trainer.getUsername().equals(username)) {
					//return Response.status(409).build();
					exists = true;
				}
			}
		}
		
		return exists;
	}
}
