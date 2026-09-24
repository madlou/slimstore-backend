package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.register.RegisterService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterSetupHandler implements PosProcessHandler {

    private final RegisterService registerService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.REGISTER_SETUP;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        registerService.changePrinterIpAddress(request);
    }

}
