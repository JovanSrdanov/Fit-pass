package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Facility;
import beans.Location;
import beans.Manager;
import beans.Role;
import beans.Workout;
import beans.WorkoutAppointment;
import dao.CustomerDao;
import dao.FacilityDao;
import dao.LocationDao;
import dao.ManagerDao;
import dao.WorkoutAppointmentDao;
import dao.WorkoutDao;
import dto.CreateFacilityDto;
import dto.FacilityDto;
import main.Startup;

@Path("/facilitys")
public class FacilityService {
	@Context
	ServletContext ctx;
	
	public FacilityService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("FacilityDao") == null) {
			ctx.setAttribute("FacilityDao", new FacilityDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FacilityDto> getAll() {
		FacilityDao dao = (FacilityDao) ctx.getAttribute("FacilityDao");
		return dao.getAllTable();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public FacilityDto getAll(@PathParam("id") int id) {
		FacilityDao dao = (FacilityDao) ctx.getAttribute("FacilityDao");
		return dao.getAllInfoById(id);
	}
	
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FacilityDto> getAllFilter(@FormParam("name") String name,
												@FormParam("facilityType") String facilityType,
												@FormParam("locationString") String locationString,
												@FormParam("rating") String rating) {
		FacilityDao dao = (FacilityDao) ctx.getAttribute("FacilityDao");
		return dao.getAllFilter(ctx.getRealPath(""), name, facilityType, locationString, rating);
	}
	
	@POST
	@Path("/new")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public CreateFacilityDto createNew(CreateFacilityDto newFacilitDtoy, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		FacilityDao facilityDao = (FacilityDao) ctx.getAttribute("FacilityDao");
		ManagerDao managerDao = new ManagerDao();
		LocationDao locationDao = new LocationDao();
		
		//Location
		Location newLocation = new Location(newFacilitDtoy.getLatitude(),
											newFacilitDtoy.getLongitude(),
											newFacilitDtoy.getStreet(),
											newFacilitDtoy.getStreetNumber(),
											newFacilitDtoy.getCity(),
											newFacilitDtoy.getPostCode());
		locationDao.addNew(newLocation);
		
		//Facility
		Facility newFacility = new Facility(newFacilitDtoy.getName(), 
											newFacilitDtoy.getFacilityType(), 
											newFacilitDtoy.getWorkStart(),
											newFacilitDtoy.getWorkEnd());
		newFacility.setLocationId(newLocation.getId());
		//slika
		facilityDao.addNew(newFacility);
		newFacility.setLogo("FacilityLogo" + newFacility.getId() + ".png");
		facilityDao.writeFile();
		
		//Manager
		Manager facilityManager = managerDao.getById(newFacilitDtoy.getManagerId());
		
		if(facilityManager == null) {
			throw new WebApplicationException(Response.Status.CONFLICT); 
		}
		
		facilityManager.setFacilityId(newFacility.getId());
		managerDao.writeFile();
		
		return newFacilitDtoy;
	}
	
	@DELETE
    @Path("/delete/{id}")
    @JWTTokenNeeded
    public Response deleteComment(@PathParam("id") int id, @Context HttpHeaders headers) {
        String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
        if(!role.equals(Role.admin.toString())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        
        FacilityDao facilityDao = (FacilityDao) ctx.getAttribute("FacilityDao");
        Facility facility = facilityDao.getById(id);
        if(facility == null || facility == Startup.deletedFacility) {
        	throw new WebApplicationException(Response.Status.CONFLICT);
        }
        
        ManagerDao managerDao = new ManagerDao();
        Manager manager = managerDao.getById(id);
        if(manager == null) {
        	throw new WebApplicationException(Response.Status.CONFLICT);
        }
        
        manager.setFacilityId(-1);
        managerDao.writeFile();
        
        WorkoutAppointmentDao appointmentDao = new WorkoutAppointmentDao();
        WorkoutDao workoutDao = new WorkoutDao();
        
        ArrayList<Integer> appointemntsForDelete = new ArrayList<Integer>();
        
        for(WorkoutAppointment app : appointmentDao.getAll()) {
        	Workout workout = workoutDao.getById(app.getWorkoutId());
        	
        	if(workout.getFacilityId() == id) {
        		appointemntsForDelete.add(app.getId());
        	}
        }
        
        for(int appId : appointemntsForDelete) {
        	appointmentDao.removeById(appId);
        }
        
        facilityDao.removeById(id);

        return Response.ok().build();
    }
}
