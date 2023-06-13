package gui.utility;

public class GUIValidators {

    public static boolean validateID(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean validateXCoordinate(String x) {
        try {
            Long.parseLong(x);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean validateYCoordinate(String y) {
        try {
            Integer.parseInt(y);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean validateXLocation(String x) {
        try {
            Long.parseLong(x);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean validateYLocation(String y) {
        try {
            Float.parseFloat(y);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean validateHeight(String height) {
        try {
            Long.parseLong(height);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

}
