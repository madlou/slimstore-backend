package cloud.matthews.slimstore.pos;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.register.RegisterSetupException;
import cloud.matthews.slimstore.store.StoreSetupException;
import cloud.matthews.slimstore.user.UserLoginException;
import cloud.matthews.slimstore.view.View.ViewName;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PosController {

    private final PosAccessGuard accessGuard;
    private final PosResponseAssembler responseAssembler;
    private final List<PosProcessHandler> serverProcessHandlers;

    @Value("${tjx.app.debug}")
    private Boolean appDebug;

    private Map<ServerProcess, PosProcessHandler> handlersByProcess;

    private Map<ServerProcess, PosProcessHandler> handlersByProcess() {
        if (handlersByProcess == null) {
            handlersByProcess = serverProcessHandlers.stream()
                .collect(Collectors.toMap(handler -> handler.supports(), Function.identity()));
        }
        return handlersByProcess;
    }

    @PostMapping(path = "/api/register")
    public @ResponseBody PosResponseDTO apiRegister(
        @RequestBody
        PosRequestDTO request,
        @CookieValue(value = "store-register", required = false)
        String storeRegCookie
    ) throws Exception {
        PosResponseDTO response = new PosResponseDTO();
        try {
            request = accessGuard.check(request, storeRegCookie);
            response = process(request);
        } catch (Exception e) {
            response.setError(handleException(request, e));
        }
        return responseAssembler.assemble(request, response, appDebug);
    }

    private String handleException(
        PosRequestDTO request,
        Exception e
    ) throws Exception {
        if (e instanceof UserLoginException) {
            request.setTargetView(ViewName.LOGIN);
        } else if ((e instanceof StoreSetupException) ||
            (e instanceof RegisterSetupException)) {
            request.setTargetView(ViewName.REGISTER_CHANGE);
        } else {
            request.setTargetView(ViewName.HOME);
        }
        if (Boolean.TRUE.equals(appDebug)) {
            throw new Exception(e.getMessage());
        }
        return e.getMessage();
    }

    private PosResponseDTO process(
        PosRequestDTO request
    ) throws Exception {
        PosResponseDTO response = new PosResponseDTO();
        ServerProcess serverProcess = request.getServerProcess();
        if (serverProcess == null) {
            return response;
        }
        PosProcessHandler handler = handlersByProcess().get(serverProcess);
        if (handler != null) {
            handler.handle(request, response);
        }
        return response;
    }

}
