package pw.avvero.jiac;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.jiac.antlr.DslLexer;
import pw.avvero.jiac.antlr.DslParser;
import pw.avvero.jiac.dsl.Issue;

public class SchemaParser {

    /**
     * Parsers Issue from string
     *
     * @param string
     * @return
     * @throws Exception
     */
    public Issue parseFromString(String string) throws Exception {
        return parseFromCharStream(CharStreams.fromString(string));
    }

    /**
     * Parsers Issue from file
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public Issue parseFromFile(String filePath) throws Exception {
        return parseFromCharStream(CharStreams.fromFileName(filePath));
    }

    /**
     * Parses Issue from CharStream
     *
     * @param charStream
     * @return
     * @throws Exception
     */
    public Issue parseFromCharStream(CharStream charStream) throws Exception {
        DslLexer lexer = new DslLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DslParser parser = new DslParser(tokens);
        ParseTree tree = parser.issue();
        if (tree.getChildCount() == 0) {
            throw new Exception("Can't parse Issue");
        }
        Issue issue = new Issue();
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof DslParser.ProjectContext) {
                issue.setProject(child.getText().trim());
            } else if (child instanceof DslParser.TypeContext) {
                issue.setType(child.getText().trim());
            } else if (child instanceof DslParser.SummaryContext) {
                issue.setSummary(child.getText().trim());
            }
        }
        return issue;
    }
}
