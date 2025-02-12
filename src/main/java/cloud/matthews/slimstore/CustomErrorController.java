package cloud.matthews.slimstore;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    private final Boolean includeStackTrace = true;

    @RequestMapping("/error")
    public Map<String, Object> error(WebRequest request, HttpServletResponse response) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults()
            .including(ErrorAttributeOptions.Include.MESSAGE)
            .including(ErrorAttributeOptions.Include.EXCEPTION)
            .including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        if(includeStackTrace){
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        return this.errorAttributes.getErrorAttributes(request, options);
    }

}
