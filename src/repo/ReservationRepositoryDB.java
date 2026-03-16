package repo;

import domain.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationRepositoryDB implements IRepository<Integer, Reservation> {
    protected String databaseConnectionUrlString;
    protected Connection activeDatabaseConnection = null;

    public ReservationRepositoryDB(String pathToDatabaseFile) {
        this.databaseConnectionUrlString = "jdbc:sqlite:" + pathToDatabaseFile;
    }

    public ReservationRepositoryDB() {

    }

    public void openConnection() {
        if (activeDatabaseConnection != null) {
            return;
        }
        try {
            this.activeDatabaseConnection = DriverManager.getConnection(databaseConnectionUrlString);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la deschiderea conexiunii: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (activeDatabaseConnection != null && !activeDatabaseConnection.isClosed()) {
                activeDatabaseConnection.close();
                activeDatabaseConnection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la închiderea conexiunii: " + e.getMessage());
        }
    }

    @Override
    public void add(Integer id, Reservation reservation) throws RepositoryException {
        openConnection();
        String sql = "INSERT INTO reservations (id, carId, customerName, startDate, endDate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getId());
            stmt.setInt(2, reservation.getCarId());
            stmt.setString(3, reservation.getCustomerName());
            stmt.setString(4, reservation.getStartDate().toString());
            stmt.setString(5, reservation.getEndDate().toString());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Failed to add reservation!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error adding reservation: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        openConnection();
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Reservation with id " + id + " not found!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting reservation: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void modify(Integer id, Reservation reservationWithNewDetails) throws RepositoryException {
        openConnection();
        String sql = "UPDATE reservations SET carId = ?, customerName = ?, startDate = ?, endDate = ? WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, reservationWithNewDetails.getCarId());
            stmt.setString(2, reservationWithNewDetails.getCustomerName());
            stmt.setString(3, reservationWithNewDetails.getStartDate().toString());
            stmt.setString(4, reservationWithNewDetails.getEndDate().toString());
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RepositoryException("Reservation with id " + id + " not found!");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error modifying reservation: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public Reservation findById(Integer id) throws RepositoryException {
        openConnection();
        String sql = "SELECT * FROM reservations WHERE id = ?";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractReservationFromResultSet(rs);
                } else {
                    throw new RepositoryException("Reservation with id " + id + " not found!");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding reservation: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public Iterable<Reservation> getAll() {
        openConnection();
        ArrayList<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (PreparedStatement stmt = activeDatabaseConnection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all reservations: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return reservations;
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int carId = rs.getInt("carId");
        String customerName = rs.getString("customerName");

        LocalDate startDate = LocalDate.parse(rs.getString("startDate"));
        LocalDate endDate = LocalDate.parse(rs.getString("endDate"));

        return new Reservation(id, carId, customerName, startDate, endDate);
    }
}