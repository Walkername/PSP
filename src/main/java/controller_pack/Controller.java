package controller_pack;

import algorithm_pack.PspAlgorithm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Queue;

public class Controller {

    private Queue<PspAlgorithm> algorithmQueue = new LinkedList<>();

    @FXML
    private VBox algorithmContent;

    @FXML
    private ComboBox<String> algorithmList;

    @FXML
    private void clickAlgoList(ActionEvent event) throws Exception {
        /*
        String algorithm = algorithmList.getValue();
        switch (algorithm) {
            case "FIR-filter" -> setFirWindow();
            case "IIR-filter" -> setIirWindow();
            case "FFT" -> setFftWindow();
            case "Noise filtration" -> setNoiseFiltration();
            case "Modulators" -> setModulaWindow();
        }

         */
    }

    @FXML
    private void addToWindow(ActionEvent event) {
        String algorithm = algorithmList.getValue();
        PspAlgorithm newAlgorithm = new PspAlgorithm(algorithm);
        addToQueue(newAlgorithm);
        algorithmContent.getChildren().add(newAlgorithm.getImageBlock());
    }

    private void addToQueue(PspAlgorithm algorithm) {
        algorithmQueue.add(algorithm);
    }

    @FXML
    private void deleteFromWindow(ActionEvent event) {

    }

    private void deleteFromQueue(ActionEvent event) {

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
