package dto;

import java.util.Date;

import beans.WorkoutType;

public class WorkoutHistoryDto {
	private String workoutName;
	private String facilityName;
	private Date workoutDate;
	private WorkoutType workoutType;
	private String customerFullName;
	private String trainerFullName;
	
	public WorkoutHistoryDto(String workoutName, String facilityName, Date workoutDate, WorkoutType workoutType,
			String customerFullName, String trainerFullName) {
		super();
		this.workoutName = workoutName;
		this.facilityName = facilityName;
		this.workoutDate = workoutDate;
		this.workoutType = workoutType;
		this.customerFullName = customerFullName;
		this.trainerFullName = trainerFullName;
	}

	public WorkoutHistoryDto() {
		
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
