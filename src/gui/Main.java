package gui;

import domain.Car;
import domain.Reservation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repo.CarRepositoryDB;
import repo.IRepository;
import repo.ReservationRepositoryDB;
import service.CarService;
import service.ReservationService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        String dbPath = "car_rental.db";

        IRepository<Integer, Car> carRepository = new CarRepositoryDB(dbPath);
        IRepository<Integer, Reservation> reservationRepository = new ReservationRepositoryDB(dbPath);

        CarService carService = new CarService(carRepository);
        ReservationService reservationService = new ReservationService(reservationRepository, carRepository);

        CarsGUIController carsController = new CarsGUIController(carService, reservationService);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/CarsGUI.fxml"));

        fxmlLoader.setController(carsController);

        Scene mainScene = new Scene(fxmlLoader.load());
        mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Car Rental Management System");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}