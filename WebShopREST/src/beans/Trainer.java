package beans;

import java.util.Date;
import java.util.List;

public class Trainer extends User{
	private List<Integer> workoutIds;

	public Trainer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Trainer(String username, String password, String name, String surname, Gender gender, Date birthDate,
			Role role, boolean isDeleted, int id, List<Integer> workoutIds) {
		super(username, password, name, surname, gender, birthDate, role, isDeleted, id);
		this.workoutIds = workoutIds;
	}

	public List<Integer> getWorkoutIds() {
		return workoutIds;
	}

	public void setWorkoutIds(List<Integer> workoutIds) {
		this.workoutIds = workoutIds;
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
