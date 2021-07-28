package pw.avvero.jam.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueLinkType implements Serializable {

    private String name;
    private String inward;
    private String outward;

}
