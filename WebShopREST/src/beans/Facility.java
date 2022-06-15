package beans;

public class Facility {
	private int id;
	private boolean isDeleted;
	private String name;
	public Facility(int id, boolean isDeleted, String name) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.name = name;
	}
	public Facility() {
		super();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
