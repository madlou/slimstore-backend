package cloud.matthews.slimstore.pos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.RegisterDTO;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.StoreDTO;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.translation.UserInterfaceService;
import cloud.matthews.slimstore.user.UserDTO;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewService;
import lombok.RequiredArgsConstructor;

/**
 * Builds the {@link PosResponseDTO} snapshot returned to the UI after
 * each request. This is the one place that legitimately needs to read from
 * every domain package, since the response is a full-screen aggregate view.
 */
@Component
@RequiredArgsConstructor
public class PosResponseAssembler {

    private final RegisterService registerService;
    private final StoreService storeService;
    private final UserService userService;
    private final BasketService basketService;
    private final TenderService tenderService;
    private final ViewService viewService;
    private final UserInterfaceService userInterfaceService;
    private final ModelMapper modelMapper;

    public PosResponseDTO assemble(
        PosRequestDTO request,
        PosResponseDTO response,
        Boolean appDebug
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
