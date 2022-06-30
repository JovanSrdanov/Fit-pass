package beans;

import java.util.Date;

public class Manager extends User{
	private int facilityId;

	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manager(int facilityId) {
		super();
		this.facilityId = facilityId;
	}

	public Manager(String username, String password, String name, String surname, Gender gender, Date birthDate,
			Role role, boolean isDeleted, int id, int facilityId) {
		super(username, password, name, surname, gender, birthDate, role, isDeleted, id);
		this.facilityId = facilityId;
	}
	
	public void update(Manager newManager) {
		this.username = newManager.username;
		this.password = newManager.password;
		this.name = newManager.name;
		this.surname = newManager.surname;
		this.gender = newManager.gender;
		this.birthDate = newManager.birthDate;
		this.role = newManager.role;
		this.isDeleted = newManager.isDeleted;
		this.id = newManager.id;
		this.facilityId = newManager.facilityId;
	}
	
	

}
