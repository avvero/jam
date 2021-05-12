package pw.avvero.jam.schema;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaParserCustom {

    public Issue parseFromString(String string) throws SchemaParsingError {
        return parse(Stream.of(string.split(System.getProperty("line.separator"))));
    }

    public Issue parseFromFile(String filePath) throws SchemaParsingError {
        try {
            Stream<String> lines = Files.lines(Path.of(filePath), Charset.defaultCharset());
            return parse(lines);
        } catch (IOException e) {
            throw new SchemaParsingError(e.getLocalizedMessage(), e);
        }
    }

    public Issue parse(Stream<String> lines) throws SchemaParsingError {
        List<LeveledIssue> leveledIssues = lines
                .map(this::mapLineToIssue)
                .collect(Collectors.toList());
        Issue issue = IssueTreeBuilder.build(leveledIssues);
        if (issue == null) throw new SchemaParsingError("Can't parse issue: no entries are detected");
        return issue;
    }

    private LeveledIssue mapLineToIssue(String value) {
        return null;
    }

    private String capture(String patternString, String value) {
        Matcher matcher = Pattern.compile(patternString).matcher(value);
        return matcher.find() ? matcher.group() : null;
    }
}
