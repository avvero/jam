package pw.avvero.jiac.dsl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.jiac.antlr.DslLexer;
import pw.avvero.jiac.antlr.DslParser;
import pw.avvero.jiac.dsl.Issue;
import pw.avvero.jiac.dsl.IssueTreeBuilder;
import pw.avvero.jiac.dsl.LeveledIssue;

import java.util.ArrayList;
import java.util.List;

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
        ParseTree tree = parser.root();
        if (tree.getChildCount() == 0) {
            throw new Exception("Can't parse Issue");
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
        return IssueTreeBuilder.build(leveledIssues);
    }

    private Issue parse(DslParser.IssueContext tree) {
        Issue issue = new Issue();
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree treeChild = tree.getChild(i);
            if (treeChild instanceof DslParser.ProjectContext) {
                issue.setProject(treeChild.getText().trim());
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