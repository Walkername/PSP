package algorithm_pack;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils_pack.PSPUtils;

import java.io.File;

public class FFT extends PspAlgorithm {

    private String[] signalRXArray;
    private String[] signalQXArray;
    private String[] coeffsArray;

    public FFT() {
        super("FFT");
        Parent settingInterface = generateInterface();
        Scene scene = new Scene(settingInterface);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Algorithm settings");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.setResizable(false);
        super.setStage(stage);
    }

    public String[] getSignalRXArray() {
        return signalRXArray;
    }

    public String[] getSignalQXArray() { return signalQXArray; }

    public String[] getCoeffsArray() {
        return coeffsArray;
    }

    private Parent generateInterface() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10 10 10 10;");

        // FILTER SIGNAL RX
        VBox signalRXBox = new VBox();
        signalRXBox.setSpacing(5);
        ToggleGroup signalRXToggleGroup = new ToggleGroup();

        VBox titleRXBox = new VBox();
        Text titleSignalRX = new Text("Signal Real Part:");
        Text exampleSignalRX = new Text("Example (numbers with space, fraction is dot): 0.5 1 0.5 1");
        titleRXBox.getChildren().addAll(titleSignalRX, exampleSignalRX);

        TextField fieldSignalRX = new TextField();
        TextField pathSignalRX = new TextField();
        pathSignalRX.setDisable(true);
        Button fileChooserSignalRX = new Button("...");
        fileChooserSignalRX.setDisable(true);
        fileChooserSignalRX.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) fileChooserSignalRX.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                pathSignalRX.setText(selectedFile.getAbsolutePath());
            }
        });

        RadioButton signalRXBtnText = new RadioButton("Type:");
        signalRXBtnText.setToggleGroup(signalRXToggleGroup);
        signalRXBtnText.fire();
        signalRXBtnText.setOnAction(event -> {
            fieldSignalRX.setDisable(false);
            pathSignalRX.setDisable(true);
            fileChooserSignalRX.setDisable(true);
        });

        RadioButton signalRXBtnFile = new RadioButton("Use file:");
        signalRXBtnFile.setToggleGroup(signalRXToggleGroup);
        signalRXBtnFile.setOnAction(event -> {
            fieldSignalRX.setDisable(true);
            pathSignalRX.setDisable(false);
            fileChooserSignalRX.setDisable(false);
        });

        HBox signalRXFileBox = new HBox();
        signalRXFileBox.setSpacing(5);
        signalRXFileBox.getChildren().addAll(signalRXBtnFile, pathSignalRX, fileChooserSignalRX);

        signalRXBox.getChildren().addAll(titleRXBox, signalRXBtnText, fieldSignalRX, signalRXFileBox);

        // FILTER SIGNAL QX
        VBox signalQXBox = new VBox();
        signalQXBox.setSpacing(5);

        ToggleGroup signalQXToggleGroup = new ToggleGroup();

        VBox titleQXBox = new VBox();
        Text titleSignalQX = new Text("Signal Imaginary Part:");
        titleQXBox.getChildren().addAll(titleSignalQX);

        TextField fieldSignalQX = new TextField();
        TextField pathSignalQX = new TextField();
        pathSignalQX.setDisable(true);
        Button fileChooserSignalQX = new Button("...");
        fileChooserSignalQX.setDisable(true);
        fileChooserSignalQX.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) fileChooserSignalQX.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                pathSignalQX.setText(selectedFile.getAbsolutePath());
            }
        });

        RadioButton signalQXBtnText = new RadioButton("Type:");
        signalQXBtnText.setToggleGroup(signalQXToggleGroup);
        signalQXBtnText.fire();
        signalQXBtnText.setOnAction(event -> {
            fieldSignalQX.setDisable(false);
            pathSignalQX.setDisable(true);
            fileChooserSignalQX.setDisable(true);
        });

        RadioButton signalQXBtnFile = new RadioButton("Use file:");
        signalQXBtnFile.setToggleGroup(signalQXToggleGroup);
        signalQXBtnFile.setOnAction(event -> {
            fieldSignalQX.setDisable(true);
            pathSignalQX.setDisable(false);
            fileChooserSignalQX.setDisable(false);
        });

        HBox signalQXFileBox = new HBox();
        signalQXFileBox.setSpacing(5);
        signalQXFileBox.getChildren().addAll(signalQXBtnFile, pathSignalQX, fileChooserSignalQX);

        signalQXBox.getChildren().addAll(titleQXBox, signalQXBtnText, fieldSignalQX, signalQXFileBox);

        // FILTER COEFFS
        VBox coeffsBox = new VBox();
        coeffsBox.setSpacing(5);

        ToggleGroup coeffsToggleGroup = new ToggleGroup();

        VBox titleCoeffsBox = new VBox();
        Text titleCoeffs = new Text("Coefficients:");
        titleCoeffsBox.getChildren().addAll(titleCoeffs);

        TextField fieldCoeffs = new TextField();
        TextField pathCoeffs = new TextField();
        pathCoeffs.setDisable(true);
        Button fileChooserCoeffs = new Button("...");
        fileChooserCoeffs.setDisable(true);
        fileChooserCoeffs.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) fileChooserCoeffs.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                pathCoeffs.setText(selectedFile.getAbsolutePath());
            }
        });

        RadioButton coeffsBtnText = new RadioButton("Type:");
        coeffsBtnText.setToggleGroup(coeffsToggleGroup);
        coeffsBtnText.fire();
        coeffsBtnText.setOnAction(event -> {
            fieldCoeffs.setDisable(false);
            pathCoeffs.setDisable(true);
            fileChooserCoeffs.setDisable(true);
        });

        RadioButton CoeffsBtnFile = new RadioButton("Use file:");
        CoeffsBtnFile.setToggleGroup(coeffsToggleGroup);
        CoeffsBtnFile.setOnAction(event -> {
            fieldCoeffs.setDisable(true);
            pathCoeffs.setDisable(false);
            fileChooserCoeffs.setDisable(false);
        });

        HBox CoeffsFileBox = new HBox();
        CoeffsFileBox.setSpacing(5);
        CoeffsFileBox.getChildren().addAll(CoeffsBtnFile, pathCoeffs, fileChooserCoeffs);

        coeffsBox.getChildren().addAll(titleCoeffsBox, coeffsBtnText, fieldCoeffs, CoeffsFileBox);

        // SAVE
        HBox generateHBox = new HBox();
        Button generateButton = new Button();
        generateButton.setText("Save");
        generateButton.setOnMouseClicked(event -> {

            RadioButton selectedRXButton = (RadioButton) signalRXToggleGroup.getSelectedToggle();
            if (selectedRXButton.equals(signalRXBtnText)) {
                this.signalRXArray = fieldSignalRX.getText().split(" ");
            }
            else {
                this.signalRXArray = PSPUtils.readFile(pathSignalRX.getText());
            }

            RadioButton selectedQXButton = (RadioButton) signalQXToggleGroup.getSelectedToggle();
            if (selectedQXButton.equals(signalQXBtnText)) {
                this.signalQXArray = fieldSignalQX.getText().split(" ");
            }
            else {
                this.signalQXArray = PSPUtils.readFile(pathSignalQX.getText());
            }

            RadioButton selectedCoeffsButton = (RadioButton) coeffsToggleGroup.getSelectedToggle();
            if (selectedCoeffsButton.equals(coeffsBtnText)) {
                this.coeffsArray = fieldCoeffs.getText().split(" ");
            }
            else {
                this.coeffsArray = PSPUtils.readFile(pathCoeffs.getText());
            }

            super.getStage().close();
        });
        generateHBox.getChildren().add(generateButton);
        generateHBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(signalRXBox, signalQXBox, coeffsBox, generateHBox);

        return vbox;
    }
}
