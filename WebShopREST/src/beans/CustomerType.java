package beans;

public class CustomerType {
	private int id;
	private boolean isDeleted;
	private String type;
	private double discount;
	private int pointsNeeded;
	
	public CustomerType() {
		
	}
	
	public CustomerType(String type, int id, boolean isDeleted, double discount, int pointsNeeded) {
		super();
		this.type = type;
		this.id = id;
		this.isDeleted = isDeleted;
		this.discount = discount;
		this.pointsNeeded = pointsNeeded;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getPointsNeeded() {
		return pointsNeeded;
	}

	public void setPointsNeeded(int pointsNeeded) {
		this.pointsNeeded = pointsNeeded;
	}

	public void update(CustomerType newCustomerType) {
		this.type = newCustomerType.type;
		this.id = newCustomerType.id;
		this.isDeleted = newCustomerType.isDeleted;
		this.discount = newCustomerType.discount;
		this.pointsNeeded = newCustomerType.pointsNeeded;
		
	}
	
	
}
