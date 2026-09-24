package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.form.FormElement;
import cloud.matthews.slimstore.form.FormElement.FormElementType;
import cloud.matthews.slimstore.form.FormElementButton;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VoidViewEnricher implements ViewEnricher {

    private final BasketService basketService;

    @Override
    public ViewName supports() {
        return ViewName.VOID;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        Form responseForm = view.getForm();
        responseForm.deleteElements();
        Integer index = 0;
        for (BasketLine line : basketService.getBasketArray()) {
            FormElement voidLine = new FormElement();
            voidLine.setKey("void");
            voidLine.setValue(index.toString());
            Form voidForm = new Form();
            voidForm.setTargetView(ViewName.VOID);
            voidForm.setServerProcess(ServerProcess.VOID_LINE);
            voidForm.addElement(voidLine);
            FormElementButton voidButton = new FormElementButton();
            voidButton.setLabel("Void Line");
            voidButton.setForm(voidForm);
            FormElement element = new FormElement();
            element.setType(FormElementType.BUTTON);
            element.setButton(voidButton);
            element.setKey(line.getCode());
            element.setLabel(line.getName());
            element.setValue(line.getLineValue().toString());
            index++;
            responseForm.addElement(element);
        }
    }

}
