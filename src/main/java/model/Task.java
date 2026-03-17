package model;

public class Task {

    private int taskId;
    private String title;
    private String description;
    private String status;
    private String createdAt;
    private int userId;

    public Task(){}

    public Task(String title,String description){
        this.title = title;
        this.description = description;
    }
    
    public Task(int taskId, String title,String description){
        this.title = title;
        this.description = description;
        this.taskId = taskId;
    }
    

    public int getTaskId(){
        return taskId;
    }

    public void setTaskId(int taskId){
        this.taskId = taskId;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    
    public void setCreatedAt(String createdAt) {
    	this.createdAt = createdAt;
    }
    
    public String getCreatedAt() {
    	return createdAt;
    }
    public void setUserId(int userId) {
    	this.userId = userId;
    }
}