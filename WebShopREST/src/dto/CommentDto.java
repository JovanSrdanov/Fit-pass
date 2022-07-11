package dto;

import beans.Comment;

public class CommentDto {
	private Comment comment;
	private String username;
	private String facilityName;
	
	public CommentDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CommentDto(Comment comment, String username, String facilityName) {
		super();
		this.comment = comment;
		this.username = username;
		this.facilityName = facilityName;
	}

	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
}
