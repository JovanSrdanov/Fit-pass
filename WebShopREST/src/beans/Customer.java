package beans;

import java.util.Date;

public class Customer extends User{
	private int membershipId;
	private int points;
	private int customerTypeId;
	
	public Customer(int membershipId, int points) {
		super();
		this.membershipId = membershipId;
		this.points = points;
	}
	
	public Customer(String username, String password, String name, String surname, Gender gender, Date birthDate,
			Role role, boolean isDeleted, int id, int membershipId, int points, int customerTypeId) {
		super(username, password, name, surname, gender, birthDate, role, isDeleted, id);
		this.membershipId = membershipId;
		this.points = points;
		this.customerTypeId = customerTypeId;
	}

	public Customer() {
		super();
	}
	public int getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	
	
	public int getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(int customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public void update(Customer newCustomer) {
		this.username = newCustomer.username;
		this.password = newCustomer.password;
		this.name = newCustomer.name;
		this.surname = newCustomer.surname;
		this.gender = newCustomer.gender;
		this.birthDate = newCustomer.birthDate;
		this.role = newCustomer.role;
		this.isDeleted = newCustomer.isDeleted;
		this.id = newCustomer.id;
		this.membershipId = newCustomer.membershipId;
		this.points = newCustomer.points;
		this.customerTypeId = newCustomer.customerTypeId;
	}
}
