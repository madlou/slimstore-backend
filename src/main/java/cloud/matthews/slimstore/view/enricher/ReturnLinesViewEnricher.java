package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.form.FormElement;
import cloud.matthews.slimstore.form.FormElement.FormElementType;
import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.transaction.TransactionLine;
import cloud.matthews.slimstore.transaction.TransactionService;
import cloud.matthews.slimstore.translation.TranslationService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReturnLinesViewEnricher implements ViewEnricher {

    private final TransactionService transactionService;
    private final TranslationService translationService;

    @Override
    public ViewName supports() {
        return ViewName.RETURN_VIEW;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        Form responseForm = view.getForm();
        responseForm.deleteElements();
        Integer strNumber = requestForm.getIntegerValueByKey("store");
        Integer regNumber = requestForm.getIntegerValueByKey("register");
        Integer txnNumber = requestForm.getIntegerValueByKey("transactionNumber");
        String date = requestForm.getValueByKey("date");
        Transaction txn = transactionService.getTransaction(strNumber, regNumber, txnNumber, date);
        if (txn == null) {
            FormElement error = new FormElement();
            error.setType(FormElementType.ERROR);
            error.setLabel(translationService.translate("error.txn_not_found"));
            responseForm.addElement(error);
            return;
        }
        for (TransactionLine line : txn.getLines()) {
            String key = txn.getStore().getNumber().toString() +
                ":" +
                txn.getRegister().getNumber().toString() +
                ":" +
                txn.getNumber().toString() +
                ":" +
                line.getNumber().toString() +
                ":" +
                line.getId().toString();
            FormElement element = new FormElement();
            element.setType(FormElementType.RETURN);
            element.setKey(key);
            element.setValue("" + (line.getQuantity() - line.getReturnedQuantity()));
            element.setQuantity(0);
            element.setPrice(line.getUnitValue());
            element.setLabel(line.getProductCode());
            responseForm.addElement(element);
        }
    }

}
