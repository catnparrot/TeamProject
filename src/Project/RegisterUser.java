package Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUser {
	Connection connection;
	public boolean registerUser(User user) {
	    try {
	        String sql = "INSERT INTO user_data (name, password) VALUES (?, ?)";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, user.getName());
	        statement.setString(2, user.getPassword());
	        int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
