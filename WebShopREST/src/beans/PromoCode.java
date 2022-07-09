package beans;

import java.util.Date;

public class PromoCode {
	private int id;
	private boolean isDeleted;
	private String code;
	private Date validDate;
	private int usageCount;
	private double discountPercentage;
	
	public PromoCode() {
		
	}
	
	public PromoCode(int id, boolean isDeleted, String code, Date validDate, int usageCount,
			double discountPercentage) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.code = code;
		this.validDate = validDate;
		this.usageCount = usageCount;
		this.discountPercentage = discountPercentage;
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

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public int getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public void update(PromoCode updatedPromoCode) {
		this.id = updatedPromoCode.id;
		this.isDeleted = updatedPromoCode.isDeleted;
		this.code = updatedPromoCode.code;
		this.validDate = updatedPromoCode.validDate;
		this.usageCount = updatedPromoCode.usageCount;
		this.discountPercentage = updatedPromoCode.discountPercentage;
	}

	public void reduceUsgeCount() {
		this.usageCount -= 1;
		if(this.usageCount <= 0) {
			this.usageCount = 0;
		}
		
	}
	
	
}
