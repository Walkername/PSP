package algorithm_pack;

public class FIRFilter extends PspAlgorithm {

    public FIRFilter() {
        super("FIR-filter");
    }

    public String generate(String signal, String coeffs, String order) {
        StringBuilder program = new StringBuilder();
        String[] signalArray = signal.split(" ");
        String[] coeffsArray = coeffs.split(" ");
        return "";
    }
}
