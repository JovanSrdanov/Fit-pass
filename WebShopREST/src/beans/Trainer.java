package beans;

import java.util.Date;
import java.util.List;

public class Trainer extends User{
	private List<WorkoutHistory> workoutHistory;

	public Trainer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Trainer(String username, String password, String name, String surname, Gender gender, Date birthDate,
			Role role, boolean isDeleted, int id) {
		super(username, password, name, surname, gender, birthDate, role, isDeleted, id);
		// TODO Auto-generated constructor stub
	}

	public List<WorkoutHistory> getWorkoutHistory() {
		return workoutHistory;
	}

	public void setWorkoutHistory(List<WorkoutHistory> workoutHistory) {
		this.workoutHistory = workoutHistory;
	}

	public Trainer(List<WorkoutHistory> workoutHistory) {
		super();
		this.workoutHistory = workoutHistory;
	}

	public void update(Trainer newTrainer) {
		// TODO Auto-generated method stub
		//this.username = newTrainer.username;
		this.password = newTrainer.password;
		this.name = newTrainer.name;
		this.surname = newTrainer.surname;
		this.gender = newTrainer.gender;
		this.birthDate = newTrainer.birthDate;
		//this.role = newTrainer.role;
		//this.isDeleted = newTrainer.isDeleted;
		//this.id = newTrainer.id;
	}
	
	
}
