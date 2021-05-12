package pw.avvero.jam.schema;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import pw.avvero.jam.antlr.DslLexer;
import pw.avvero.jam.antlr.DslParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchemaParserAntlr {

    /**
     * Parsers Issue from string
     *
     * @param string
     * @return
     * @throws Exception
     */
    public Issue parseFromString(String string) throws SchemaParsingError {
        if (StringUtils.isBlank(string)) throw new SchemaParsingError("Provided schema is empty");
        return parseFromCharStream(CharStreams.fromString(string));
    }

    /**
     * Parsers Issue from file
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public Issue parseFromFile(String filePath) throws SchemaParsingError {
        try {
            return parseFromCharStream(CharStreams.fromFileName(filePath));
        } catch (IOException e) {
            throw new SchemaParsingError(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Parses Issue from CharStream
     *
     * @param charStream
     * @return
     * @throws Exception
     */
    private Issue parseFromCharStream(CharStream charStream) throws SchemaParsingError {
        DslLexer lexer = new DslLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DslParser parser = new DslParser(tokens);
        return parse(parser.root());
    }

    /**
     * Parses issue from ParseTree
     *
     * @param tree
     * @return
     * @throws Exception
     */
    private Issue parse(ParseTree tree) throws SchemaParsingError {
        if (tree.getChildCount() == 0) {
            throw new SchemaParsingError("Can't parse issue: no entries are detected");
        }
        List<LeveledIssue> leveledIssues = new ArrayList<>();
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree treeChild = tree.getChild(i);
            if (treeChild instanceof DslParser.IssueContext) {
                leveledIssues.add(new LeveledIssue(0, parse((DslParser.IssueContext) treeChild)));
            } else if (treeChild instanceof DslParser.ChildContext) {
                DslParser.ChildContext childContext = (DslParser.ChildContext) treeChild;
                leveledIssues.add(parse(childContext));
            }
        }
        Issue issue = IssueTreeBuilder.build(leveledIssues);
        if (issue == null) throw new SchemaParsingError("Can't parse issue: no entries are detected");
        return issue;
    }

    private Issue parse(DslParser.IssueContext tree) {
        Issue issue = new Issue();
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree treeChild = tree.getChild(i);
            if (treeChild instanceof DslParser.KeyContext) {
                String key = treeChild.getText().trim();
                String[] keyParts = key.split("-"); // TODO move to antlr
                issue.setProject(keyParts[0]);
                if (keyParts.length == 2) {
                    issue.setKey(key);
                }
            } else if (treeChild instanceof DslParser.TypeContext) {
                issue.setType(treeChild.getText().trim());
            } else if (treeChild instanceof DslParser.SummaryContext) {
                issue.setSummary(treeChild.getText().trim());
            }
        }
        return issue;
    }

    private LeveledIssue parse(DslParser.ChildContext tree) {
        LeveledIssue leveledIssue = new LeveledIssue();
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof DslParser.IssueContext) {
                Issue issue = parse((DslParser.IssueContext) child);
                leveledIssue.setIssue(issue);
            } else if (child instanceof DslParser.DashContext) {
                leveledIssue.setLevel(leveledIssue.getLevel() + 1);
            }
        }
        return leveledIssue;
    }

}
