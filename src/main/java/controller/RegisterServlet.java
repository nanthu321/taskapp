package controller;

import dao.UserDAO;
import service.UserService;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
       

        User user = new User(username, email, password);

        boolean registered = userService.registerUser(user);
        
        response.setContentType("text/plain");

        if(registered){
            response.getWriter().write("success");
        }else{
            response.getWriter().write("email_exists");
        }
        
    }
}


