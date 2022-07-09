package beans;

import java.util.Date;

public class WorkoutHistory {
	private int workoutId;
	private Date checkDate;
	private int customerId;
	private int trainerId;
	
	public WorkoutHistory(int workoutId, Date checkDate, int customerId, int trainerId) {
		super();
		this.workoutId = workoutId;
		this.checkDate = checkDate;
		this.customerId = customerId;
		this.trainerId = trainerId;
	}


	public int getCustomerId() {
		return customerId;
	}


	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}


	public int getTrainerId() {
		return trainerId;
	}


	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}


	public WorkoutHistory() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getWorkoutId() {
		return workoutId;
	}
	public void setWorkoutId(int workoutId) {
		this.workoutId = workoutId;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	
}
