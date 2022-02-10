package uj.java.gvt;

public enum Operation {

    INIT ("init"),
    ADD ("add"),
    DETACH ("detach"),
    CHECKOUT ("checkout"),
    COMMIT ("commit"),
    HISTORY ("history"),
    VERSION ("version");

    private final String COMMAND_TEXT;

    Operation(String commandText) {
        this.COMMAND_TEXT = commandText;
    }

    public String commandText() {
        return COMMAND_TEXT;
    }

}
