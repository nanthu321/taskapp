package controller;

import model.User;
import service.UserService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.google.gson.Gson;

import util.JWTUtil;



@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
    	String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User("", email, password);
        User loggedInUser = userService.loginUser(user);

        if (loggedInUser != null) {
            loggedInUser.setPassword(null);

           
            JWTUtil jwtUtil = new JWTUtil();
            String jwtToken = jwtUtil.generateToken(loggedInUser.getUserId() + "");

            Cookie cookie = new Cookie("token", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); 
            cookie.setMaxAge(7 * 24 * 60 * 60); // 1 week
            response.addCookie(cookie);

            response.setContentType("application/json");
            new Gson().toJson(loggedInUser);
            response.getWriter().write(new Gson().toJson(loggedInUser));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"fail\"}");
        }
    }
}
