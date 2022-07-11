package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import beans.Customer;
import beans.Facility;
import beans.Gender;
import beans.Role;
import beans.Workout;
import beans.WorkoutHistory;
import beans.WorkoutType;

@ApplicationPath("/rest")
public class Startup  extends Application{
	@Context
	ServletContext ctx;
	
	public static String path;
	public static Facility deletedFacility;
	public static Customer deletedCutomer;
	public static Workout deletedWorkout;
	
	public Startup() {
		deletedFacility = new Facility(-99, true, "Obrisan",
				"Obrisan tip", new ArrayList<Integer>(),
				false, -99, "deletred.png", -1, "0", "0");
		
		deletedCutomer = new Customer("Obrisan", "Obrisan", "Obrisan", "Obrisan", Gender.male, new Date(),
				Role.customer , true, -99, -1, 0, -99,
				new ArrayList<>(), new ArrayList<>());
		
		deletedWorkout = new Workout(-99, true, "Obrisan", WorkoutType.solo, -99,
				0, -99, "Obrisan", "deleted.png", 0);
	}
	
	@PostConstruct
	public void init() {
	    String contextPath = ctx.getRealPath("");
	    path = contextPath.split(".metadata")[0];
	}
}
