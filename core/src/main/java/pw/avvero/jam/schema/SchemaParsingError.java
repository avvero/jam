package pw.avvero.jam.schema;

public class SchemaParsingError extends Exception{

    public SchemaParsingError() {
    }

    public SchemaParsingError(String message) {
        super(message);
    }

    public SchemaParsingError(String message, Throwable cause) {
        super(message, cause);
    }
}
