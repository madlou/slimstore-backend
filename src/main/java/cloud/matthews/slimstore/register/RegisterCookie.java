package cloud.matthews.slimstore.register;

import jakarta.servlet.http.Cookie;
import lombok.Data;

@Data
public class RegisterCookie {

    private Integer store;
    private Integer register;

    public void set(Cookie cookie){
        if(cookie != null){
            set(cookie.getValue());
        }
    }

    public void set(String cookie){
        if (cookie != null) {
            String[] cookieSplit = cookie.split("-");
            if (cookieSplit[1] != null) {
                store = Integer.parseInt(cookieSplit[0]);
                register = Integer.parseInt(cookieSplit[1]);
            }
        }
    }

    public Boolean isValid(){
        if(store != null && register != null){
            return true;
        }
        return false;
    }

}
