package pw.avvero.jam.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private Map<String, String> errors;

}
