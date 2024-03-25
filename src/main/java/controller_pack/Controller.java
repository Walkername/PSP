package controller_pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLOutput;

public class Controller {
    @FXML
    private VBox algorithmContent;

    @FXML
    private Button firButton;

    @FXML
    private Button iirButton;

    @FXML
    private Button fftButton;

    @FXML
    private Button noiseButton;

    @FXML
    private Button modulaButton;

    @FXML
    private void clickFir(ActionEvent event) throws Exception {
        firButton.setDisable(true);
        iirButton.setDisable(false);
        fftButton.setDisable(false);
        noiseButton.setDisable(false);
        modulaButton.setDisable(false);

        AnchorPane content = FXMLLoader.load(getClass().getResource("fir.fxml"));
        algorithmContent.getChildren().clear();
        algorithmContent.getChildren().add(content);
    }



    @FXML
    private void clickIir(ActionEvent event) {
        firButton.setDisable(false);
        iirButton.setDisable(true);
        fftButton.setDisable(false);
        noiseButton.setDisable(false);
        modulaButton.setDisable(false);

        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void clickFft(ActionEvent event) {
        firButton.setDisable(false);
        iirButton.setDisable(false);
        fftButton.setDisable(true);
        noiseButton.setDisable(false);
        modulaButton.setDisable(false);

        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void clickNoise(ActionEvent event) {
        firButton.setDisable(false);
        iirButton.setDisable(false);
        fftButton.setDisable(false);
        noiseButton.setDisable(true);
        modulaButton.setDisable(false);

        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }

    @FXML
    private void clickModula(ActionEvent event) {
        firButton.setDisable(false);
        iirButton.setDisable(false);
        fftButton.setDisable(false);
        noiseButton.setDisable(false);
        modulaButton.setDisable(true);

        algorithmContent.getChildren().clear();
        Text text = new Text("Sorry, this algorithm is not finished yet!");
        algorithmContent.getChildren().add(text);
    }
}
