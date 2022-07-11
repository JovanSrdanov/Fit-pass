package dto;

public class CustomerTypeDto {
	private String typeName;
	private double discount;
	
	public CustomerTypeDto(String typeName, double discount) {
		super();
		this.typeName = typeName;
		this.discount = discount;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
}
