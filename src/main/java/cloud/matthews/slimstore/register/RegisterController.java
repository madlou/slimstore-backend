package cloud.matthews.slimstore.register;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.giftcard.GiftCardService;
import cloud.matthews.slimstore.product.barcode.Barcode;
import cloud.matthews.slimstore.product.barcode.BarcodeService;
import cloud.matthews.slimstore.register.form.Form.ServerProcess;
import cloud.matthews.slimstore.register.view.View.ViewName;
import cloud.matthews.slimstore.register.view.ViewService;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreDTO;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.transaction.TransactionService;
import cloud.matthews.slimstore.transaction.report.TransactionReportService;
import cloud.matthews.slimstore.translation.TranslationService;
import cloud.matthews.slimstore.translation.UserInterfaceService;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserDTO;
import cloud.matthews.slimstore.user.UserLoginException;
import cloud.matthews.slimstore.user.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    
    private final RegisterService registerService;
    private final BasketService basketService;
    private final TenderService tenderService;
    private final UserInterfaceService userInterfaceService;
    private final UserService userService;
    private final ViewService viewService;
    private final GiftCardService giftCardService;
    private final StoreService storeService;
    private final BarcodeService barcodeService;
    private final TransactionService transactionService;

    private final TransactionReportService transactionReportService;
    
    private final TranslationService translationService;
    
    private final ModelMapper modelMapper;

    @Value("${tjx.app.debug}")
    private Boolean appDebug;
    
    @PostMapping(path = "/api/register")
    public @ResponseBody
    RegisterResponseDTO apiRegister(
        @RequestBody
        RegisterRequestDTO request,
        @CookieValue(value = "store-register", required = false)
        String storeRegCookie,
        String errorMessage
    ) throws Exception {
        RegisterResponseDTO response = new RegisterResponseDTO();
        try {
            request = checks(request, storeRegCookie);
            response = process(request);
        } catch (UserLoginException e) {
            request.setTargetView(ViewName.LOGIN);
            errorMessage = e.getMessage();
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        } catch (RegisterChangeException e) {
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
        return updateDTO(request, response);
    }
    
    private RegisterRequestDTO checks(
        RegisterRequestDTO request,
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
            throw new RegisterChangeException(translationService.translate("error.location_setup_required"));
        }
        return request;
    }

    public RegisterResponseDTO process(
        RegisterRequestDTO request
    ) throws Exception {
        RegisterResponseDTO response = new RegisterResponseDTO();
        switch (request.getServerProcess()) {
            case null:
            default:
                break;
            case ADD_TO_BASKET:
                basketService.addToBasketByForm(request);
                break;
            case ADD_MANUAL_RETURN_TO_BASKET:
                basketService.addManualReturnToBasketByForm(request);
                break;
            case CHANGE_REGISTER:
                Boolean isUserAdmin = userService.isUserAdmin();
                storeService.setStoreByForm(request, isUserAdmin);
                registerService.setRegisterByForm(request, isUserAdmin);
                break;
            case EMPTY_BASKET:
                basketService.empty();
                tenderService.empty();
                break;
            case LOGIN:
                userService.loginByForm(request);
                registerService.registerCheck();
                break;
            case LOGOUT:
                request.setTargetView(ViewName.LOGIN);
                userService.logout();
                break;
            case NEW_USER:
                userService.managerCheck();
                userService.addUserByForm(request);
                break;
            case PROCESS_GIFTCARD:
                basketService.addToBasketByForm(giftCardService.topupByForm(request));
                break;
            case RUN_REPORT:
                userService.managerCheck();
                response.setReport(transactionReportService.runReportByForm(request));
                break;
            case SEARCH:
                Barcode barcode = barcodeService.getBarcodeByForm(request);
                if (barcode != null) {
                    basketService.addFormElement(barcode.getFormElement());
                    request.setTargetView(ViewName.HOME);
                }
                break;
            case SAVE_USER:
                userService.managerCheck();
                userService.saveUserByForm(request);
                break;
            case STORE_SETUP:
                userService.managerCheck();
                storeService.saveStoreByForm(request);
                break;
            case TENDER:
                tenderService.addTenderByForm(request);
                if (tenderService.isComplete()) {
                    request.setTargetView(ViewName.COMPLETE);
                    transactionService.addTransaction();
                }
                break;
            case TRANSACTION_COMPLETE:
                basketService.empty();
                tenderService.empty();
                break;
            case VOID_LINE:
                basketService.voidLineByForm(request);
                break;
        }
        return response;
    }
    
    private RegisterResponseDTO updateDTO(
        RegisterRequestDTO request,
        RegisterResponseDTO response
    ) throws Exception {
        StoreDTO storeDTO = null;
        if (storeService.getStore().isSet()) {
            storeDTO = modelMapper.map(storeService.getStore(), StoreDTO.class);
        }
        RegisterDTO registerDTO = null;
        if (registerService.getRegister().isSet()) {
            registerDTO = modelMapper.map(registerService.getRegister(), RegisterDTO.class);
        }
        try {
            response.setView(viewService.getViewByForm(request));
        } catch (Exception e) {
            response.setView(viewService.getViewByName(ViewName.HOME));
            response.setError(e.getMessage());
            if (appDebug.equals(Boolean.TRUE)) {
                throw new Exception(e.getMessage());
            }
        }
        response.setStore(storeDTO);
        response.setRegister(registerDTO);
        if (userService.isLoggedIn()) {
            response.setBasket(basketService.getBasketArray());
            response.setTender(tenderService.getTenderArray());
            response.setUser(modelMapper.map(userService.getUser(), UserDTO.class));
        }
        response.setUiTranslations(userInterfaceService.getUserInterfaceTranslations());
        return response;
    }

}
