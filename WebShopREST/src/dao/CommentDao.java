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

import beans.Comment;
import beans.CommentStatus;
import main.Startup;

public class CommentDao {
private static HashMap<Integer, Comment> comments;
	
	private static HashMap<Integer, Comment> allComments;
	
	public CommentDao() {
		readFile();
	}
	
	/*public CommentDao(String path) {
		String goodPath = path.split(".metadata")[0];
		this.path = goodPath;
		readFile();
	}*/
	
	/*private void loadComments() {
		//neki json load
		comments = new HashMap<Integer, Comment>();
		comments.put(1, new Comment("strale15", "cip11", "Strahinja", "Erakovic", Gender.male, new Date(),
				Role.customer, false, 1, -1, 0));
	}*/
	
	public void writeFile() {
		File theFile = new File(Startup.path + "WebProjekat/Data/Comments.json");
		
		try {
			FileWriter writer = new FileWriter(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			gson.toJson(allComments, writer);
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}	
	}
	
	private void readFile() {
		
		File theFile = new File(Startup.path + "WebProjekat/Data/Comments.json");
		
		try {
			FileReader reader = new FileReader(theFile);
			Gson gson = new GsonBuilder()
					  .setPrettyPrinting()
					  .create();
			Type type = new TypeToken<HashMap<Integer, Comment>>(){}.getType();
			allComments = gson.fromJson(reader, type);
			reader.close();
			
			filterDeleted();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error2");
		}
	}
	
	
	private void filterDeleted() {
		comments = new HashMap<Integer, Comment>();
		
		for(Comment cust : allComments.values()) {
			if(!cust.isDeleted()) {
				comments.put(cust.getId(), cust);
			}
		}
		
	}
	
	public boolean exists(int customerId, int facilityId) {
		for(Comment com : comments.values()) {
			if(com.getFacilityId() == facilityId && com.getCustomerId() == customerId) {
				return true;
			}
		}
		return false;
	}

	public Collection<Comment> getAll() {
		return comments.values();
	}
	
	public Collection<Comment> getForFacilityApproved(int id) {
		ArrayList<Comment> commentsFacility = new ArrayList<Comment>();
		
		for(Comment com : comments.values()) {
			if(com.getFacilityId() == id && com.getStatus() == CommentStatus.approved) {
				commentsFacility.add(com);
			}
		}
		
		return commentsFacility;
	}
	
	public Collection<Comment> getForFacilityAll(int id) {
		ArrayList<Comment> commentsFacility = new ArrayList<Comment>();
		
		for(Comment com : comments.values()) {
			if(com.getFacilityId() == id) {
				commentsFacility.add(com);
			}
		}
		
		return commentsFacility;
	}
	
	public Comment getById(int id) {
		return comments.containsKey(id) ? comments.get(id) : null;
	}
	
	public Comment addNew(Comment newComment) {
		Integer maxId = -1;
		for (int id : comments.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		newComment.setId(maxId);
		comments.put(newComment.getId(), newComment);
		allComments.put(newComment.getId(), newComment);
		writeFile();
		return newComment;
	}
	
	public void removeById(int id) {
		this.comments.get(id).setDeleted(true);
		this.comments.remove(id);
		writeFile();
	}

	public int getNumberOfApprovedForFacility(int facilityId) {
		int numberOfComments = 0;
		
		for(Comment com : comments.values()) {
			if(com.getFacilityId() == facilityId && com.getStatus() == CommentStatus.approved) {
				numberOfComments += 1;
			}
		}
		return numberOfComments;
	}

	public Collection<Comment> getAllWaiting() {
		ArrayList<Comment> waitingComments = new ArrayList<Comment>();
		
		for(Comment com : comments.values()) {
			if(com.getStatus() == CommentStatus.waiting) {
				waitingComments.add(com);
			}
		}
		
		return waitingComments;
	}
}
