package algorithm_pack;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FIRFilter extends PspAlgorithm {

    private List<String> signalArray;
    private List<String> coeffsArray;
    private String order;

    public FIRFilter() {
        super("FIR-filter");
        Parent settingInterface = generateInterface();
        Scene scene = new Scene(settingInterface);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Algorithm settings");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.setResizable(false);
        super.setStage(stage);
    }

    public List<String> getSignalArray() {
        return signalArray;
    }

    public List<String> getCoeffsArray() {
        return coeffsArray;
    }

    public String getOrder() {
        return order;
    }

    private Parent generateInterface() {
        AnchorPane anchorPane = new AnchorPane();
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10 10 10 10;");

        VBox orderVBox = new VBox();
        Text titleFirOrder = new Text("Filter order:");
        Text exampleFirOrder = new Text("Example: 3");
        TextField valueFirOrder = new TextField();
        orderVBox.getChildren().addAll(titleFirOrder, exampleFirOrder, valueFirOrder);

        VBox signalVBox = new VBox();
        Text titleSignal = new Text("Signal Array:");
        Text exampleSignal = new Text("Example (numbers with space, fraction is dot): 0.5 1 0.5 1");
        TextArea valueSignal = new TextArea();
        valueSignal.setPrefRowCount(3);
        signalVBox.getChildren().addAll(titleSignal, exampleSignal, valueSignal);

        VBox coeffsVBox = new VBox();
        Text titleCoeffs = new Text("Coefficients Array:");
        Text exampleCoeffs = new Text("Example (numbers with space, fraction is dot): 0.42 0.75 0.6 0.2");
        TextArea valueCoeffs = new TextArea();
        valueCoeffs.setPrefRowCount(3);
        coeffsVBox.getChildren().addAll(titleCoeffs, exampleCoeffs, valueCoeffs);

        VBox outFileChooser = new VBox();
        Text textFileChooser = new Text("Choose the location to create hex file:");
        TextField valueFileChooser = new TextField();
        Button locationChooser = new Button();
        locationChooser.setText("Choose location");
        locationChooser.setOnMouseClicked(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            Stage stage = (Stage) locationChooser.getScene().getWindow();
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                valueFileChooser.setText(selectedDirectory.getAbsolutePath());
            }
        });
        outFileChooser.getChildren().addAll(textFileChooser, valueFileChooser, locationChooser);
        outFileChooser.setSpacing(5);

        HBox generateHBox = new HBox();
        Button generateButton = new Button();
        generateButton.setText("Save");
        generateButton.setOnMouseClicked(event -> {
            this.order = valueFirOrder.getText();
            this.signalArray = Arrays.asList(valueSignal.getText().split(" "));
            this.coeffsArray = Arrays.asList(valueCoeffs.getText().split(" "));
            super.getStage().close();
        });
        generateHBox.getChildren().add(generateButton);
        generateHBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(orderVBox, signalVBox, coeffsVBox, outFileChooser, generateHBox);
        //anchorPane.getChildren().add(vbox);

        return vbox;
    }
}
