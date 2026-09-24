package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.tender.TenderService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmptyBasketHandler implements PosProcessHandler {

    private final BasketService basketService;
    private final TenderService tenderService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.EMPTY_BASKET;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        basketService.empty();
        tenderService.empty();
    }

}
