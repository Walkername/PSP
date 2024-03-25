package controller_pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ControllerFir {

    @FXML
    private AnchorPane window;

    @FXML
    private Button locationChooser;
    @FXML
    private TextField absolutePath;

    @FXML
    private TextField firOrder;
    @FXML
    private TextArea firArrayX;
    @FXML
    private TextArea firArrayB;

    @FXML
    private HBox firResult;

    @FXML
    private void generate(ActionEvent event) {
        createHexFile(absolutePath.getText());
        //getResult();
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

    private String convertToHex(String number, int width, int fraction) {
        StringBuilder hexNumber;

        StringBuilder strIntPart = new StringBuilder();
        StringBuilder strFractionPart = new StringBuilder();

        String[] intFraction = number.split("\\.");
        int intPart = Integer.parseInt(intFraction[0]);

        if (intFraction.length == 2) {
            // FRACTION CONVERSION
            float flNumber = Float.parseFloat("0." + number.split("\\.")[1]);

            float remainder = 1f;
            while (remainder != 0.0f) {
                flNumber *= 2.0f;
                strFractionPart.append(String.valueOf(flNumber).split("\\.")[0]);
                flNumber = Float.parseFloat("0." + String.valueOf(flNumber).split("\\.")[1]);
                remainder = flNumber;
                if (strFractionPart.length() >= 25) {
                    break;
                }
            }
        }

        int restBits = fraction - strFractionPart.length();
        strFractionPart.append("0".repeat(Math.max(0, restBits)));

        // INTEGER CONVERSION
        while(intPart > 0) {
            strIntPart.insert(0, intPart % 2);
            intPart /= 2;
        }

        restBits = width - fraction - strIntPart.length();
        for (int i = 0; i < restBits; i++) {
            strIntPart.insert(0, 0);
        }

        hexNumber = new StringBuilder(strIntPart.toString() + strFractionPart);

        String[] halfBytes = new String[8];
        for (int i = 0, k = 0; i < hexNumber.length() && k < 8; i += 4, k++) {
            halfBytes[k] = hexNumber.substring(i, i + 4);
        }

        hexNumber = new StringBuilder();
        for (String halfByte: halfBytes) {
            hexNumber.append(Integer.toHexString(Integer.parseInt(halfByte, 2)));
        }

        return hexNumber.toString();
    }

    @FXML
    private void createHexFile(String absolutePath) {
        try (FileWriter writer = new FileWriter(absolutePath + "\\arrayX.txt", false)) {
            String stringArrayX = firArrayX.getText();
            String[] numbersX = stringArrayX.split(" ");
            for (String numberX: numbersX) {
                writer.write(convertToHex(numberX, 32,25));
                writer.write("\n");
            }
            writer.flush();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try (FileWriter writer = new FileWriter(absolutePath + "\\arrayB.txt", false)) {
            String stringArrayB = firArrayB.getText();
            String[] numbersB = stringArrayB.split(" ");
            for (String numberB: numbersB) {
                writer.write(convertToHex(numberB, 32,25));
                writer.write("\n");
            }
            writer.flush();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void getResult() {
        Text result = new Text("Filter Order = " + firOrder.getText() + "\n"
                + "ArrayX: [" + firArrayX.getText() + "]\n"
                + "ArrayB: [" + firArrayB.getText() + "]");
        HBox containerResult = new HBox(result);
        firResult.getChildren().clear();
        firResult.getChildren().add(containerResult);
    }
}
