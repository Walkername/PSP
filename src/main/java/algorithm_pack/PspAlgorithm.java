package algorithm_pack;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public abstract class PspAlgorithm {

    private final String name;

    private final VBox imageBlock;

    private Stage stage;
    private final VBox signalBox = new VBox();

    public PspAlgorithm(String name) {
        this.name = name;

        Text textName = new Text(name);
        this.imageBlock = new VBox();
        this.imageBlock.setMinSize(50.0, 50.0);
        this.imageBlock.setMaxSize(50.0, 50.0);
        this.imageBlock.setStyle("-fx-border-color: black;");
        this.imageBlock.setSpacing(5);
        this.imageBlock.setAlignment(Pos.CENTER);
        this.imageBlock.getChildren().add(textName);
    }

    public void setState(boolean state) {
        signalBox.setDisable(state);
    }

    public VBox getImageBlock() {
        return this.imageBlock;
    }

    public String getName() {
        return this.name;
    }

    public Stage getStage() {
        return this.stage;
    }

    public VBox getSignalBox() {
        return this.signalBox;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
