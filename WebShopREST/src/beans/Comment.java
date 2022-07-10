package beans;

public class Comment {
	private int id;
	private boolean isDeleted;
	private int customerId;
	private int facilityId;
	private String commentText;
	private int rating;
	private CommentStatus status;
	
	public Comment(int id, boolean isDeleted, int customerId, int facilityId, String commentText, int rating,
			CommentStatus status) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.customerId = customerId;
		this.facilityId = facilityId;
		this.commentText = commentText;
		this.rating = rating;
		this.status = status;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public CommentStatus getStatus() {
		return status;
	}
	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	
	
}
