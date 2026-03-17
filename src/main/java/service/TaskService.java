package service;

import java.util.List;

import dao.TaskDAO;
import model.Task;

public class TaskService {

    private TaskDAO taskDAO = new TaskDAO();

    public boolean createTask(Task task){

        if(task.getTitle()==null || task.getTitle().trim().isEmpty()){
            return false;
        }

        return taskDAO.addTask(task);
    }
    
    public List<Task>  getUserTasks(int userId) {
    	return taskDAO.getUserTasks(userId);
    }
    
    public boolean updateTask(Task task) {
    	return taskDAO.updateTask(task);
    }
    
    public boolean deleteTask(int taskId) {
    	return taskDAO.deleteTask(taskId);
    }
    
    public boolean toggleTask(int taskId,String status) {
    	return taskDAO.toggleTask(taskId,status);
    }
}