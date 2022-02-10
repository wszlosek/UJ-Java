public class Statement {
    private String status;
    private String field;

    public Statement(String status, String field) {
        this.status = status;
        this.field = field;
    }

    public String status() {
        return status;
    }

    public String field() {
        return field;
    }

    private void changeStatus(String newStatus) {
        status = newStatus;
    }

    private void changeField(String newField) {
        field = newField;
    }

    public void changeParameters(String newStatus, String newField) {
        changeStatus(newStatus);
        changeField(newField);
    }
}
