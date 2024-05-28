package controller_pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils_pack.PSPUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerModulus {
    public final int inDataAddr = 0;
    public final int outDataAddr = 0;

    @FXML
    private Button locationChooser;
    @FXML
    private TextField absolutePath;

    @FXML
    private TextArea realPart;
    @FXML
    private TextArea imagPart;

    @FXML
    private void generate(ActionEvent event) {
        String programText = generateProgram();
        createHexFile(programText, absolutePath.getText());
        System.out.println("You generated!");
    }

    private String generateProgram() {
        List<String> instructions = new ArrayList<>();

        String order = "1";
        String realPartText = realPart.getText();
        String imagPartText = imagPart.getText();

        StringBuilder programText = new StringBuilder();

        // READ
        String inAddr = PSPUtils.convertToBinary(String.valueOf(inDataAddr), 11);
        String irp01 = generateLDI("00011", "01", inAddr);
        // SETTING OF REGISTERS FOR WRITE RAM0 & RAM2
        String iwp0 = generateLDI("00110", "01", inAddr);
        String iwp1 = generateLDI("00111", "01", inAddr);
        String iwc0 = generateLDI("01001", "01", inAddr);

        instructions.add(irp01);
        instructions.add(iwp0);
        instructions.add(iwp1);
        instructions.add(iwc0);

        String[] realArray = realPartText.split(" ");
        String[] imagArray = imagPartText.split(" ");

        // WRITE TO RAM0
        for (String value : realArray) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("0110", cutData);
            instructions.add(instructionSTI);
        }

        // WRITE TO RAM1
        for (String value : imagArray) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("0111", cutData);
            instructions.add(instructionSTI);
        }

        // FFT
        String binOrder = PSPUtils.convertToBinary(order, 5);
        int length = Math.max(realArray.length, imagArray.length);
        for (int i = 0; i < length; i++) {
            String instructionMOD = generateMOD("1001", "1", "011");
            instructions.add(instructionMOD);
        }

        for (String instruction : instructions) {
            programText.append(instruction);
            programText.append("\n");
        }

        return programText.toString();
    }

    private String generateMOD(String indReg, String wd, String srcRegs) {
        String instruction = "00100010";
        String reserve = "00000000000";
        String rptReg = "00001";
        instruction += indReg + wd + rptReg + srcRegs + reserve;
        return instruction;
    }

    private String generateSTI(String indReg, String data) {
        String instruction = "00000010" + indReg;
        String constant = data.substring(0, 20);
        instruction += constant;
        return instruction;
    }

    private String generateLDI(String indReg, String dir, String data) {
        String instruction = "";
        String operationCode = "00000001";
        String reserve = "000000";
        instruction += operationCode + indReg + dir + data + reserve;
        return instruction;
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
    private void createHexFile(String program, String absolutePath) {
        try (FileWriter writer = new FileWriter(absolutePath + "\\program.txt", false)) {
            writer.write(program);
            writer.flush();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
