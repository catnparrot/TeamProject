package Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(Product product) {
        try {
            String sql = "INSERT INTO product (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(String name, int quantity) {
        try {
            String sql = "UPDATE product SET quantity = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductByName(String name) {
        try {
            String sql = "SELECT * FROM product WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                return new Product(name, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

	public Product[] getAllProducts() {
		return null;
	}
}
