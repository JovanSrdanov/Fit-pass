package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.Role;
import dao.FacilityDao;
import dao.WorkoutDao;
import main.Startup;

@Path("/files")  
public class FileUploadService {  
    @POST  
    @Path("/upload") 
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadFile(  
            @FormDataParam("file") InputStream uploadedInputStream,  
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context HttpHeaders headers) { 
    		
    	String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
    		
		FacilityDao facilityDao = new FacilityDao();
		int nextId = facilityDao.getNextId();
		String fileLocation = Startup.path + "WebProjekat/WebShopREST/WebContent/FacilityLogo/FacilityLogo" + nextId + ".png";
        //String fileLocation = "D://" + fileDetail.getFileName();
        //saving file  
        try {  
            FileOutputStream out = new FileOutputStream(new File(fileLocation));  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            out = new FileOutputStream(new File(fileLocation));  
            while ((read = uploadedInputStream.read(bytes)) != -1) {  
                out.write(bytes, 0, read);  
            }  
            out.flush();  
            out.close();  
        } catch (IOException e) {e.printStackTrace();}  
        String output = "File successfully uploaded to : " + fileLocation;  
        return Response.status(200).entity(output).build();  
    } 
    
    @POST  
    @Path("/workoutPicture") 
    @JWTTokenNeeded
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadWorkoutPicture(  
            @FormDataParam("file") InputStream uploadedInputStream,  
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context HttpHeaders headers) { 
    		
    	String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.manager.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
    		
		WorkoutDao workoutDao = new WorkoutDao();
		int nextId = workoutDao.getNextId();
		String fileLocation = Startup.path + "WebProjekat/WebShopREST/WebContent/ActivityPictures/WorkoutLogo" + nextId + ".png";
        //String fileLocation = "D://" + fileDetail.getFileName();
        //saving file  
        try {  
            FileOutputStream out = new FileOutputStream(new File(fileLocation));  
            int read = 0;  
            byte[] bytes = new byte[1024];  
            out = new FileOutputStream(new File(fileLocation));  
            while ((read = uploadedInputStream.read(bytes)) != -1) {  
                out.write(bytes, 0, read);  
            }  
            out.flush();  
            out.close();  
        } catch (IOException e) {e.printStackTrace();}  
        String output = "File successfully uploaded to : " + fileLocation;  
        return Response.status(200).entity(output).build();  
    }  
    
    
    @GET
    @Path("/path")
    public String getPath() {
    	return Startup.path;
    }
  } 
