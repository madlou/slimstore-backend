package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.product.barcode.Barcode;
import cloud.matthews.slimstore.product.barcode.BarcodeService;
import cloud.matthews.slimstore.view.View.ViewName;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchHandler implements PosProcessHandler {

    private final BarcodeService barcodeService;
    private final BasketService basketService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.SEARCH;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        Barcode barcode = barcodeService.getBarcodeByForm(request);
        if (barcode != null) {
            basketService.addFormElement(barcode.getFormElement());
            request.setTargetView(ViewName.HOME);
        }
    }

}
