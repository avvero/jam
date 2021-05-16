package pw.avvero.jam.schema;

import pw.avvero.jam.core.Issue;

public class SchemaParser {

    private final SchemaParserAntlr parserAntlr = new SchemaParserAntlr();
    private final SchemaParserCustom parserCustom = new SchemaParserCustom();

    /**
     * Parsers Issue from string
     *
     * @param string
     * @return
     * @throws Exception
     */
    public Issue parseFromString(String string) throws SchemaParsingError {
        return parserAntlr.parseFromString(string);
    }

    /**
     * Parsers Issue from file
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public Issue parseFromFile(String filePath) throws SchemaParsingError {
        return parserAntlr.parseFromFile(filePath);
    }
}
