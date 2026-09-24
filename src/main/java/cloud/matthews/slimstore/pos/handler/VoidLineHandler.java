package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VoidLineHandler implements PosProcessHandler {

    private final BasketService basketService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.VOID_LINE;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        basketService.voidLineByForm(request);
    }

}
