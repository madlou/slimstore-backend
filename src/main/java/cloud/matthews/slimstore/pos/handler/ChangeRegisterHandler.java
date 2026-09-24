package cloud.matthews.slimstore.pos.handler;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.pos.PosProcessHandler;
import cloud.matthews.slimstore.pos.PosRequestDTO;
import cloud.matthews.slimstore.pos.PosResponseDTO;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangeRegisterHandler implements PosProcessHandler {

    private final UserService userService;
    private final StoreService storeService;
    private final RegisterService registerService;

    @Override
    public ServerProcess supports() {
        return ServerProcess.CHANGE_REGISTER;
    }

    @Override
    public void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception {
        Boolean isUserAdmin = userService.isUserAdmin();
        storeService.setStoreByForm(request, isUserAdmin);
        registerService.setRegisterByForm(request, isUserAdmin);
    }

}
