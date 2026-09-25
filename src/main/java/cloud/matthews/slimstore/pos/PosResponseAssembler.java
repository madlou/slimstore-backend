package cloud.matthews.slimstore.pos;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewService;
import lombok.RequiredArgsConstructor;

/**
 * Builds the {@link PosResponseDTO} returned to the UI after each request.
 * Live register state is delivered separately through broadcast websocket
 * messages.
 */
@Component
@RequiredArgsConstructor
public class PosResponseAssembler {

    private final ViewService viewService;

    public PosResponseDTO assemble(
        PosRequestDTO request,
        PosResponseDTO response,
        Boolean appDebug
    ) throws Exception {
        try {
            response.setView(viewService.getViewByForm(request));
        } catch (Exception e) {
            response.setView(viewService.getViewByName(ViewName.HOME));
            response.setError(e.getMessage());
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        }
        return response;
    }

}
