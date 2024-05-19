package algorithm_pack;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public abstract class PspAlgorithm {

    private final String name;

    private final VBox imageBlock;

    public PspAlgorithm(String name) {
        this.name = name;

        Text textName = new Text(name);
        imageBlock = new VBox();
        imageBlock.setMinSize(50.0, 50.0);
        imageBlock.setStyle("-fx-border-color: black;");
        imageBlock.setSpacing(5);
        imageBlock.setAlignment(Pos.CENTER);
        imageBlock.getChildren().add(textName);
    }

    public String generateSTI(String data, int ram) {
        String instruction = "00000010";
        switch (ram) {
            case 0 -> instruction += "0101";
            case 1 -> instruction += "0110";
            case 2 -> instruction += "1000";
        }
        String constant = data.substring(0, 21);
        instruction += constant;
        return instruction;
    }

    public VBox getImageBlock() {
        return this.imageBlock;
    }

    public String getName() {
        return this.name;
    }
}
