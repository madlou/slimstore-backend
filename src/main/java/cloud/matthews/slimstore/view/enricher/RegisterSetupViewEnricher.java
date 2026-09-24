package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterSetupViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final RegisterService registerService;

    @Override
    public ViewName supports() {
        return ViewName.REGISTER_SETUP;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        userService.managerCheck();
        Form responseForm = view.getForm();
        Integer pinNumber = registerService.getRegister().getCustomerDisplayPin();
        responseForm.setValueByKey("pin", String.format("%04d", pinNumber));
        String printerIp = registerService.getRegister().getPrinterIpAddress();
        if (printerIp == null) {
            printerIp = "";
        }
        responseForm.setValueByKey("printerIpAddress", printerIp);
    }

}
