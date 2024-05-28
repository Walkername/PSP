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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        drawingArea.getChildren().clear();

        VBox vbox2 = (VBox) mainWindow.getChildren().getLast();
        vbox2.getChildren().clear();

        ObservableList<String> algorithms = FXCollections.observableArrayList(
                "FIR-filter", "FFT"
        );
        algorithmList = new ComboBox<>(algorithms);
        algorithmList.setPromptText("Select Filter Type");
        algorithmList.setValue("FIR-filter");

        Button button = new Button();
        button.setText("Add to Queue");
        button.setOnAction(this::addToWindow);

        drawingArea.setStyle("-fx-padding: 10 10 10 10");
        drawingArea.setPrefWidth(300);
        drawingArea.setAlignment(Pos.CENTER);
        drawingArea.setSpacing(15);
        drawingArea.setFillWidth(false);

        ScrollPane scrollPane = new ScrollPane(drawingArea);
        scrollPane.setPrefWidth(500);
        scrollPane.setMaxHeight(300);

        Button generateButton = new Button();
        generateButton.setText("Generate");

        vbox1.getChildren().addAll(algorithmList, button);
        vbox2.getChildren().addAll(scrollPane, generateButton);
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

            algorithmQueue.add(newAlgorithm);
            algorithmContent.getChildren().add(newAlgorithm.getImageBlock());

            newAlgorithm.getImageBlock().setOnMouseClicked(event1 -> {
                if (newAlgorithm.getImageBlock().getChildren().size() == 1) {
                    VBox vbox = new VBox();
                    HBox box = new HBox();

                    Button settingButton = new Button();
                    settingButton.setText("Settings");
                    settingButton.setOnMouseClicked(event2 -> newAlgorithm.getStage().show());

                    Button deleteButton = new Button();
                    deleteButton.setText("Delete");
                    deleteButton.setOnMouseClicked(event2 -> {
                        algorithmContent.getChildren().remove(newAlgorithm.getImageBlock());
                        algorithmQueue.remove(newAlgorithm);
                    });

                    Button closeButton = new Button();
                    closeButton.setText("X");
                    closeButton.setOnMouseClicked(event2 -> newAlgorithm.getImageBlock().getChildren().remove(vbox));

                    box.getChildren().addAll(deleteButton, closeButton);
                    vbox.getChildren().addAll(settingButton, box);
                    newAlgorithm.getImageBlock().getChildren().add(vbox);
                }
            });
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
