package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.transaction.report.TransactionReportService;
import cloud.matthews.slimstore.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RunReportHandler implements PosProcessHandler {

    private final UserService userService;
    private final TransactionReportService transactionReportService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.RUN_REPORT;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        userService.managerCheck();
        response.setReport(transactionReportService.runReportByForm(request));
    }

}
