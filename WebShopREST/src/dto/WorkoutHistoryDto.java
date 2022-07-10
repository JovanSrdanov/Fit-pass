package dto;

import java.util.Date;

import beans.WorkoutType;

public class WorkoutHistoryDto {
	private int appointentId;
	private String workoutName;
	private String facilityName;
	private Date workoutDate;
	private WorkoutType workoutType;
	private String customerFullName;
	private String trainerFullName;
	private double price;
	private boolean isCanceled;
	private String facilityType;
	


	public WorkoutHistoryDto(int appointentId, String workoutName, String facilityName, Date workoutDate,
			WorkoutType workoutType, String customerFullName, String trainerFullName, double price, String facilityType) {
		super();
		this.appointentId = appointentId;
		this.workoutName = workoutName;
		this.facilityName = facilityName;
		this.workoutDate = workoutDate;
		this.workoutType = workoutType;
		this.customerFullName = customerFullName;
		this.trainerFullName = trainerFullName;
		this.price = price;
		this.isCanceled = false;
		this.facilityType = facilityType;
	}

	public WorkoutHistoryDto(int appointentId, String workoutName, String facilityName, Date workoutDate,
			WorkoutType workoutType, String customerFullName, String trainerFullName, double price,
			boolean isCanceled, String facilityType) {
		super();
		this.appointentId = appointentId;
		this.workoutName = workoutName;
		this.facilityName = facilityName;
		this.workoutDate = workoutDate;
		this.workoutType = workoutType;
		this.customerFullName = customerFullName;
		this.trainerFullName = trainerFullName;
		this.price = price;
		this.isCanceled = isCanceled;
		this.facilityType = facilityType;
	}



	public WorkoutHistoryDto() {
		
	}
	
	public String getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public int getAppointentId() {
		return appointentId;
	}

	public void setAppointentId(int appointentId) {
		this.appointentId = appointentId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getWorkoutId() {
		return appointentId;
	}


	public void setWorkoutId(int appointentId) {
		this.appointentId = appointentId;
	}


	public String getCustomerFullName() {
		return customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getTrainerFullName() {
		return trainerFullName;
	}

	public void setTrainerFullName(String trainerFullName) {
		this.trainerFullName = trainerFullName;
	}

	public String getWorkoutName() {
		return workoutName;
	}
	public void setWorkoutName(String workoutName) {
		this.workoutName = workoutName;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public Date getWorkoutDate() {
		return workoutDate;
	}
	public void setWorkoutDate(Date workoutDate) {
		this.workoutDate = workoutDate;
	}
	public WorkoutType getWorkoutType() {
		return workoutType;
	}
	public void setWorkoutType(WorkoutType workoutType) {
		this.workoutType = workoutType;
	}
	
	
}
