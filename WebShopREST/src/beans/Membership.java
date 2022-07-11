package beans;

import java.util.Date;

public class Membership {
	private int id;
	private boolean isDeleted;
	private String code;
	private Date startDate;
	private Date endDate;
	private double price;
	private int customerId;
	private boolean isActive;
	private int numberOfTrainings;
	private int numberOfChechkins;
	
	public Membership() {
		
	}
	
	public Membership(int id, boolean isDeleted, String code, Date startDate, Date endDate, double price,
			int customerId, boolean isActive, int numberOfTrainings, int numberOfChechkins) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.code = code;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.customerId = customerId;
		this.isActive = isActive;
		this.numberOfTrainings = numberOfTrainings;
		this.numberOfChechkins = numberOfChechkins;
	}

	public int getNumberOfChechkins() {
		return numberOfChechkins;
	}

	public void setNumberOfChechkins(int numberOfChechkins) {
		this.numberOfChechkins = numberOfChechkins;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getNumberOfTrainings() {
		return numberOfTrainings;
	}

	public void setNumberOfTrainings(int numberOfTrainings) {
		this.numberOfTrainings = numberOfTrainings;
	}
	
	
	
}
