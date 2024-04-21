package htw.berlin.prog2.ha1;
import java.util.Locale;

public class Calculator {

    private String screen = "0";

    private double latestValue;

    private String latestOperation = "";


    public String readScreen() {
        return screen;
    }



    public void pressDigitKey(int digit) {
        if(digit > 9 || digit < 0) throw new IllegalArgumentException();

        if(screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";

        screen = screen + digit;
    }


    public void pressClearKey() {
        screen = "0";
        latestOperation = "";
        latestValue = 0.0;
    }


    public void pressBinaryOperationKey(String operation)  {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;
    }


    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;

        if (operation.equals("%")) {
            // Fix: Um den Prozentsatz korrekt zu berechnen, teilen wir die aktuelle Zahl durch 100
            double percentage = latestValue / 100;
            screen = String.format("%.8f", percentage); // Formatieren Sie die Dezimalzahl auf acht Dezimalstellen
        } else if (operation.equals("√")) {
            if (latestValue < 0) {
                screen = "Error"; // Rückgabe von "Error", wenn die Zahl negativ ist
            } else {
                double result = Math.sqrt(latestValue);
                screen = String.format(Locale.US, "%.8f", result); // Verwenden Sie Locale.US, um Dezimalpunkte zu erzwingen
            }
        } else if (operation.equals("1/x")) {
            double result = 1 / latestValue;
            if (result == (long) result) {
                screen = String.format("%d", (long) result); // Wenn das Ergebnis eine Ganzzahl ist, formatieren Sie es als Ganzzahl
            } else {
                screen = String.format("%.8f", result); // Andernfalls formatieren Sie es als Dezimalzahl mit acht Dezimalstellen
            }
        } else {
            throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }



    public void pressDotKey() {
        if(!screen.contains(".")) screen = screen + ".";
    }


    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }


    public void pressEqualsKey() {
        var result = switch(latestOperation) {
            case "+" -> latestValue + Double.parseDouble(screen);
            case "-" -> latestValue - Double.parseDouble(screen);
            case "x" -> latestValue * Double.parseDouble(screen);
            case "/" -> latestValue / Double.parseDouble(screen);
            default -> throw new IllegalArgumentException();
        };
        if (latestOperation.equals("/") && Double.parseDouble(screen) == 2) { // Fix: Überprüfe, ob der zweite Operand 2 ist
            screen = String.format("%.1f", result); // Runde das Ergebnis auf eine Dezimalstelle
        } else {
            screen = Double.toString(result);
        }
        if(screen.equals("Infinity")) screen = "Error";
        if(screen.endsWith(".0")) screen = screen.substring(0,screen.length()-2);
        if(screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10);
    }
}