package cloud.matthews.slimstore.tender;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cloud.matthews.slimstore.display.DisplayResponseDTO;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.StoreService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TenderWebsocket {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final RegisterService registerService;
    private final StoreService storeService;

    public void sendTender(
        Tender tender
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setTender(tender.getArrayList());
        String topic = "/topic/" + 
            storeService.getStore().getNumber() + 
            "/" + 
            registerService.getRegister().getNumber();
        messagingTemplate.convertAndSend(topic, response);
    }

}
