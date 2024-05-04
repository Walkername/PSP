package algorithm_pack;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class PspAlgorithm {

    private final String name;

    private final VBox imageBlock;

    public PspAlgorithm(String name) {
        this.name = name;

        Text textName = new Text(name);
        imageBlock = new VBox();
        imageBlock.setMinSize(50.0, 50.0);
        imageBlock.setStyle("-fx-border-color: black;");
        imageBlock.setAlignment(Pos.CENTER);
        //imageBlock.getChildren().add(textName);
        Text textName1 = new Text("hi");
        //imageBlock.getChildren().add(textName1);
    }

    public VBox getImageBlock() {
        return this.imageBlock;
    }

    public String getName() {
        return this.name;
    }
}
