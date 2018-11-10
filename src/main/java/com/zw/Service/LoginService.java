package com.zw.Service;

import com.zw.Util.Pass;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean getPasswordByUsername(String username,String password){
       if(username.equals("zengwei")){
            return Pass.getPass().getPassWord(password);
        }else{
           return false;
       }
    }
}
