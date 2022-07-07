package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Workout;
import main.Startup;

public class WorkoutDao {
private static HashMap<Integer, Workout> workouts;
	
	public WorkoutDao() {
		readFile();
	}
	
	/*public WorkoutDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}*/
	
	/*private void loadWorkouts() {
		//neki json load
		workouts = new HashMap<Integer, Workout>();
		workouts.put(1, new Workout("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}*/
	
	public void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Workouts.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(workouts, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Workouts.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Workout>>(){}.getType();
			workouts = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	public Collection<Workout> getAll() {
		return workouts.values();
	}
	
	public Workout getById(int id) {
		return workouts.containsKey(id) ? workouts.get(id) : null;
	}
	
	public Workout addNew(Workout newWorkout) {
		Integer maxId = -1;
		for (int id : workouts.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newWorkout.setId(maxId);
		workouts.put(newWorkout.getId(), newWorkout);
		writeFile();
		return newWorkout;
	}
	
	public Workout update(int id, Workout updatedWorkout) {
		Workout workoutToUpdate = this.getById(id);
		
		if(workoutToUpdate == null) {
			return this.addNew(updatedWorkout);
		}
		
		workoutToUpdate.update(updatedWorkout);
		writeFile();
		return workoutToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.workouts.remove(id);
		writeFile();
	}

	public int getNextId() {
		Integer maxId = -1;
		for (int id : workouts.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		return maxId;
	}
}
