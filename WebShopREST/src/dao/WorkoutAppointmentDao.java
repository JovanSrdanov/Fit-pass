package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.WorkoutAppointment;
import main.Startup;

public class WorkoutAppointmentDao {
	private static HashMap<Integer, WorkoutAppointment> workoutAppointments;
	
	private static HashMap<Integer, WorkoutAppointment> allWorkoutAppointments;
	
	public WorkoutAppointmentDao() {
		readFile();
	}
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/WorkoutAppointments.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(allWorkoutAppointments, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/WorkoutAppointments.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, WorkoutAppointment>>(){}.getType();
			allWorkoutAppointments = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	private void filterDeleted() {
		workoutAppointments = new HashMap<Integer, WorkoutAppointment>();
		
		for(WorkoutAppointment cust : allWorkoutAppointments.values()) {
			if(!cust.isDeleted()) {
				workoutAppointments.put(cust.getId(), cust);
			}
		}
		
	}

	public Collection<WorkoutAppointment> getAll() {
		return workoutAppointments.values();
	}
	
	public WorkoutAppointment getById(int id) {
		return workoutAppointments.containsKey(id) ? workoutAppointments.get(id) : null;
	}
	
	public ArrayList<WorkoutAppointment> getAllForTrainerId(int id) {
		ArrayList<WorkoutAppointment> appointents = new ArrayList<WorkoutAppointment>();
		for(WorkoutAppointment app : workoutAppointments.values()) {
			if(app.getTrainerId() == id && !app.isCanceled()) {
				appointents.add(app);
			}
		}
		
		return appointents;
	}
	public ArrayList<WorkoutAppointment> getAllForCustomerId(int id) {
		ArrayList<WorkoutAppointment> appointents = new ArrayList<WorkoutAppointment>();
		for(WorkoutAppointment app : workoutAppointments.values()) {
			if(app.getCustomerId() == id) {
				appointents.add(app);
			}
		}
		
		return appointents;
	}
	
	public void cancelById(int id) {
		workoutAppointments.get(id).setCanceled(true);
		writeFile();
	}
	
	public WorkoutAppointment addNew(WorkoutAppointment newWorkoutAppointment) {
		Integer maxId = -1;
		for (int id : workoutAppointments.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newWorkoutAppointment.setId(maxId);
		workoutAppointments.put(newWorkoutAppointment.getId(), newWorkoutAppointment);
		allWorkoutAppointments.put(newWorkoutAppointment.getId(), newWorkoutAppointment);
		writeFile();
		return newWorkoutAppointment;
	}
	
	public void removeById(int id) {
		this.workoutAppointments.get(id).setDeleted(true);
		this.workoutAppointments.remove(id);
		writeFile();
	}
}
