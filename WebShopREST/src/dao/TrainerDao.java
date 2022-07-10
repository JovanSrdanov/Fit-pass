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

import beans.Customer;
import beans.Trainer;
import main.Startup;

public class TrainerDao {
	private static HashMap<Integer, Trainer> trainers;
	
	public TrainerDao() {
		readFile();
	}
	
	/*public TrainerDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}*/
	
	/*private void loadTrainers() {
		//neki json load
		trainers = new HashMap<Integer, Trainer>();
		trainers.put(1, new Trainer("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}*/
	
	public void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Trainers.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(trainers, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Trainers.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Trainer>>(){}.getType();
			trainers = gson.fromJson(reader, type);
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	public Collection<Trainer> getAll() {
		return trainers.values();
	}
	
	public Trainer getById(int id) {
		return trainers.containsKey(id) ? trainers.get(id) : null;
	}
	
	public Trainer addNew(Trainer newTrainer) {
		Integer maxId = -1;
		for (int id : trainers.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newTrainer.setId(maxId);
		trainers.put(newTrainer.getId(), newTrainer);
		writeFile();
		return newTrainer;
	}
	
	public Trainer update(int id, Trainer updatedTrainer) {
		Trainer trainerToUpdate = this.getById(id);
		
		if(trainerToUpdate == null) {
			return this.addNew(updatedTrainer);
		}
		
		trainerToUpdate.update(updatedTrainer);
		writeFile();
		return trainerToUpdate;
	}
	
	public void removeById(int id) {
		
		//treba logicko
		this.trainers.remove(id);
		writeFile();
	}
	
	public Trainer getByUsername(String username) {
		for(Trainer train : trainers.values()) {
			if(train.getUsername().equals(username)) {
				return train;
			}
		}
		return null;
	}

}
