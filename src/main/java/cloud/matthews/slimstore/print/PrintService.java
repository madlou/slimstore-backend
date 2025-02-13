package cloud.matthews.slimstore.print;

import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.tender.TenderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrintService {

    private final BasketService basketService;
    private final RegisterService registerService;
    private final TenderService tenderService;

    // TODO: Implement the printReceipt method and printer integration
    public void printReceipt() {
        System.out.println("Printing... " + 
            "Basketlines: " + basketService.getBasketArrayList().size() + " | " + 
            "Tenderlines: " + tenderService.getTenderArrayList().size() + " > " + 
            "Printer: " + registerService.getRegister().getPrinterIpAddress());
    }

}
