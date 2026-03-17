package dao;

import model.Task;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public boolean addTask(Task task){

        String sql = "INSERT INTO tasks(title,description,user_id) VALUES(?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,task.getTitle());
            ps.setString(2,task.getDescription());
            ps.setInt(3, 16);

            int rows = ps.executeUpdate();

            return rows > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    
    public List<Task> getUserTasks(int userId){

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT task_id,title,description,status,created_at FROM tasks WHERE user_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Task t = new Task();

                t.setTaskId(rs.getInt("task_id"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                t.setCreatedAt(rs.getString("created_at"));

                tasks.add(t);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return tasks;
    }
    
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET title=?, description=? WHERE task_id=?";
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getTaskId());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public boolean deleteTask(int taskId) {
    	String sql = "DELETE FROM tasks WHERE task_id = ?";
    	 try(Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, taskId);
             int rows = ps.executeUpdate();
             return rows > 0;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return false;
    	
    }
    
    public boolean toggleTask(int taskId,String status) {
    	String sql = "UPDATE tasks SET status=? WHERE task_id=?";
   	 	try(Connection conn = DBConnection.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql)) {
   	 		ps.setString(1, status);
   	 		ps.setInt(2, taskId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}