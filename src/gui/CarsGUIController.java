package gui;

import domain.Car;
import domain.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.CarService;
import service.ReservationService;
import gui.ReservationFilterType;
import gui.CarSortType;
import undo.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gui.CarSortType.PRICE_HIGH_TO_LOW;
import static gui.ReservationFilterType.*;

public class CarsGUIController {

    private CarService carService;
    private ReservationService reservationService;
    private UndoRedoManager<Integer, Car> carUndoRedoManager;
    private UndoRedoManager<Integer, Reservation> reservationUndoRedoManager;

    public CarsGUIController(CarService carService, ReservationService reservationService) {
        this.carService = carService;
        this.reservationService = reservationService;
        this.carUndoRedoManager = new UndoRedoManager<>();
        this.reservationUndoRedoManager = new UndoRedoManager<>();
    }

    @FXML
    private TableView<Car> tableViewForDisplayingCars;
    @FXML
    private TableColumn<Car, Integer> tableColumnForCarId;
    @FXML
    private TableColumn<Car, String> tableColumnForCarMake;
    @FXML
    private TableColumn<Car, String> tableColumnForCarModel;
    @FXML
    private TableColumn<Car, Double> tableColumnForCarRentalPrice;

    @FXML
    private TextField textFieldInputForCarId;
    @FXML
    private TextField textFieldInputForCarMake;
    @FXML
    private TextField textFieldInputForCarModel;
    @FXML
    private TextField textFieldInputForCarPrice;

    @FXML
    private TextField textFieldInputForFilteringCars;
    @FXML
    private ChoiceBox<CarFilterType> choiceBoxForSelectingCarFilterType;
    @FXML
    private ChoiceBox<CarSortType> choiceBoxForSelectingCarSortType;

    @FXML
    private Button buttonToAddCar;
    @FXML
    private Button buttonToModifyCar;
    @FXML
    private Button buttonToDeleteCar;
    @FXML
    private Button buttonToApplyCarFilter;
    @FXML
    private Button buttonToSortCars;
    @FXML
    private Button buttonToResetCarFilter;
    @FXML
    private Button buttonToUndoCarAction;
    @FXML
    private Button buttonToRedoCarAction;
    @FXML
    private Button buttonToExitCarTab;

    @FXML
    private TableView<Reservation> tableViewForDisplayingReservations;
    @FXML
    private TableColumn<Reservation, Integer> tableColumnForReservationId;
    @FXML
    private TableColumn<Reservation, Integer> tableColumnForReservationCarId;
    @FXML
    private TableColumn<Reservation, String> tableColumnForReservationCustomerName;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnForReservationStartDate;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnForReservationEndDate;

    @FXML
    private TextField textFieldInputForReservationId;
    @FXML
    private TextField textFieldInputForReservationCarId;
    @FXML
    private TextField textFieldInputForCustomerName;
    @FXML
    private TextField textFieldInputForStartDate;
    @FXML
    private TextField textFieldInputForEndDate;

    @FXML
    private TextField textFieldInputForFilteringReservations;
    @FXML
    private ChoiceBox<ReservationFilterType> choiceBoxForSelectingReservationFilterType;

    @FXML
    private Button buttonToAddReservation;
    @FXML
    private Button buttonToModifyReservation;
    @FXML
    private Button buttonToDeleteReservation;
    @FXML
    private Button buttonToApplyReservationFilter;
    @FXML
    private Button buttonToResetReservationFilter;
    @FXML
    private Button buttonToUndoReservationAction;
    @FXML
    private Button buttonToRedoReservationAction;
    @FXML
    private Button buttonToExitReservationTab;

    @FXML
    private ListView<Car> listView = new ListView<>();

    public void initialize() {
        this.setColumns();
        this.initializeChoiceBoxes();
        this.setupTableListeners();
        this.updateStateOfUndoRedoButtons();
        this.populateTables();


    }


    private void setColumns() {
        tableColumnForCarId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnForCarMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        tableColumnForCarModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnForCarRentalPrice.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));

        tableColumnForReservationId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnForReservationCarId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        tableColumnForReservationCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableColumnForReservationStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableColumnForReservationEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        Iterable<Car> iterableOfCars = carService.getAllCars();
        List<Car> listOfCars = new ArrayList<>();
        iterableOfCars.forEach(listOfCars::add);
        ObservableList<Car> observableListOfCars = FXCollections.observableArrayList(listOfCars);
        listView.setItems(observableListOfCars);
    }

    private void initializeChoiceBoxes() {
        choiceBoxForSelectingCarFilterType.getItems().addAll(CarFilterType.values());
        choiceBoxForSelectingCarFilterType.setValue(CarFilterType.MAKE);

        choiceBoxForSelectingCarSortType.getItems().addAll((CarSortType.values()));
        choiceBoxForSelectingCarSortType.setValue((PRICE_HIGH_TO_LOW));

        choiceBoxForSelectingReservationFilterType.getItems().addAll(ReservationFilterType.values());
        choiceBoxForSelectingReservationFilterType.setValue(CUSTOMER_NAME);
    }

    private void setupTableListeners() {
        tableViewForDisplayingCars.getSelectionModel().selectedItemProperty().addListener((observableValue, oldCarSelection, newCarSelection) -> {
            if (newCarSelection != null) {
                textFieldInputForCarId.setText(String.valueOf(newCarSelection.getId()));
                textFieldInputForCarMake.setText(newCarSelection.getMake());
                textFieldInputForCarModel.setText(newCarSelection.getModel());
                textFieldInputForCarPrice.setText(String.valueOf(newCarSelection.getRentalPrice()));
                textFieldInputForCarId.setDisable(true);
            } else {
                this.clearCarInputs();
            }
        });

        tableViewForDisplayingReservations.getSelectionModel().selectedItemProperty().addListener((observableValue, oldReservationSelection, newReservationSelection) -> {
            if (newReservationSelection != null) {
                textFieldInputForReservationId.setText(String.valueOf(newReservationSelection.getId()));
                textFieldInputForReservationCarId.setText(String.valueOf(newReservationSelection.getCarId()));
                textFieldInputForCustomerName.setText(newReservationSelection.getCustomerName());
                textFieldInputForStartDate.setText(newReservationSelection.getStartDate().toString());
                textFieldInputForEndDate.setText(newReservationSelection.getEndDate().toString());
                textFieldInputForReservationId.setDisable(true);
            } else {
                this.clearReservationInputs();
            }
        });
    }

    private void populateTables() {
        ArrayList<Car> listOfAllCarsFromService = new ArrayList<>();
        this.carService.getAllCars().forEach(listOfAllCarsFromService::add);
        this.tableViewForDisplayingCars.getItems().setAll(listOfAllCarsFromService);

        ArrayList<Reservation> listOfAllReservationsFromService = new ArrayList<>();
        this.reservationService.getAllReservations().forEach(listOfAllReservationsFromService::add);
        this.tableViewForDisplayingReservations.getItems().setAll(listOfAllReservationsFromService);
    }
    @FXML
    void carApplyFilterHandler(ActionEvent actionEvent) {
        String searchText = textFieldInputForFilteringCars.getText().trim();
        if (searchText.isEmpty()) {
            populateTables();
            return;
        }

        CarFilterType selectedFilterType = choiceBoxForSelectingCarFilterType.getValue();
        ArrayList<Car> filteredCarsList = new ArrayList<>();

        try {
            switch (selectedFilterType) {
                case MAKE:
                    carService.filterCarsByMake(searchText).forEach(filteredCarsList::add);
                    break;
                case MAX_PRICE:
                    double price = Double.parseDouble(searchText);
                    carService.filterCarsByRentalPrice(price).forEach(filteredCarsList::add);
                    break;
                case MODEL:
                    carService.getCarsOfAGivenModel(searchText).forEach(filteredCarsList::add);
                    break;
                case RENTED_BY_CUSTOMER:
                    filteredCarsList.addAll(reservationService.getAllCarsRentedByCustomer(searchText));
                    break;
            }
            tableViewForDisplayingCars.getItems().setAll(filteredCarsList);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid input: Please enter a valid number for price or car ID.");
        } catch (Exception e) {
            showErrorDialog("Filter Error: " + e.getMessage());
        }
    }

    // New: Handler for reservation apply filter button (restores apply button functionality)
    @FXML
    void reservationApplyFilterHandler(ActionEvent actionEvent) {
        String searchText = textFieldInputForFilteringReservations.getText().trim();
        if (searchText.isEmpty()) {
            populateTables();
            return;
        }

        ReservationFilterType selectedFilterType = choiceBoxForSelectingReservationFilterType.getValue();
        ArrayList<Reservation> filteredReservationsList = new ArrayList<>();

        try {
            switch (selectedFilterType) {
                case CUSTOMER_NAME:
                    reservationService.filterReservationByCustomerName(searchText).forEach(filteredReservationsList::add);
                    break;
                case CAR_ID:
                    int carId = Integer.parseInt(searchText);
                    reservationService.filterReservationByCarId(carId).forEach(filteredReservationsList::add);

                    List<String> customers = reservationService.getCustomerNameByCarId(carId);
                    if (customers.isEmpty()) {
                        showInfoDialog("No customers found for Car ID: " + carId);
                    } else {
                        String customerList = String.join("\n- ", customers);
                        showInfoDialog("Customers for Car ID " + carId + ":\n- " + customerList);
                    }
                    break;
                case ACTIVE_AT_DATE:
                    LocalDate date = LocalDate.parse(searchText);
                    filteredReservationsList.addAll(reservationService.getReservationsThatAreActiveAtACertainDate(date));
                    break;
            }
            tableViewForDisplayingReservations.getItems().setAll(filteredReservationsList);
        } catch (NumberFormatException | DateTimeParseException e) {
            showErrorDialog("Invalid input: Please enter a valid car ID, date (YYYY-MM-DD), or name.");
        } catch (Exception e) {
            showErrorDialog("Filter Error: " + e.getMessage());
        }
    }

    @FXML
    void carAddButtonHandler(ActionEvent event) {
        int newCarId = Integer.parseInt(this.textFieldInputForCarId.getText());
        String newCarMake = this.textFieldInputForCarMake.getText();
        String newCarModel = this.textFieldInputForCarModel.getText();
        double newCarRentalPrice = Double.parseDouble(this.textFieldInputForCarPrice.getText());

        try {
            Car newCarToBeAdded = new Car(newCarId, newCarMake, newCarModel, newCarRentalPrice);
            this.carService.addCar(newCarToBeAdded);
            this.carUndoRedoManager.executeAction(new ActionAdd<>(carService.getRepository(), newCarToBeAdded));
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearCarInputs();
        } catch (Exception addCarProcessException) {
            Alert errorAlertForAddCar = new Alert(Alert.AlertType.ERROR);
            errorAlertForAddCar.setContentText(addCarProcessException.getMessage());
            errorAlertForAddCar.showAndWait();
        }
    }

    @FXML
    void carModifyButtonHandler(ActionEvent actionEvent) {
        Car selectedCarFromTable = this.tableViewForDisplayingCars.getSelectionModel().getSelectedItem();
        if (selectedCarFromTable == null) {
            Alert selectionErrorAlert = new Alert(Alert.AlertType.ERROR);
            selectionErrorAlert.setContentText("Please select a car.");
            selectionErrorAlert.showAndWait();
            return;
        }
        try {
            String updatedMake = this.textFieldInputForCarMake.getText();
            String updatedModel = this.textFieldInputForCarModel.getText();
            double updatedRentalPrice = Double.parseDouble(this.textFieldInputForCarPrice.getText());
            Car carStateBeforeModification = new Car(
                    selectedCarFromTable.getId(),
                    selectedCarFromTable.getMake(),
                    selectedCarFromTable.getModel(),
                    selectedCarFromTable.getRentalPrice()
            );
            Car carStateAfterModification = new Car(
                    selectedCarFromTable.getId(),
                    updatedMake,
                    updatedModel,
                    updatedRentalPrice
            );
            this.carService.modifyCar(carStateAfterModification);
            this.carUndoRedoManager.executeAction(
                    new ActionUpdate<>(carService.getRepository(), carStateBeforeModification, carStateAfterModification));
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearCarInputs();

        } catch (Exception modificationProcessException) {
            Alert errorAlertForModifyCar = new Alert(Alert.AlertType.ERROR);
            errorAlertForModifyCar.setContentText(modificationProcessException.getMessage());
            errorAlertForModifyCar.showAndWait();
        }
    }

    @FXML
    void carDeleteButtonHandler(ActionEvent actionEvent) {
        Car carSelectedForDeletion = this.tableViewForDisplayingCars.getSelectionModel().getSelectedItem();
        if (carSelectedForDeletion == null) {
            Alert selectionErrorAlert = new Alert(Alert.AlertType.ERROR);
            selectionErrorAlert.setContentText("Please select a car.");
            selectionErrorAlert.showAndWait();
            return;
        }
        try {
            this.carService.deleteCar(carSelectedForDeletion.getId());
            this.carUndoRedoManager.executeAction(
                    new ActionRemove<>(carService.getRepository(), carSelectedForDeletion)
            );
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearCarInputs();
        } catch (Exception deletionProcessException) {
            Alert errorAlertForDeleteCar = new Alert(Alert.AlertType.ERROR);
            errorAlertForDeleteCar.setContentText(deletionProcessException.getMessage());
            errorAlertForDeleteCar.showAndWait();
        }
    }
    @FXML
    void carSortHandler(ActionEvent actionEvent) {
        CarSortType selectedSortingCriterion = this.choiceBoxForSelectingCarSortType.getValue();
        if (PRICE_HIGH_TO_LOW.equals(selectedSortingCriterion)) {
            try {
                List<Car> carsSortedByPriceDescending = this.carService.getCarsSortedDescendingByPrice();
                this.tableViewForDisplayingCars.getItems().setAll(carsSortedByPriceDescending);

            } catch (Exception sortingProcessException) {
                Alert errorAlertForSorting = new Alert(Alert.AlertType.ERROR);
                errorAlertForSorting.setContentText(sortingProcessException.getMessage());
                errorAlertForSorting.showAndWait();
            }
        }
    }

    @FXML
    void carResetHandler(ActionEvent actionEvent) {
        this.populateTables();
        this.textFieldInputForFilteringCars.clear();
    }


    @FXML
    void handleDeleteReservationButtonAction(ActionEvent actionEvent) {
        Reservation reservationSelectedForDeletion = this.tableViewForDisplayingReservations.getSelectionModel().getSelectedItem();
        if (reservationSelectedForDeletion == null) {
            Alert selectionErrorAlert = new Alert(Alert.AlertType.ERROR);
            selectionErrorAlert.setContentText("Please select a reservation.");
            selectionErrorAlert.showAndWait();
            return;
        }

        try {
            this.reservationService.deleteReservation(reservationSelectedForDeletion.getId());
            this.reservationUndoRedoManager.executeAction(
                    new ActionRemove<>(reservationService.getRepository(), reservationSelectedForDeletion)
            );
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearReservationInputs();

        } catch (Exception deletionProcessException) {
            Alert processingErrorAlert = new Alert(Alert.AlertType.ERROR);
            processingErrorAlert.setContentText(deletionProcessException.getMessage());
            processingErrorAlert.showAndWait();
        }
    }


    @FXML
    void reservationAddButtonHandler(ActionEvent actionEvent) {
        int newReservationId = Integer.parseInt(this.textFieldInputForReservationId.getText());
        int targetCarId = Integer.parseInt(this.textFieldInputForReservationCarId.getText());
        String customerName = this.textFieldInputForCustomerName.getText();
        LocalDate startDate = LocalDate.parse(this.textFieldInputForStartDate.getText());
        LocalDate endDate = LocalDate.parse(this.textFieldInputForEndDate.getText());

        try {
            Reservation newReservationToBeAdded = new Reservation(newReservationId, targetCarId, customerName, startDate, endDate);
            this.reservationService.addReservation(newReservationToBeAdded);
            this.reservationUndoRedoManager.executeAction(new ActionAdd<>(reservationService.getRepository(), newReservationToBeAdded));
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearReservationInputs();
        } catch (Exception addReservationProcessException) {
            Alert errorAlertForAddReservation = new Alert(Alert.AlertType.ERROR);
            errorAlertForAddReservation.setContentText(addReservationProcessException.getMessage());
            errorAlertForAddReservation.showAndWait();
        }
    }

    @FXML
    void reservationModifyButtonHandler(ActionEvent actionEvent) {
        Reservation selectedReservationFromTable = this.tableViewForDisplayingReservations.getSelectionModel().getSelectedItem();
        if (selectedReservationFromTable == null) {
            Alert selectionErrorAlert = new Alert(Alert.AlertType.ERROR);
            selectionErrorAlert.setContentText("Please select a reservation.");
            selectionErrorAlert.showAndWait();
            return;
        }

        try {
            int targetCarId = Integer.parseInt(this.textFieldInputForReservationCarId.getText());
            String customerName = this.textFieldInputForCustomerName.getText();
            LocalDate startDate = LocalDate.parse(this.textFieldInputForStartDate.getText());
            LocalDate endDate = LocalDate.parse(this.textFieldInputForEndDate.getText());

            Reservation reservationStateBeforeModification = new Reservation(selectedReservationFromTable.getId(), selectedReservationFromTable.getCarId(), selectedReservationFromTable.getCustomerName(), selectedReservationFromTable.getStartDate(), selectedReservationFromTable.getEndDate());
            Reservation reservationStateAfterModification = new Reservation(selectedReservationFromTable.getId(), targetCarId, customerName, startDate, endDate);

            this.reservationService.modifyReservation(reservationStateAfterModification);
            this.reservationUndoRedoManager.executeAction(new ActionUpdate<>(reservationService.getRepository(), reservationStateBeforeModification, reservationStateAfterModification));
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearReservationInputs();
        } catch (Exception modifyReservationProcessException) {
            Alert errorAlertForModifyReservation = new Alert(Alert.AlertType.ERROR);
            errorAlertForModifyReservation.setContentText(modifyReservationProcessException.getMessage());
            errorAlertForModifyReservation.showAndWait();
        }
    }

    @FXML
    void reservationDeleteButtonHandler(ActionEvent actionEvent) {
        Reservation reservationSelectedForDeletion = this.tableViewForDisplayingReservations.getSelectionModel().getSelectedItem();
        if (reservationSelectedForDeletion == null) {
            Alert selectionErrorAlert = new Alert(Alert.AlertType.ERROR);
            selectionErrorAlert.setContentText("Please select a reservation.");
            selectionErrorAlert.showAndWait();
            return;
        }

        try {
            this.reservationService.deleteReservation(reservationSelectedForDeletion.getId());
            this.reservationUndoRedoManager.executeAction(new ActionRemove<>(reservationService.getRepository(), reservationSelectedForDeletion));
            this.populateTables();
            this.updateStateOfUndoRedoButtons();
            this.clearReservationInputs();
        } catch (Exception deletionProcessException) {
            Alert errorAlertForDeleteReservation = new Alert(Alert.AlertType.ERROR);
            errorAlertForDeleteReservation.setContentText(deletionProcessException.getMessage());
            errorAlertForDeleteReservation.showAndWait();
        }
    }


    @FXML
    void reservationResetFilterButtonHandler(ActionEvent actionEvent) {
        this.populateTables();
        this.textFieldInputForFilteringReservations.clear();
    }

    @FXML
    void carUndoHandler(ActionEvent actionEvent) {
        this.carUndoRedoManager.undo();
        this.populateTables();
        this.updateStateOfUndoRedoButtons();
    }

    @FXML
    void carRedoHandler(ActionEvent actionEvent) {
        this.carUndoRedoManager.redo();
        this.populateTables();
        this.updateStateOfUndoRedoButtons();
    }

    @FXML
    void reservationUndoHandler(ActionEvent actionEvent) {
        this.reservationUndoRedoManager.undo();
        this.populateTables();
        this.updateStateOfUndoRedoButtons();
    }

    @FXML
    void reservationRedoHandler(ActionEvent actionEvent) {
        this.reservationUndoRedoManager.redo();
        this.populateTables();
        this.updateStateOfUndoRedoButtons();
    }

    @FXML
    void carExitButtonHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) this.textFieldInputForCarId.getScene().getWindow();
        stage.close();
    }

    @FXML
    void reservationExitButtonHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) this.textFieldInputForReservationId.getScene().getWindow();
        stage.close();
    }

    @FXML
    void carClearInputButtonHandler(ActionEvent actionEvent) {
        this.clearCarInputs();
    }

    @FXML
    void reservationClearInputButtonHandler(ActionEvent actionEvent) {
        this.clearReservationInputs();
    }

    private void clearCarInputs() {
        this.textFieldInputForCarId.clear();
        this.textFieldInputForCarMake.clear();
        this.textFieldInputForCarModel.clear();
        this.textFieldInputForCarPrice.clear();
        this.textFieldInputForCarId.setDisable(false);
        this.tableViewForDisplayingCars.getSelectionModel().clearSelection();
    }

    private void clearReservationInputs() {
        this.textFieldInputForReservationId.clear();
        this.textFieldInputForReservationCarId.clear();
        this.textFieldInputForCustomerName.clear();
        this.textFieldInputForStartDate.clear();
        this.textFieldInputForEndDate.clear();
        this.textFieldInputForReservationId.setDisable(false);
        this.tableViewForDisplayingReservations.getSelectionModel().clearSelection();
    }

    private void updateStateOfUndoRedoButtons() {
        this.buttonToUndoCarAction.setDisable(!this.carUndoRedoManager.canUndo());
        this.buttonToRedoCarAction.setDisable(!this.carUndoRedoManager.canRedo());
        this.buttonToUndoReservationAction.setDisable(!this.reservationUndoRedoManager.canUndo());
        this.buttonToRedoReservationAction.setDisable(!this.reservationUndoRedoManager.canRedo());
    }
    private void showInfoDialog(String message) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }

    private void showErrorDialog(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}