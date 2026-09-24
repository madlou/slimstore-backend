package cloud.matthews.slimstore.pos;

import cloud.matthews.slimstore.form.Form.ServerProcess;

/**
 * Implemented by domain packages to handle one {@link ServerProcess} coming
 * from the POS register API, so {@link PosController} does not need to
 * depend directly on every domain service.
 */
public interface PosProcessHandler {

    ServerProcess supports();

    void handle(
        PosRequestDTO request,
        PosResponseDTO response
    ) throws Exception;

}
