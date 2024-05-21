package controller_pack;

import algorithm_pack.FFT;
import algorithm_pack.FIRFilter;
import algorithm_pack.PspAlgorithm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

        VBox vbox = (VBox) mainWindow.getChildren().getFirst();
        vbox.getChildren().clear();
        algorithmContent.getChildren().clear();

        Button firButton = new Button();
        firButton.setText("FIR-filter");
        firButton.setMaxWidth(Double.MAX_VALUE);
        firButton.setOnAction(event1 -> {
            try {
                setFirWindow();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Button iirButton = new Button();
        iirButton.setText("IIR-filter");
        iirButton.setMaxWidth(Double.MAX_VALUE);

        Button fftButton = new Button();
        fftButton.setText("FFT");
        fftButton.setMaxWidth(Double.MAX_VALUE);

        Button noiseButton = new Button();
        noiseButton.setText("Noise Filtration");
        noiseButton.setMaxWidth(Double.MAX_VALUE);

        Button modulaButton = new Button();
        modulaButton.setText("Modulars");
        modulaButton.setMaxWidth(Double.MAX_VALUE);

        vbox.getChildren().addAll(firButton, iirButton, fftButton, noiseButton, modulaButton);
    }

    @FXML
    private void changeToUserMode() {
        modeChooser.setText("Change to Test mode");

        VBox vbox = (VBox) mainWindow.getChildren().getFirst();
        vbox.getChildren().clear();
        algorithmContent.getChildren().clear();

        ObservableList<String> algorithms = FXCollections.observableArrayList(
                "FIR-filter", "FFT"
        );
        algorithmList = new ComboBox<>(algorithms);
        algorithmList.setPromptText("Select Filter Type");
        algorithmList.setValue("FIR-filter");

        Button button = new Button();
        button.setText("Add to Queue");
        button.setOnAction(this::addToWindow);

        vbox.getChildren().addAll(algorithmList, button);
    }

    @FXML
    private void addToWindow(ActionEvent event) {
        String algorithm = algorithmList.getValue();
        if (algorithm != null) {
            PspAlgorithm newAlgorithm;
            switch (algorithm) {
                case "FIR-filter" -> newAlgorithm = new FIRFilter();
                case "FFT" -> newAlgorithm = new FFT();
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
                    settingButton.setOnMouseClicked(event2 -> {
                        newAlgorithm.getStage().show();
                    });

                    Button deleteButton = new Button();
                    deleteButton.setText("Delete");
                    deleteButton.setOnMouseClicked(event2 -> {
                        algorithmContent.getChildren().remove(newAlgorithm.getImageBlock());
                        algorithmQueue.remove(newAlgorithm);
                    });

                    Button closeButton = new Button();
                    closeButton.setText("X");
                    closeButton.setOnMouseClicked(event2 -> {
                        newAlgorithm.getImageBlock().getChildren().remove(vbox);

                    });

                    box.getChildren().addAll(deleteButton, closeButton);
                    vbox.getChildren().addAll(settingButton, box);
                    newAlgorithm.getImageBlock().getChildren().add(vbox);
                }
            });
        }
    }

    @FXML
    private void setFirWindow() throws Exception {
        AnchorPane content = FXMLLoader.load(getClass().getResource("fir.fxml"));
        algorithmContent.getChildren().clear();
        algorithmContent.getChildren().add(content);
    }

    @FXML
    private void setIirWindow() {
        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void setFftWindow() {
        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void setNoiseFiltration() {
        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void setModulaWindow() {
        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }
}
