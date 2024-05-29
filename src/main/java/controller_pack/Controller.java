package controller_pack;

import algorithm_pack.FFT;
import algorithm_pack.FIRFilter;
import algorithm_pack.Modulus;
import algorithm_pack.PspAlgorithm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils_pack.PSPUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Controller {

    private Queue<PspAlgorithm> algorithmQueue = new LinkedList<>();

    @FXML
    private ToggleButton modeChooser;

    @FXML
    private HBox mainWindow;

    @FXML
    private VBox algorithmContent;

    @FXML
    private ComboBox<String> algorithmList;

    @FXML
    private TextField absolutePath;

    @FXML
    private Button locationChooser;

    @FXML
    private void generate(ActionEvent event) {
        String programText = "";
        PSPUtils.createHexFile(programText, absolutePath.getText());
        System.out.println("You generated!");
    }

    @FXML
    private void chooseLocation(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) locationChooser.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            absolutePath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void changeMode(ActionEvent event) {
        if (modeChooser.isSelected()) {
            changeToTestMode();
        } else {
            changeToUserMode();
        }
    }

    @FXML
    private void changeToTestMode() {
        modeChooser.setText("Change to User mode");

        VBox vbox1 = (VBox) mainWindow.getChildren().getFirst();
        vbox1.getChildren().clear();

        VBox vbox2 = (VBox) mainWindow.getChildren().getLast();
        vbox2.getChildren().clear();
        //algorithmContent.getChildren().clear();

        Button firButton = new Button();
        firButton.setText("FIR-filter");
        firButton.setMaxWidth(Double.MAX_VALUE);
        firButton.setOnAction(event -> {
            try {
                setFirWindow(vbox2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Button fftButton = new Button();
        fftButton.setText("FFT");
        fftButton.setMaxWidth(Double.MAX_VALUE);
        fftButton.setOnAction(event -> {
            try {
                setFftWindow(vbox2);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        Button modulusButton = new Button();
        modulusButton.setText("Modulus");
        modulusButton.setMaxWidth(Double.MAX_VALUE);
        modulusButton.setOnAction(event -> {
            try {
                setModulusWindow(vbox2);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        vbox1.getChildren().addAll(firButton, fftButton, modulusButton);
    }

    @FXML
    private void changeToUserMode() {
        modeChooser.setText("Change to Test mode");

        VBox vbox1 = (VBox) mainWindow.getChildren().getFirst();
        vbox1.getChildren().clear();

        VBox drawingArea = algorithmContent;
        //drawingArea.getChildren().clear();

        VBox vbox2 = (VBox) mainWindow.getChildren().getLast();
        vbox2.getChildren().clear();

        ObservableList<String> algorithms = FXCollections.observableArrayList(
                "FIR-filter", "FFT", "Modulus"
        );
        algorithmList = new ComboBox<>(algorithms);
        algorithmList.setPromptText("Select Filter Type");
        algorithmList.setValue("FIR-filter");

        Button button = new Button();
        button.setText("Add to Queue");
        button.setOnAction(this::addToWindow);

        drawingArea.setStyle("-fx-padding: 10 10 10 10");
        drawingArea.setPrefWidth(480);
        drawingArea.setAlignment(Pos.CENTER);
        drawingArea.setSpacing(15);

        ScrollPane scrollPane = new ScrollPane(drawingArea);
        scrollPane.setPrefWidth(500);
        scrollPane.setMaxHeight(330);

        HBox hBox = new HBox();
        hBox.setSpacing(5);
        TextField textField = absolutePath;
        Button locButton = locationChooser;
        locButton.setOnAction(this::chooseLocation);
        hBox.getChildren().addAll(textField, locButton);
        Button generateButton = new Button();
        generateButton.setText("Generate");
        generateButton.setOnAction(this::generate);

        vbox1.getChildren().addAll(algorithmList, button);
        vbox2.getChildren().addAll(scrollPane, hBox, generateButton);
    }

    @FXML
    private void addToWindow(ActionEvent event) {
        String algorithm = algorithmList.getValue();
        if (algorithm != null) {
            PspAlgorithm newAlgorithm;
            switch (algorithm) {
                case "FIR-filter" -> newAlgorithm = new FIRFilter();
                case "FFT" -> newAlgorithm = new FFT();
                case "Modulus" -> newAlgorithm = new Modulus();
                default -> newAlgorithm = new FIRFilter();
            }

            HBox panelAlgorithm = new HBox();
            panelAlgorithm.setAlignment(Pos.BASELINE_LEFT);
            panelAlgorithm.setSpacing(30);
            panelAlgorithm.setStyle("-fx-padding: 10 10 10 10; -fx-border-color: black;");
            Text algorithmNumber = new Text(String.valueOf(algorithmQueue.size() + 1));

            Button settingButton = new Button();
            settingButton.setText("Settings");
            settingButton.setOnMouseClicked(event1 -> newAlgorithm.getStage().show());

            Button deleteButton = new Button();
            deleteButton.setText("Delete");
            deleteButton.setOnMouseClicked(event2 -> {
                int number = Integer.parseInt(algorithmNumber.getText());
                for (int i = number; i < algorithmContent.getChildren().size(); i++) {
                    HBox hbox = (HBox) algorithmContent.getChildren().get(i);
                    Text algNumber = (Text) hbox.getChildren().getFirst();
                    algNumber.setText(String.valueOf(i));
                }

                algorithmContent.getChildren().remove(panelAlgorithm);
                algorithmQueue.remove(newAlgorithm);
            });

            HBox.setHgrow(algorithmNumber, Priority.ALWAYS);
            HBox.setHgrow(settingButton, Priority.ALWAYS);
            HBox.setHgrow(newAlgorithm.getImageBlock(), Priority.ALWAYS);
            HBox.setHgrow(deleteButton, Priority.ALWAYS);
            panelAlgorithm.getChildren().addAll(algorithmNumber, settingButton, newAlgorithm.getImageBlock(), deleteButton);

            algorithmQueue.add(newAlgorithm);
            algorithmContent.getChildren().add(panelAlgorithm);

        }
    }

    @FXML
    private void setFirWindow(VBox vbox) throws Exception {
        AnchorPane content = FXMLLoader.load(getClass().getResource("fir.fxml"));
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setPrefWidth(520);
        scrollPane.setMaxHeight(360);
        vbox.getChildren().clear();
        vbox.getChildren().add(scrollPane);
    }

    @FXML
    private void setFftWindow(VBox vbox) throws IOException {
        AnchorPane content = FXMLLoader.load(getClass().getResource("fft.fxml"));
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setPrefWidth(520);
        scrollPane.setMaxHeight(360);
        vbox.getChildren().clear();
        vbox.getChildren().add(scrollPane);
    }

    @FXML
    private void setModulusWindow(VBox vbox) throws IOException {
        AnchorPane content = FXMLLoader.load(getClass().getResource("modulus.fxml"));
        content.setStyle("-fx-padding: 10 10 10 10;");
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setPrefWidth(520);
        scrollPane.setMaxHeight(360);
        vbox.getChildren().clear();
        vbox.getChildren().add(scrollPane);
    }
}
