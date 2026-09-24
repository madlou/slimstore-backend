package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportsViewEnricher implements ViewEnricher {

    private final UserService userService;

    @Override
    public ViewName supports() {
        return ViewName.REPORTS;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        userService.managerCheck();
        Form responseForm = view.getForm();
        if (requestForm.findByKey("scope") != null) {
            responseForm.setValueByKey("scope", requestForm.getValueByKey("scope"));
            responseForm.setValueByKey("report", requestForm.getValueByKey("report"));
            responseForm.setValueByKey("days", requestForm.getValueByKey("days"));
        } else {
            responseForm.setValueByKey("scope", "Register");
            responseForm.setValueByKey("report", "Transactions");
            responseForm.setValueByKey("days", "1");
        }
    }

}
