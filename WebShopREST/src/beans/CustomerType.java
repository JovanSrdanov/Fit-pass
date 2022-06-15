package beans;

public class CustomerType {
	private int id;
	private boolean isDeleted;
	private String type;
	private float discount;
	private int pointsNeeded;
	
	public CustomerType() {
		
	}
	
	public CustomerType(String type, int id, boolean isDeleted, float discount, int pointsNeeded) {
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

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public int getPointsNeeded() {
		return pointsNeeded;
	}

	public void setPointsNeeded(int pointsNeeded) {
		this.pointsNeeded = pointsNeeded;
	}
	
	
}
