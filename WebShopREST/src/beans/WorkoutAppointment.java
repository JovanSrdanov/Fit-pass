package beans;

import java.util.Date;

public class WorkoutAppointment {
	private int id;
	private boolean isDeleted;
	private int workoutId;
	private Date date;
	private int customerId;
	private int trainerId;
	private boolean isCanceled;
	
	public WorkoutAppointment(int id, boolean isDeleted, int workoutId, Date date, int customerId, int trainerId,
			boolean isCanceled) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.workoutId = workoutId;
		this.date = date;
		this.customerId = customerId;
		this.trainerId = trainerId;
		this.isCanceled = isCanceled;
	}

	public WorkoutAppointment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public boolean isCanceled() {
		return isCanceled;
	}
	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
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
	public int getWorkoutId() {
		return workoutId;
	}
	public void setWorkoutId(int workoutId) {
		this.workoutId = workoutId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	

}
