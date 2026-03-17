package service;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.User;



public class UserService {

    private UserDAO userDAO = new UserDAO();
    
    
    public boolean registerUser(User user) {

        if(userDAO.emailExists(user.getEmail())) {
            return false;
        }
        
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
     
        return userDAO.registerUser(user);
    }
    
    
    public User loginUser(User user) {

        User dbUser = userDAO.getUserByEmail(user.getEmail());

        if (dbUser == null) {
            return null;
        }

        boolean isValid = BCrypt.checkpw(user.getPassword(), dbUser.getPassword());

        if (isValid) {
            return dbUser; 
        }

        return null;
    }
    
      
    
}