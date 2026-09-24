package cloud.matthews.slimstore.store;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.translation.TranslationService;
import lombok.RequiredArgsConstructor;

/**
 * Shared helper for view enrichers that need to render the list of stores as
 * "value|label" form options (used by the user edit/list views).
 */
@Component
@RequiredArgsConstructor
public class StoreOptionsProvider {

    private final StoreService storeService;
    private final TranslationService translationService;

    public String[] getStoreOptions(
        Boolean showNoStoreOption
    ) {
        Iterable<Store> stores = storeService.getStores();
        ArrayList<String> storeOptions = new ArrayList<String>();
        if (showNoStoreOption) {
            storeOptions.add("0|" + translationService.translate("ui.no_store"));
        }
        for (Store str : stores) {
            storeOptions.add(str.getNumber() + "|" + str.getNumber() + ": " + str.getName());
        }
        return storeOptions.toArray(new String[0]);
    }

}
