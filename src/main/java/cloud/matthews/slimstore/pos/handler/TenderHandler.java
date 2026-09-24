package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.print.PrintService;
import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.transaction.TransactionService;
import cloud.matthews.slimstore.view.View.ViewName;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenderHandler implements PosProcessHandler {

    private final TenderService tenderService;
    private final TransactionService transactionService;
    private final PrintService printService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.TENDER;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        tenderService.addTenderByForm(request);
        if (tenderService.isComplete()) {
            request.setTargetView(ViewName.COMPLETE);
            transactionService.addTransaction();
            printService.printReceipt();
        }
    }

}
