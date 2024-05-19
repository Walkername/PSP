package utils_pack;

public class PSPUtils {

    public static String toTwosCompliment(String bin) {
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
        for (int i = twos.length() - 1; i >= 0; i--) {
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

    public static String convertToBinary(String number, int width) {
        StringBuilder strIntPart = new StringBuilder();
        int intPart = Integer.parseInt(number);
        while(intPart > 0) {
            strIntPart.insert(0, intPart % 2);
            intPart /= 2;
        }

        int restBits = width - strIntPart.length(); // width - 1 because 32nd bit is sign
        for (int i = 0; i < restBits; i++) {
            strIntPart.insert(0, 0);
        }

        return strIntPart.toString();
    }

    public static String convertToHex(String number, int width, int fraction) {

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
        //String[] halfBytes = new String[8];
        //for (int i = 0, k = 0; i < binNumber.length() && k < 8; i += 4, k++) {
        //    halfBytes[k] = binNumber.substring(i, i + 4);
        //}

        //StringBuilder hexNumber = new StringBuilder();
        //for (String halfByte: halfBytes) {
        //    hexNumber.append(Integer.toHexString(Integer.parseInt(halfByte, 2)));
        //}

        return binNumber.toString();
    }

}
