package cloud.matthews.slimstore.pos;

import com.fasterxml.jackson.annotation.JsonRawValue;

import cloud.matthews.slimstore.view.View;
import lombok.Data;

@Data
public class PosResponseDTO {

    private String error = new String();
    @JsonRawValue
    private String report;
    private View view = new View();

}
