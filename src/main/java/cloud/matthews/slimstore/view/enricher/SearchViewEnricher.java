package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.product.ProductService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchViewEnricher implements ViewEnricher {

    private final ProductService productService;

    @Override
    public ViewName supports() {
        return ViewName.SEARCH;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        String searchQuery = requestForm.getValueByKey("search");
        view.getForm().setElements(productService.search(searchQuery));
    }

}
