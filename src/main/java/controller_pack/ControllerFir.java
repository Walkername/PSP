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

    private String toTwosCompliment(String bin) {
        StringBuilder twos = new StringBuilder();

        // One's complement
        for (int i = 0; i < bin.length(); i++) {
            if (bin.charAt(i) == '1') {
                twos.append('0');
            }
            else if (bin.charAt(i) == '0') {
                twos.append('1');
            }
        }

        // Two's complement
        boolean addition = false;
        for (int i = twos.length() - 1; i > 0; i--) {
            if (twos.charAt(i) == '1') {
                twos.setCharAt(i, '0');
            }
            else if (twos.charAt(i) == '0') {
                twos.setCharAt(i, '1');
                addition = true;
                break;
            }
        }
        if (!addition) {
            twos.setCharAt(twos.length() - 1, '1');
        }

        return twos.toString();
    }

    private String convertToHex(String number, int width, int fraction) {
        System.out.println("Init = " + number);

        String parseNumber = String.valueOf(number);
        char sign = '+';
        if (parseNumber.charAt(0) == '-') {
            sign = '-';
            parseNumber = number.substring(1);
        }

        // Division in order to get intPart = 0
        float floatNumber = Float.parseFloat(parseNumber);
        while (floatNumber >= 1.0) {
            floatNumber /= 10.0;
        }

        parseNumber = Float.toString(floatNumber);

        StringBuilder strIntPart = new StringBuilder();
        StringBuilder strFractionPart = new StringBuilder();

        String[] intFraction = parseNumber.split("\\.");
        int intPart = Integer.parseInt(intFraction[0]);

        if (intFraction.length == 2) {
            // FRACTION CONVERSION
            float floatPart = Float.parseFloat("0." + intFraction[1]);

            float remainder = 1f;
            while (remainder != 0.0f) {
                floatPart *= 2.0f;
                strFractionPart.append(String.valueOf(floatPart).split("\\.")[0]);
                floatPart = Float.parseFloat("0." + String.valueOf(floatPart).split("\\.")[1]);
                remainder = floatPart;
                if (strFractionPart.length() >= fraction) {
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

        restBits = width - 1 - fraction - strIntPart.length(); // width - 1 because 32nd bit is sign
        for (int i = 0; i < restBits; i++) {
            strIntPart.insert(0, 0);
        }

        // INTEGER AND FLOAT UNION
        StringBuilder binNumber = new StringBuilder(strIntPart.toString() + strFractionPart);
        System.out.println("Bin = " + binNumber);

        // SIGN CONVERSION
        if (sign == '-') {
            String twosComplement = toTwosCompliment(binNumber.toString());
            binNumber = new StringBuilder(twosComplement);
            binNumber.insert(0, 1);
        }
        else {
            binNumber.insert(0, 0);
        }

        // CONVERT TO HEX
        String[] halfBytes = new String[8];
        for (int i = 0, k = 0; i < binNumber.length() && k < 8; i += 4, k++) {
            halfBytes[k] = binNumber.substring(i, i + 4);
        }

        StringBuilder hexNumber = new StringBuilder();
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
                writer.write(convertToHex(numberB, 32,31));
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
        Text result = new Text(
                "Filter Order = " + firOrder.getText() + "\n"
                + "ArrayX: [" + firArrayX.getText() + "]\n"
                + "ArrayB: [" + firArrayB.getText() + "]"
        );
        HBox containerResult = new HBox(result);
        firResult.getChildren().clear();
        firResult.getChildren().add(containerResult);
    }
}
