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
import cloud.matthews.slimstore.store.LocationSetupException;
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
                .collect(Collectors.toMap(PosProcessHandler::supports, Function.identity()));
        }
        return handlersByProcess;
    }

    @PostMapping(path = "/api/register")
    public @ResponseBody
    PosResponseDTO apiRegister(
        @RequestBody
        PosRequestDTO request,
        @CookieValue(value = "store-register", required = false)
        String storeRegCookie,
        String errorMessage
    ) throws Exception {
        PosResponseDTO response = new PosResponseDTO();
        try {
            request = accessGuard.check(request, storeRegCookie);
            response = process(request);
        } catch (UserLoginException e) {
            request.setTargetView(ViewName.LOGIN);
            errorMessage = e.getMessage();
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        } catch (LocationSetupException e) {
            request.setTargetView(ViewName.REGISTER_CHANGE);
            errorMessage = e.getMessage();
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            request.setTargetView(ViewName.HOME);
            errorMessage = e.getMessage();
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        }
        if (errorMessage != null) {
            response.setError(errorMessage);
        }
        return responseAssembler.assemble(request, response, appDebug);
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
