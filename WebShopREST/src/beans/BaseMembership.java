package beans;

public class BaseMembership {
	private int id;
	private boolean isDeleted;
	private String code;
	private double priceMultiplicator;
	private int durationDays;
	public BaseMembership(int id, boolean isDeleted, String code, double priceMultiplicator, int durationDays) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.code = code;
		this.priceMultiplicator = priceMultiplicator;
		this.durationDays = durationDays;
	}
	public BaseMembership() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getPriceMultiplicator() {
		return priceMultiplicator;
	}
	public void setPriceMultiplicator(double priceMultiplicator) {
		this.priceMultiplicator = priceMultiplicator;
	}
	public int getDurationDays() {
		return durationDays;
	}
	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}
	
	
}
