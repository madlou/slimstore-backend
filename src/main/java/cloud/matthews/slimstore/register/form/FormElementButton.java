package cloud.matthews.slimstore.register.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormElementButton {

    private String label = "";
    private Form form = new Form();

}
