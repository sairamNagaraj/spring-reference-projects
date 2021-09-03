package in.co.cockatiel.engine.template.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenerateDocumentRequest {

    private String fileName;

    private String instructName;

    private String outputType;

    private Object input;

}
