package controller_pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils_pack.PSPUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerFIR {

    @FXML
    private Button locationChooser;
    @FXML
    private TextField absolutePath;

    @FXML
    private TextField firOrder;
    @FXML
    private TextField firArrayRX;
    @FXML
    private TextField firArrayRXaddr;
    @FXML
    private TextField firArrayQX;
    @FXML
    private TextField firArrayQXaddr;
    @FXML
    private TextField firArrayB;
    @FXML
    private TextField firArrayBaddr;

    @FXML
    private void generate(ActionEvent event) {
        String programText = generateProgram();
        createHexFile(programText, absolutePath.getText());
        System.out.println("You generated!");
    }

    private String generateProgram() {
        List<String> instructions = new ArrayList<>();

        String order = firOrder.getText();
        String signalRX = firArrayRX.getText();
        String signalQX = firArrayQX.getText();
        String coeffs = firArrayB.getText();

        StringBuilder programText = new StringBuilder();

        // READ
        String ram0Addr = PSPUtils.convertToBinary(firArrayRXaddr.getText(), 11);
        String ram1Addr = PSPUtils.convertToBinary(firArrayRXaddr.getText(), 11);
        String ram2Addr = PSPUtils.convertToBinary(firArrayBaddr.getText(), 11);
        String irp0 = generateLDI("00000", "01", ram0Addr);
        String irp2 = generateLDI("00010", "01", ram2Addr);
        // WRITE RAM0 & RAM2
        String iwp0 = generateLDI("00110", "01", ram0Addr);
        String iwc0 = generateLDI("01001", "01", ram2Addr);

        instructions.add(irp0);
        instructions.add(irp2);
        instructions.add(iwp0);
        instructions.add(iwc0);

        String[] signalArray = signalRX.split(" ");
        String[] coeffsArray = coeffs.split(" ");

        // WRITE TO RAM0
        for (String value : signalArray) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("0110", cutData);
            instructions.add(instructionSTI);
        }

        // WRITE TO RAM2
        for (String value : coeffsArray) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("1001", cutData);
            instructions.add(instructionSTI);
        }

        // FIR2
        int intOrder = Integer.parseInt(order);

        for (int i = 1; i <= intOrder; i++) {
            String inModAddr = PSPUtils.convertToBinary(String.valueOf(Integer.parseInt(ram2Addr) + i - 1), 11);
            String irp0LDI = generateLDI("00000", "01", ram0Addr);
            String irp2LDI = generateLDI("00010", "10", inModAddr);

            String binOrder = PSPUtils.convertToBinary(String.valueOf(i), 5);
            String instructionFIR = generateFIR("0110", "1", binOrder, "100");

            instructions.add(irp0LDI);
            instructions.add(irp2LDI);
            instructions.add(instructionFIR);
        }

        for (int i = intOrder - 1; i >= 1; i--) {
            String inModAddrRam0 = PSPUtils.convertToBinary(String.valueOf(intOrder - 1), 11);
            String inModAddrRam2 = PSPUtils.convertToBinary(String.valueOf(intOrder - i), 11);
            String irp0LDI = generateLDI("00000", "10", inModAddrRam0);
            String irp2LDI = generateLDI("00010", "01", inModAddrRam2);

            String binOrder = PSPUtils.convertToBinary(String.valueOf(i), 5);
            String instructionFIR = generateFIR("0110", "1", binOrder, "100");

            instructions.add(irp0LDI);
            instructions.add(irp2LDI);
            instructions.add(instructionFIR);
        }

        for (String instruction : instructions) {
            programText.append(instruction);
            programText.append("\n");
        }

        return programText.toString();
    }

    private String generateFIR(String indReg, String wd, String rptReg, String srcRegs) {
        String instruction = "00100000";
        String reserve = "00000000000";
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
