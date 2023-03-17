package app.workapp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    @FXML
    private ImageView detexLogo;
    @FXML
    private Button emailButton, partsButton, createButton, resetButton, addPartPaneButton,
            deletePartButton, addPartButton, tsCallsButton, closeButton, drawingButton;
    @FXML
    private Pane emailPane, partsPane, addPartPane, tsPane, fileFinderPane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField tsNum, refNum, siteNum, productText, partNumberText, partNameText, descriptionText;
    @FXML
    private TextArea notes;
    @FXML
    private TableView tableView, tsTableView;
    @FXML
    private ComboBox partsCombo;



    ObservableList<ObservableList> data;

    @FXML
    protected void loadImages() {
        Image logo = new Image(new File("src/main/resources/images/detexLogo-white.png").toURI().toString(),true);

        detexLogo.setImage(logo);
    }

    @FXML
    public void handleClicks(ActionEvent actionEvent) throws IOException, URISyntaxException, SQLException {
        if (actionEvent.getSource() == emailButton) {
            emailPane.toFront();
        }
        if (actionEvent.getSource() == partsButton) {
            partsPane.toFront();
        }
        if (actionEvent.getSource() == addPartPaneButton){
            resetAddPartPane();
            addPartPane.toFront();
        }
        if (actionEvent.getSource() == createButton) {
            email();
            addCall();
        }
        if (actionEvent.getSource() == resetButton) {
            refNum.setText("");
            siteNum.setText("");
            notes.setText("");
            tsNum.setText("");
        }
        if (actionEvent.getSource() == partsCombo){
            loadPartsClick();
        }
        if (actionEvent.getSource() == addPartButton){
            addPart();
            addProductsCombo();
        }
        if (actionEvent.getSource() == deletePartButton){
            delete();
        }
        if (actionEvent.getSource() == tsCallsButton){
            tsPane.toFront();
            loadCallsClick();
        }
        if (actionEvent.getSource() == closeButton){
            Platform.exit();
        }
        if (actionEvent.getSource() == drawingButton){
            fileFinderPane.toFront();
        }
        closeButton.toFront();
    }

    /****************************************************
                 Email Formatter Pane
     ****************************************************/

    void email() throws IOException, URISyntaxException {
        if(notes.getText().isEmpty() || refNum.getText().isEmpty() || siteNum.getText().isEmpty() || tsNum.getText().isEmpty() ) {
            Alert a1 = new Alert(Alert.AlertType.NONE,
                    "Error: One of the fields is empty", ButtonType.OK);

            // show the dialog
            a1.show();
        } else {
            String subjectLineText = "Call# " + refNum.getText() + " || Work Order " + tsNum.getText() + " || Site# " + siteNum.getText();
            URI msg = new URI("mailto", "TrueSource-Full" + "?" + "subject=" + subjectLineText + "&cc= " + "&" + "body=" + notes.getText(), (String) null);
            Desktop.getDesktop().mail(msg);
        }
    }

    /****************************************************
                    COMMON PARTS PANE
     ****************************************************/
    //loads data to table
    public void buildData(String dbName, String tableName, TableView table){

        Connection c ;
        data = FXCollections.observableArrayList();
        try{
            c = DriverManager.getConnection("jdbc:derby:" + dbName + ";create=true");
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from " + tableName + "";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            for(int i=0 ; i < rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
                //System.out.println("Column ["+i+"] ");
            }

            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    void addProductsCombo(){
        ArrayList<String> allProducts;

        partsDatabase commonParts = new partsDatabase("commonParts");

        allProducts = commonParts.readAllProducts();

        ObservableList<String> data = FXCollections.observableArrayList(allProducts);

        partsCombo.setItems(data);
    }

    void loadPartsClick(){
        String product = (String) partsCombo.getValue();

        tableView.getItems().clear();
        tableView.getColumns().clear();

        buildData("commonParts", product, tableView);
    }

    /****************************************************
                     Add Parts Pane
     ****************************************************/
    void addPart() throws SQLException {
        partsDatabase db = new partsDatabase("commonParts");

        if (!(db.tableExists(productText.getText()))){
            db.createTable(productText.getText());
        }

        String productName = productText.getText();
        String partNumber = partNumberText.getText();
        String partName = partNameText.getText();
        String description = descriptionText.getText();

        db.insertRow(productName, partNumber, partName, description);
    }

    void delete(){
        partsDatabase db = new partsDatabase("commonParts");
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Object selectedRow = tableView.getSelectionModel().getSelectedItem();
            String selectedString = selectedRow.toString();
            String[] selectedArray = selectedString.split("[ ,]");

            db.deleteRow(partsCombo.getValue().toString(), selectedArray[2]);
        }
        loadPartsClick();
    }

    void resetAddPartPane() {
        productText.setText("");
        partNumberText.setText("");
        partNameText.setText("");
        descriptionText.setText("");
    }
    /****************************************************
                    Calls Pane
     ****************************************************/
    void loadCallsClick(){
        tsTableView.getItems().clear();
        tsTableView.getColumns().clear();

        buildData("tsdb","calls", tsTableView);
    }

    void addCall() throws SQLException {
        tsDatabase db = new tsDatabase("tsdb");

        if(db.tableExists("calls")){
            db.createTable("calls");
        }

        String tableName = "calls";
        String referenceNum = refNum.getText();
        String trueSourceNum = tsNum.getText();
        String siteNumber = siteNum.getText();

        db.insertRow(tableName, referenceNum, trueSourceNum, siteNumber);
    }

    /****************************************************
                        Movable Window
     ****************************************************/
    private static double xOffset = 0;
    private static double yOffset = 0;

    @FXML
    void mousePressed(MouseEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    void mouseDragged(MouseEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /****************************************************
                     File Finder
     ****************************************************/


    @FXML
    protected void initialize() {
        closeButton.toFront();
        loadImages();
        addProductsCombo();
    }
}