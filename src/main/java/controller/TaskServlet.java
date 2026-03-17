package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import model.Task;
import service.TaskService;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private TaskService taskService = new TaskService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String taskIdStr = request.getParameter("taskId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

        boolean success = false;

       
        if(taskIdStr == null || taskIdStr.isEmpty()) {
            Task task = new Task(title, description);
            success = taskService.createTask(task);
        }
		
        else {
        	int taskId = Integer.parseInt(taskIdStr);
            success = taskService.toggleTask(taskId,status);
        }

        response.getWriter().write(success ? "success" : "failed");
    }
            
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int userId = 16;
        List<Task> tasks = taskService.getUserTasks(userId);

        response.setContentType("application/json");

        Gson gson = new Gson();
        String json = gson.toJson(tasks);  
        response.getWriter().write(json);
    }
    

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        
        Gson gson = new Gson();

        Task task = gson.fromJson(reader, Task.class);

        boolean success = false;

        if (task != null &&
            task.getTaskId() > 0 &&
            task.getTitle() != null &&
            task.getDescription() != null) {

            success = taskService.updateTask(task);
        }

        response.getWriter().write(success ? "success" : "failed");
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String taskIdStr = request.getParameter("taskId");

        boolean success = false;

        if (taskIdStr != null && !taskIdStr.isEmpty()) {
            int taskId = Integer.parseInt(taskIdStr);
            success = taskService.deleteTask(taskId);
        }

        response.getWriter().write(success ? "success" : "failed");
    }
    
}