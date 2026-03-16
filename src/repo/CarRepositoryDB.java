package repo;

import domain.Car;
import java.sql.*;
import java.util.ArrayList;

public class CarRepositoryDB implements IRepository<Integer, Car> {
    protected String databaseConnectionUrlString;
    protected Connection activeDatabaseConnection = null;


    public CarRepositoryDB(String pathToDatabaseFile) {
        this.databaseConnectionUrlString = "jdbc:sqlite:" + pathToDatabaseFile;
    }

    public void openConnection() {
        if (activeDatabaseConnection != null) {
            return;
        }
        try {
            this.activeDatabaseConnection = DriverManager.getConnection(databaseConnectionUrlString);
        } catch (SQLException e) {
            throw new RuntimeException("Error at open connection: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (activeDatabaseConnection != null && !activeDatabaseConnection.isClosed()) {
                activeDatabaseConnection.close();
                activeDatabaseConnection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error at closing connection: " + e.getMessage());
        }
    }

    @Override
    public void add(Integer id, Car car) throws RepositoryException {
        openConnection();
        String sql = "INSERT INTO cars (id, make, model, rental_price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, car.getId());
            stmt.setString(2, car.getMake());
            stmt.setString(3, car.getModel());
            stmt.setDouble(4, car.getRentalPrice());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Failed to add car!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error adding car: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        openConnection();
        String sql = "DELETE FROM cars WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Car with id " + id + " not found!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting car: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void modify(Integer id, Car carWithNewDetails) throws RepositoryException {
        openConnection();
        String sql = "UPDATE cars SET make = ?, model = ?, rental_price = ? WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setString(1, carWithNewDetails.getMake());
            stmt.setString(2, carWithNewDetails.getModel());
            stmt.setDouble(3, carWithNewDetails.getRentalPrice());
            stmt.setInt(4, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Car with id " + id + " not found!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error modifying car: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public Car findById(Integer id) throws RepositoryException {
        openConnection();
        String sql = "SELECT * FROM cars WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCarFromResultSet(rs);
                } else {
                    throw new RepositoryException("Car with id " + id + " not found!");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding car: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public Iterable<Car> getAll() {
        openConnection();
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all cars: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return cars;
    }

    private Car extractCarFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String make = rs.getString("make");
        String model = rs.getString("model");
        double rentalPrice = rs.getDouble("rental_price");

        return new Car(id, make, model, rentalPrice);
    }
}