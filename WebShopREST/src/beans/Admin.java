package beans;

import java.util.Date;

public class Admin extends User{

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String username, String password, String name, String surname, Gender gender, Date birthDate,
			Role role, boolean isDeleted, int id) {
		super(username, password, name, surname, gender, birthDate, role, isDeleted, id);
		// TODO Auto-generated constructor stub
	}
	
	public void update(Admin newAdmin) {
		//this.username = newAdmin.username;
		this.password = newAdmin.password;
		this.name = newAdmin.name;
		this.surname = newAdmin.surname;
		this.gender = newAdmin.gender;
		this.birthDate = newAdmin.birthDate;
		//this.role = newAdmin.role;
		//this.isDeleted = newAdmin.isDeleted;
		//this.id = newAdmin.id;
	}

}
