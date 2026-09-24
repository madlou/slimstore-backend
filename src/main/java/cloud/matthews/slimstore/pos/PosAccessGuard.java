package cloud.matthews.slimstore.pos;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form.ServerProcess;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.LocationSetupException;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.translation.TranslationService;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserLoginException;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View.ViewName;
import lombok.RequiredArgsConstructor;

/**
 * Validates that an incoming request is allowed to proceed (register is
 * initialised, user is logged in / permitted for their store) before it
 * reaches a {@link PosProcessHandler}.
 */
@Component
@RequiredArgsConstructor
public class PosAccessGuard {

    private final RegisterService registerService;
    private final StoreService storeService;
    private final UserService userService;
    private final TranslationService translationService;

    public PosRequestDTO check(
        PosRequestDTO request,
        String storeRegCookie
    ) throws Exception {
        registerService.initialiseRegister(storeRegCookie);
        if (userService.isLoggedOut()) {
            if (request.getTargetView() == ViewName.ABOUT) {
                return request;
            }
            if (request.getServerProcess() != ServerProcess.LOGIN) {
                request.setTargetView(ViewName.LOGIN);
                request.setServerProcess(null);
                return request;
            }
            User user = userService.getUser(request.getValueByKey("code"));
            if ((user == null) ||
                !user.isSet()) {
                throw new UserLoginException(translationService.translate("error.security_user_not_found"));
            }
            if (storeService.getStore().isSet()) {
                Store userStore = user.getStore();
                if (((userStore != null) &&
                    !userStore.isSet()) &&
                    !user.isAdmin()) {
                    throw new UserLoginException(translationService.translate("error.security_user_not_found"));
                }
                if (!user.isAdmin() &&
                    (userStore != null)) {
                    String usrStoreNum = userStore.getNumber().toString();
                    String regStoreNum = storeService.getStore().getNumber().toString();
                    if (!usrStoreNum.equals(regStoreNum)) {
                        throw new UserLoginException(translationService.translate("error.security_user_wrong_store", usrStoreNum, regStoreNum));
                    }
                }
            }
        } else if ((!storeService.getStore().isSet()) &&
            (request.getServerProcess() != ServerProcess.CHANGE_REGISTER)) {
            throw new LocationSetupException(translationService.translate("error.location_setup_required"));
        }
        return request;
    }

}
