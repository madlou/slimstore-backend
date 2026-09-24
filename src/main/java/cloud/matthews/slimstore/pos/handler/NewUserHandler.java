package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NewUserHandler implements PosProcessHandler {

    private final UserService userService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.NEW_USER;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        userService.managerCheck();
        userService.addUserByForm(request);
    }

}
