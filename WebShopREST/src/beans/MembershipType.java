package beans;

public class MembershipType {
	private int id;
	private boolean isDeleted;
	private String type;
	
	public MembershipType() {
		
	}

	public MembershipType(int id, String type, boolean isDeleted) {
		super();
		this.id = id;
		this.type = type;
		this.isDeleted = isDeleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
