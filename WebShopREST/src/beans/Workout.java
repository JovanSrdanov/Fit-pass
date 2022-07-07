package beans;

public class Workout {
	private int id;
	private boolean isDeleted;
	private String name;
	private WorkoutType workoutType;
	private int facilityId;
	private int durationInMinutes;
	private int trainerId;
	private String description;
	private String picture;
	
	public Workout(int id, boolean isDeleted, String naziv, WorkoutType workoutType, int facilityId,
			int durationInMinutes, int trainerId, String description, String picture) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.name = naziv;
		this.workoutType = workoutType;
		this.facilityId = facilityId;
		this.durationInMinutes = durationInMinutes;
		this.trainerId = trainerId;
		this.description = description;
		this.picture = picture;
	}

	public Workout() {
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

	public String getname() {
		return name;
	}

	public void setname(String naziv) {
		name = naziv;
	}

	public WorkoutType getWorkoutType() {
		return workoutType;
	}

	public void setWorkoutType(WorkoutType workoutType) {
		this.workoutType = workoutType;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void update(Workout newWorkout) {
		//this.id = newWorkout.id;
		//this.isDeleted = newWorkout.isDeleted;
		this.name = newWorkout.name;
		this.workoutType = newWorkout.workoutType;
		//this.facilityId = newWorkout.facilityId;
		this.durationInMinutes = newWorkout.durationInMinutes;
		this.trainerId = newWorkout.trainerId;
		this.description = newWorkout.description;
		this.picture = newWorkout.picture;
		
	}
	
	
}
