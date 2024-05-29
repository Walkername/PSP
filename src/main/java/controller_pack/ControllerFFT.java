package controller_pack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils_pack.PSPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControllerFFT {

    @FXML
    private Button locationChooser;
    @FXML
    private TextField absolutePath;

    @FXML
    private TextField fftArrayRX;
    @FXML
    private TextField fftArrayRXaddr;
    @FXML
    private TextField fftArrayQX;
    @FXML
    private TextField fftArrayQXaddr;
    @FXML
    private TextField fftArrayB;
    @FXML
    private TextField fftArrayBaddr;

    @FXML
    private void generate(ActionEvent event) {
        String programText = generateProgram();
        PSPUtils.createHexFile(programText, absolutePath.getText());
        System.out.println("You generated!");
    }

    private String generateProgram() {
        List<String> instructions = new ArrayList<>();

        String order = "4";
        String signalRX = fftArrayRX.getText();
        String signalQX = fftArrayQX.getText();
        String coeffs = fftArrayB.getText();

        StringBuilder programText = new StringBuilder();

        // ADDRESSES
        String ram0Addr = PSPUtils.convertToBinary(fftArrayRXaddr.getText(), 11);
        String ram1Addr = PSPUtils.convertToBinary(fftArrayQXaddr.getText(), 11);
        String ram2Addr = PSPUtils.convertToBinary(fftArrayBaddr.getText(), 11);
        // READ
        String irp0 = generateLDI("00000", "01", ram0Addr);
        String irp1 = generateLDI("00001", "01", ram1Addr);
        String irp2 = generateLDI("00010", "01", ram2Addr);
        // WRITE RAM0 & RAM2
        String iwp0 = generateLDI("00110", "01", ram0Addr);
        String iwp1 = generateLDI("00111", "01", ram1Addr);
        String iwc0 = generateLDI("01001", "01", ram2Addr);

        instructions.add(irp0);
        instructions.add(irp1);
        instructions.add(irp2);
        instructions.add(iwp0);
        instructions.add(iwp1);
        instructions.add(iwc0);

        String[] signalArrayRX = signalRX.split(" ");
        String[] signalArrayQX = signalQX.split(" ");
        String[] coeffsArray = coeffs.split(" ");

        // WRITE TO RAM0
        for (String value : signalArrayRX) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("0110", cutData);
            instructions.add(instructionSTI);
        }

        // WRITE TO RAM1
        for (String value : signalArrayQX) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("0111", cutData);
            instructions.add(instructionSTI);
        }

        // WRITE TO RAM2
        for (String value : coeffsArray) {
            String data = PSPUtils.convertToHex(value, 32, 31);
            String cutData = data.substring(0, 21);
            String instructionSTI = generateSTI("1001", cutData);
            instructions.add(instructionSTI);
        }

        // FFT
        String binOrder = PSPUtils.convertToBinary(order, 5);
        for (int i = 0; i < signalArrayRX.length; i++) {
            String instructionFFT = generateFFT("1000", "1", "100");
            instructions.add(instructionFFT);
        }

        for (String instruction : instructions) {
            programText.append(instruction);
            programText.append("\n");
        }

        return programText.toString();
    }

    private String generateFFT(String indReg, String wd, String srcRegs) {
        String instruction = "00100001";
        String reserve = "00000000000";
        String rptReg = "00100";
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
}
