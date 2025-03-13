package cloud.matthews.slimstore.display;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DisplayWebsocketController {
    
    @MessageMapping("/connect")
    @SendTo("/topic/connected")
    public DisplayResponseDTO connect(
        @Payload
        DisplayRequestDTO request
    ) throws Exception {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStoreNumber(request.getStore());
        response.setRegisterNumber(request.getRegister());
        return response;
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnected")
    public DisplayResponseDTO disconnect(
        @Payload
        DisplayRequestDTO request
    ) throws Exception {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStoreNumber(request.getStore());
        response.setRegisterNumber(request.getRegister());
        return response;
    }

    // public void sendAuthorisation(
    //     String status,
    //     Integer reference
    // ) {
    //     DisplayResponseDTO response = new DisplayResponseDTO();
    //     Integer storeNumber = storeService.getStore().getNumber();
    //     Integer registerNumber = registerService.getRegister().getNumber();
    //     String topic = "/topic/" + storeNumber + "/" + registerNumber;
    //     messagingTemplate.convertAndSend(topic, response);
    // }

}
