package com.caldwell.arclight.manager;

import com.caldwell.arclight.bodies.Star;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// ********************************************************************************** //
// Title: Arclight                                                                    //
// Author: Gabriel Caldwell                                                           //
// Course Section: CMIS201-ONL1 (Seidel) Fall 2022                                    //
// File: ManagerController.java                                                       //
// Description: Controller for manager.fxml                                           //
// ********************************************************************************** //

public class ManagerController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Manager manager;

    @FXML
    private TableView<Star> starTable;
    @FXML
    private TableColumn<Star, String> nameColumn;
    @FXML
    private TableColumn<Star, String> colorColumn;
    @FXML
    private TableColumn<Star, Double> distanceColumn;


    @FXML
    private Button menuButton;
    @FXML
    private Button journalButton;
    @FXML
    private RadioButton nameRadioButton;
    @FXML
    private RadioButton colorRadioButton;
    @FXML
    private RadioButton distanceRadioButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;

    @FXML
    private TextField nameField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField distanceField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Text sortByText;
    @FXML
    private Separator separator;
    @FXML
    private TextArea textArea;

    ObservableList<Star> starObservableList;

    final File starData = new File("stars.dat");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initializing manager...");

        // set up values for columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

        // read the stars from memory
        manager = new Manager();
        if (starData.exists()) {
            manager.readStars();
        }
        else if (!starData.exists()) {
            manager.writeStars();
        }

        // add items to list
        starObservableList = FXCollections.observableArrayList(manager.getStars());
        starTable.setItems(starObservableList);
    }

    // return to main menu
    @FXML
    public void onMainMenuButton(ActionEvent event) throws IOException {
        System.out.println("returning to the menu...");
        root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1125, 632);
        stage.setScene(scene);
        stage.show();
    }

    // switch to journal view
    @FXML
    public void onJournalButton(ActionEvent event) throws IOException {
        System.out.println("switching to journal mode...");
        root = FXMLLoader.load(getClass().getClassLoader().getResource("journal.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1125, 632);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onNameRadioButton() {
        // sort list by name then update table
        if (nameRadioButton.isSelected()) {
            // disable radio button when selected
            nameRadioButton.setDisable(true);
            // deselect other radio buttons
            colorRadioButton.setSelected(false);
            distanceRadioButton.setSelected(false);
            // enable other radio buttons
            colorRadioButton.setDisable(false);
            distanceRadioButton.setDisable(false);

            // quicksort the array by name then update table
            System.out.println("now sorting by name...");
            ArrayList<Star> sortedListName = manager.returnQuickSortString(manager.getStars());
            starObservableList.removeAll(sortedListName);
            starObservableList.addAll(sortedListName);
            starTable.setItems(starObservableList);
        }
    }

    @FXML
    public void onColorRadioButton() {
        // sort list by color then update table
        if (colorRadioButton.isSelected()) {
            // disable radio button when selected
            colorRadioButton.setDisable(true);
            // deselect other radio buttons
            nameRadioButton.setSelected(false);
            distanceRadioButton.setSelected(false);
            // enable other radio buttons
            distanceRadioButton.setDisable(false);
            nameRadioButton.setDisable(false);

            // quicksort the array by color then update table
            System.out.println("now sorting by color...");
            ArrayList<Star> sortedListColor = manager.returnQuickSortColor(manager.getStars());
            starObservableList.removeAll(sortedListColor);
            starObservableList.addAll(sortedListColor);
            starTable.setItems(starObservableList);
        }
    }

    @FXML
    public void onDistanceRadioButton() {
        if (distanceRadioButton.isSelected()) {
            // disable radio button when selected
            distanceRadioButton.setDisable(true);
            // deselect other radio buttons
            colorRadioButton.setSelected(false);
            nameRadioButton.setSelected(false);
            // enable other radio buttons
            colorRadioButton.setDisable(false);
            nameRadioButton.setDisable(false);

            // quicksort the array by distance then update table
            System.out.println("now sorting by distance...");
            ArrayList<Star> sortedListDistance = manager.returnQuickSort(manager.getStars());
            starObservableList.removeAll(sortedListDistance);
            starObservableList.addAll(sortedListDistance);
            starTable.setItems(starObservableList);
        }
    }

    @FXML
    public void onDeleteButton() {
        if (starTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println("deleting: " + starTable.getSelectionModel().getSelectedItem().getName());
            manager.stars.remove(starTable.getSelectionModel().getSelectedItem());
            manager.writeStars();
            manager.starStack.push(starTable.getSelectionModel().getSelectedItem());
            starObservableList.remove(starTable.getSelectionModel().getSelectedItem());
            starTable.setItems(starObservableList);
        }
    }

    @FXML
    public void onUndoButton() {
        if (!manager.starStack.isEmpty()) {
            Star temp = manager.starStack.pop();
            manager.stars.add(temp);
            manager.writeStars();
            starObservableList.add(temp);
            if (nameRadioButton.isSelected()) {
                starTable.setItems(starObservableList);
                onNameRadioButton();
            }
            else if (colorRadioButton.isSelected()) {
                starTable.setItems(starObservableList);
                onColorRadioButton();
            }
            else if (distanceRadioButton.isSelected()) {
                starTable.setItems(starObservableList);
                onDistanceRadioButton();
            }
            System.out.println("undoing deletion of: " + temp.getName());
        }
    }

    public void onAddButton() {
        menuButton.setDisable(true);
        menuButton.setOpacity(0);
        journalButton.setDisable(true);
        journalButton.setOpacity(0);
        undoButton.setDisable(true);
        undoButton.setOpacity(0);
        addButton.setDisable(true);
        addButton.setOpacity(0);
        deleteButton.setDisable(true);
        deleteButton.setOpacity(0);
        sortByText.setOpacity(0);
        separator.setOpacity(0);
        nameRadioButton.setDisable(true);
        nameRadioButton.setOpacity(0);
        colorRadioButton.setDisable(true);
        colorRadioButton.setOpacity(0);
        distanceRadioButton.setDisable(true);
        distanceRadioButton.setOpacity(0);
        textArea.setDisable(true);
        textArea.setOpacity(0);
        nameField.setDisable(false);
        nameField.setOpacity(1);
        colorField.setDisable(false);
        colorField.setOpacity(1);
        distanceField.setDisable(false);
        distanceField.setOpacity(1);
        confirmButton.setDisable(false);
        confirmButton.setOpacity(1);
        cancelButton.setDisable(false);
        cancelButton.setOpacity(1);
    }

    public void onConfirmButton() {
        if (nameField.getText() != null && colorField.getText() != null && distanceField.getText() != null) {
            if (distanceField.getText().matches(".*[0-9].*")) {
                Star temp = new Star(nameField.getText(), colorField.getText(), Integer.parseInt(distanceField.getText()));
                starObservableList.add(temp);
                starTable.setItems(starObservableList);
                manager.stars.add(temp);
                manager.writeStars();
                onCancelButton();
            }
        }
    }

    public void onCancelButton() {
        menuButton.setDisable(false);
        menuButton.setOpacity(1);
        journalButton.setDisable(false);
        journalButton.setOpacity(1);
        undoButton.setDisable(false);
        undoButton.setOpacity(1);
        addButton.setDisable(false);
        addButton.setOpacity(1);
        deleteButton.setDisable(false);
        deleteButton.setOpacity(1);
        sortByText.setOpacity(1);
        separator.setOpacity(1);
        nameRadioButton.setDisable(false);
        nameRadioButton.setOpacity(1);
        colorRadioButton.setDisable(false);
        colorRadioButton.setOpacity(1);
        distanceRadioButton.setDisable(false);
        distanceRadioButton.setOpacity(1);
        textArea.setDisable(false);
        textArea.setOpacity(1);
        nameField.setDisable(true);
        nameField.setOpacity(0);
        colorField.setDisable(true);
        colorField.setOpacity(0);
        distanceField.setDisable(true);
        distanceField.setOpacity(0);
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        cancelButton.setDisable(true);
        cancelButton.setOpacity(0);
    }
}
