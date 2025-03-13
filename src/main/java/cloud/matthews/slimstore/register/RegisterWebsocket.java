package cloud.matthews.slimstore.register;

import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cloud.matthews.slimstore.display.DisplayResponseDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterWebsocket {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final ModelMapper modelMapper;

    public void sendRegister(
        Register register
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setRegister(modelMapper.map(register, RegisterDTO.class));
        String topic = "/topic/" + 
        register.getStore().getNumber() + 
            "/" + 
            register.getNumber();
        messagingTemplate.convertAndSend(topic, response);
    }

}
