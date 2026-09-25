package cloud.matthews.slimstore.print;

import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.tender.TenderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrintService {

    private final BasketService basketService;
    private final Register register;
    private final TenderService tenderService;

    // TODO: Implement the printReceipt method and printer integration
    public void printReceipt() {
        System.out.println("Printing... " + 
            "Basketlines: " + basketService.getBasketArrayList().size() + " | " + 
            "Tenderlines: " + tenderService.getTenderArrayList().size() + " > " + 
            "Printer: " + register.getPrinterIpAddress());
    }

}
