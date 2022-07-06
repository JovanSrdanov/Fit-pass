package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.PromoCode;
import beans.Manager;
import main.Startup;

public class PromoCodeDao {
private static HashMap<Integer, PromoCode> promoCodes;
	
	private static HashMap<Integer, PromoCode> allPromoCodes;
	
	public PromoCodeDao() {
		readFile();
	}
	
	private void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/PromoCodes.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(allPromoCodes, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/PromoCodes.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, PromoCode>>(){}.getType();
			allPromoCodes = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	private void filterDeleted() {
		promoCodes = new HashMap<Integer, PromoCode>();
		
		for(PromoCode facility : allPromoCodes.values()) {
			if(!facility.isDeleted()) {
				promoCodes.put(facility.getId(), facility);
			}
		}
		
	}
	
	public Collection<PromoCode> getAll() {
		//readFile();
		return promoCodes.values();
	}

	public PromoCode getById(int id) {
		return promoCodes.containsKey(id) ? promoCodes.get(id) : null;
	}
	
	public PromoCode addNew(PromoCode newPromoCode) {
		Integer maxId = -1;
		for (int id : promoCodes.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newPromoCode.setId(maxId);
		promoCodes.put(newPromoCode.getId(), newPromoCode);
		allPromoCodes.put(newPromoCode.getId(), newPromoCode);
		writeFile();
		return newPromoCode;
	}
	
	public PromoCode update(int id, PromoCode updatedPromoCode) {
		PromoCode facilityToUpdate = this.getById(id);
		
		if(facilityToUpdate == null) {
			return this.addNew(updatedPromoCode);
		}
		
		facilityToUpdate.update(updatedPromoCode);
		writeFile();
		return facilityToUpdate;
	}
	
	public void removeById(int id) {
		this.promoCodes.get(id).setDeleted(true);
		this.promoCodes.remove(id);
		writeFile();
	}
}
