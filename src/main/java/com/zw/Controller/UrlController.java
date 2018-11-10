package com.zw.Controller;

import com.zw.Util.ZwJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UrlController {

    @GetMapping("error")
    public String Error(){
        return "errorpage/error";
    }

    @GetMapping("Home")
    public String Home(HttpSession session){
        if(session.getAttribute("ZwToken")!=null){
            if(ZwJWT.isAdoptRequest((String) session.getAttribute("ZwToken"))){
                return "Home";
            }
            return "Login";
        }else{
            return "Login";
        }
    }

    @GetMapping("Login")
    public String Login(){
        return "Login";
    }

    @GetMapping("Share")
    public String Share(){
        return "Share";
    }
}
