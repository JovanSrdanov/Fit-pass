package beans;

import java.util.Date;

public class WorkoutHistory {
	private int workoutId;
	private Date checkDate;
	
	public WorkoutHistory(int workoutId, Date checkDate) {
		super();
		this.workoutId = workoutId;
		this.checkDate = checkDate;
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
