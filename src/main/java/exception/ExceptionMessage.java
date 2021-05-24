package exception;

public class ExceptionMessage {
    private String parameter;
    private String value;

    public ExceptionMessage() {
    }

    public ExceptionMessage(String parameter, String value) {
        this.parameter = parameter;
        this.value = value;
    }


    public String getJson(){
        return parameter == null || value == null ? "{}" : String.format("{\"%s\" : \"%s\"}",parameter, value);
    }

}
