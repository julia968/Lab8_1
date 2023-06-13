package Utils;

import java.util.ArrayList;

public final class LineSplitter {

    private LineSplitter() {
    }
    public static ArrayList<String> smartSplit(String line) {
        ArrayList<String> splittedLine = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        boolean screeningStarted = false;
        for (char ch : line.toCharArray()) {
            if (ch == ' ' && !screeningStarted) {
                splittedLine.add(currentString.toString());
                currentString.setLength(0);
            } else if (ch == '"') {
                screeningStarted = !screeningStarted;
            } else {
                currentString.append(ch);
            }
        }
        splittedLine.add(currentString.toString());
        return splittedLine;
    }

}
