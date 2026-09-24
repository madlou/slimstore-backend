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
public class AddManualReturnToBasketHandler implements PosProcessHandler {

    private final BasketService basketService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.ADD_MANUAL_RETURN_TO_BASKET;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        basketService.addManualReturnToBasketByForm(request);
    }

}
