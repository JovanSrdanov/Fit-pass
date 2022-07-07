package dto;

import beans.Trainer;
import beans.Workout;

public class WorkoutDto {
	private Workout workout;
	private Trainer trainer;
	
	public Workout getWorkout() {
		return workout;
	}
	public void setWorkout(Workout workout) {
		this.workout = workout;
	}
	public Trainer getTrainer() {
		return trainer;
	}
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}
	
	public WorkoutDto(Workout workout, Trainer trainer) {
		super();
		this.workout = workout;
		this.trainer = trainer;
	}
	
	
}
