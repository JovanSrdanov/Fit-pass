package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.CustomerDao;
import dao.FacilityDao;
import dto.FacilityDto;

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

}
