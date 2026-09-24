package cloud.matthews.slimstore.view;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.view.View.ViewName;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final HttpServletRequest request;
    private final ViewTranslator viewTranslator;
    private final ViewConfig viewConfig;
    private final List<ViewEnricher> viewEnrichers;

    private Map<ViewName, ViewEnricher> enrichersByViewName;

    private Map<ViewName, ViewEnricher> enrichersByViewName() {
        if (enrichersByViewName == null) {
            enrichersByViewName = viewEnrichers.stream()
                .collect(Collectors.toMap(ViewEnricher::supports, Function.identity()));
        }
        return enrichersByViewName;
    }

    private View enrichView(
        View view,
        Form requestForm
    ) throws Exception {
        ViewEnricher enricher = enrichersByViewName().get(view.getName());
        if (enricher != null) {
            enricher.enrich(view, requestForm);
        }
        return view;
    }

    public View getViewByForm(
        Form requestForm
    ) throws Exception {
        ViewName viewName = requestForm.getTargetView() == null ? ViewName.HOME : requestForm.getTargetView();
        View view = getViewByName(viewName);
        return enrichView(view, requestForm);
    }

    public View getViewByName(
        ViewName viewName
    ) {
        View view = viewConfig.getView(viewName);
        view.setLocale(request.getLocale());
        view.setCacheKey(view.getName() + ":" + view.getLocale().toString());
        view = viewTranslator.translateView(view);
        return view;
    }

}
