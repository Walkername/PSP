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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {

    private final Queue<PspAlgorithm> algorithmQueue = new LinkedList<>();

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
        String programText = generateCode();
        PSPUtils.createHexFile(programText, absolutePath.getText());
        System.out.println("You generated!");
    }

    private String generateCode() {
        List<String> instructions = new ArrayList<>();
        int writeAddress = 0;
        int readAddress = 0;
        PspAlgorithm firstAlgorithm = algorithmQueue.peek();
        for (PspAlgorithm algorithm : algorithmQueue) {

            // Addresses
            String wAddr = PSPUtils.convertToBinary(String.valueOf(writeAddress), 11);
            String rAddr = PSPUtils.convertToBinary(String.valueOf(readAddress), 11);

            // WRITE LDI INSTRUCTIONS RAM2
            String iwc0LDI = PSPUtils.generateLDI("01001", "01", wAddr);
            // READ LDI INSTRUCTIONS RAM2
            String irp2LDI = PSPUtils.generateLDI("00010", "01", rAddr);

            instructions.add(iwc0LDI);
            instructions.add(irp2LDI);

            // WRITE LDI INSTRUCTIONS RAM0, RAM1
            String iwp0LDI = PSPUtils.generateLDI("00110", "01", wAddr);
            String iwp1LDI = PSPUtils.generateLDI("00111", "01", wAddr);

            // READ LDI INSTRUCTIONS RAM0, RAM1
            String irp0LDI = PSPUtils.generateLDI("00000", "01", rAddr);
            String irp1LDI = PSPUtils.generateLDI("00001", "01", rAddr);

            instructions.add(iwp0LDI);
            instructions.add(iwp1LDI);
            instructions.add(irp0LDI);
            instructions.add(irp1LDI);

            switch (algorithm.getName()) {

                case "FIR-filter" -> {
                    FIRFilter firFilter = (FIRFilter) algorithm;

                    String firOrder = firFilter.getOrder();
                    String[] signalRXarray = firFilter.getSignalRXArray();
                    String[] signalQXarray = firFilter.getSignalQXArray();
                    String[] coeffsArray = firFilter.getCoeffsArray();

                    // STI INSTRUCTIONS RAM2
                    for (String value : coeffsArray) {
                        String data = PSPUtils.convertToHex(value, 32, 31);
                        String cutData = data.substring(0, 20);
                        String iwc0STI = PSPUtils.generateSTI("1001", cutData);
                        instructions.add(iwc0STI);
                    }

                    if (algorithm.equals(firstAlgorithm)) {
                        // STI INSTRUCTIONS RAM0
                        for (String value : signalRXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String iwp0STI = PSPUtils.generateSTI("0110", cutData);
                            instructions.add(iwp0STI);
                        }

                        // STI INSTRUCTIONS RAM1
                        for (String value : signalQXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String iwp1STI = PSPUtils.generateSTI("0111", cutData);
                            instructions.add(iwp1STI);
                        }
                    }

                    // FIR INSTRUCTIONS
                    int intOrder = Integer.parseInt(firOrder);
                    for (int i = 1; i <= intOrder; i++) {
                        String irp0LDIFIR = PSPUtils.generateLDI("00000", "01", rAddr);
                        String irp1LDIFIR = PSPUtils.generateLDI("00001", "01", rAddr);
                        String rModAddr = PSPUtils.convertToBinary(String.valueOf(Integer.parseInt(rAddr) + i - 1), 11);
                        String irp2LDIFIR = PSPUtils.generateLDI("00010", "10", rModAddr);

                        String binOrder = PSPUtils.convertToBinary(String.valueOf(i), 5);
                        String instructionFIR = PSPUtils.generateFIR("1000", "1", binOrder, "100");

                        instructions.add(irp0LDIFIR);
                        instructions.add(irp1LDIFIR);
                        instructions.add(irp2LDIFIR);
                        instructions.add(instructionFIR);
                    }

                    for (int i = intOrder - 1; i >= 1; i--) {
                        String rModAddrRam01 = PSPUtils.convertToBinary(String.valueOf(readAddress + intOrder - 1), 11);
                        String rModAddrRam2 = PSPUtils.convertToBinary(String.valueOf(readAddress + intOrder - i), 11);
                        String irp0LDIFIR = PSPUtils.generateLDI("00000", "10", rModAddrRam01);
                        String irp1LDIFIR = PSPUtils.generateLDI("00001", "10", rModAddrRam01);
                        String irp2LDIFIR = PSPUtils.generateLDI("00010", "01", rModAddrRam2);

                        String binOrder = PSPUtils.convertToBinary(String.valueOf(i), 5);
                        String instructionFIR = PSPUtils.generateFIR("1000", "1", binOrder, "100");

                        instructions.add(irp0LDIFIR);
                        instructions.add(irp1LDIFIR);
                        instructions.add(irp2LDIFIR);
                        instructions.add(instructionFIR);
                    }

                    readAddress += signalRXarray.length;
                    writeAddress += signalRXarray.length;
                }

                case "FFT" -> {
                    FFT fft = (FFT) algorithm;

                    String[] signalRXarray = fft.getSignalRXArray();
                    String[] signalQXarray = fft.getSignalQXArray();
                    String[] coeffsArray = fft.getCoeffsArray();

                    // WRITE TO RAM2
                    for (String value : coeffsArray) {
                        String data = PSPUtils.convertToHex(value, 32, 31);
                        String cutData = data.substring(0, 20);
                        String instructionSTI = PSPUtils.generateSTI("1001", cutData);
                        instructions.add(instructionSTI);
                    }

                    if (algorithm.equals(firstAlgorithm)) {
                        // WRITE TO RAM0
                        for (String value : signalRXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String instructionSTI = PSPUtils.generateSTI("0110", cutData);
                            instructions.add(instructionSTI);
                        }

                        // WRITE TO RAM1
                        for (String value : signalQXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String instructionSTI = PSPUtils.generateSTI("0111", cutData);
                            instructions.add(instructionSTI);
                        }
                    }

                    // FFT
                    for (int i = 0; i < signalRXarray.length; i++) {
                        String instructionFFT = PSPUtils.generateFFT("1000", "1", "100");
                        instructions.add(instructionFFT);
                    }

                    readAddress += signalRXarray.length;
                    writeAddress += signalRXarray.length;
                }
                case "Modulus" -> {
                    Modulus modulus = (Modulus) algorithm;

                    String[] signalRXarray = modulus.getSignalRXArray();
                    String[] signalQXarray = modulus.getSignalQXArray();

                    if (algorithm.equals(firstAlgorithm)) {
                        // WRITE TO RAM0
                        for (String value : signalRXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String instructionSTI = PSPUtils.generateSTI("0110", cutData);
                            instructions.add(instructionSTI);
                        }

                        // WRITE TO RAM1
                        for (String value : signalQXarray) {
                            String data = PSPUtils.convertToHex(value, 32, 31);
                            String cutData = data.substring(0, 20);
                            String instructionSTI = PSPUtils.generateSTI("0111", cutData);
                            instructions.add(instructionSTI);
                        }
                    }

                    for (int i = 0; i < signalRXarray.length; i++) {
                        String instructionMOD = PSPUtils.generateMOD("1001", "1", "011");
                        instructions.add(instructionMOD);
                    }

                    readAddress += signalRXarray.length;
                    writeAddress += signalRXarray.length;
                }
            }
        }

        StringBuilder programText = new StringBuilder();

        for (String instruction : instructions) {
            programText.append(instruction);
            programText.append("\n");
        }

        return programText.toString();
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

                PspAlgorithm firstAlg = algorithmQueue.peek();
                for (PspAlgorithm alg : algorithmQueue) {
                    alg.setState(alg != firstAlg);
                }
            });

            HBox.setHgrow(algorithmNumber, Priority.ALWAYS);
            HBox.setHgrow(settingButton, Priority.ALWAYS);
            HBox.setHgrow(newAlgorithm.getImageBlock(), Priority.ALWAYS);
            HBox.setHgrow(deleteButton, Priority.ALWAYS);
            panelAlgorithm.getChildren().addAll(algorithmNumber, settingButton, newAlgorithm.getImageBlock(), deleteButton);

            newAlgorithm.setState(algorithmQueue.size() != 0);

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
