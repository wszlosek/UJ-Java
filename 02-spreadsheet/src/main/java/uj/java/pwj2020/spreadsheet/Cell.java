package uj.java.pwj2020.spreadsheet;

public class Cell {

    public enum Status {
        NUMBER, REFERENCE, FORMULA
    }

    public String value;
    public Status status;

    public Cell(String value) {
        this.value = value;
        getStatus(value);
    }

    private void getStatus(String value) {
        if (reference(value)) {
            this.status = Status.REFERENCE;
        } else if (mathFunction(value)) {
            this.status = Status.FORMULA;
        } else {
            this.status = Status.NUMBER;
        }
    }

    private boolean reference(String value) {
        return value.contains("$") && !mathFunction(value);
    }

    private boolean mathFunction(String value) {
        return value.contains("=");
    }

    public int characterToColumnNumber() {
        return (int) value.toUpperCase().charAt(1) - 'A';
    }

    public String getOperationFromFormula() {
        return value.substring(1, 4);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "value='" + value + '\'' +
                ", status=" + status +
                '}';
    }
}
