package com.zw.Filter;

import com.zw.Model.OutPutJson;
import com.zw.Util.Pass;
import com.zw.Util.ZwGson;
import com.zw.Util.ZwJWT;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InterceptorConfig implements Filter {
    //登录界面  登录接口   主页  错误页面  分享页面  下载的scoket  文件下载  分享文件下载
    private String[] includeUrls = new String[]{"/Login","/SignIn","/Home","/error","/Share","/websocket","/DownloadFile","/Share1"};
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if(!request.getHeader("Host").equals("www.zengwei123.top:8089")){
            /**来源不对**/
            response.getWriter().write(ZwGson.GsonString(new OutPutJson<String>("error","1","请求非法")));
        }else{
            if(!isNeedFilter(request.getServletPath())){
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                try {
                    String zwToken=request.getHeader("zwToken");
                    if(ZwJWT.deToken(zwToken).getClaim("username").asString().equals("zengwei")&&Pass.getPass().getPassWord(ZwJWT.deToken(zwToken).getClaim("password").asString())){
                        filterChain.doFilter(servletRequest, servletResponse);
                    }else{
                        response.getWriter().write(ZwGson.GsonString(new OutPutJson<String>("error","1","请求非法")));
                    }
                }catch (Exception e){
                    response.getWriter().write(ZwGson.GsonString(new OutPutJson<String>("error","1","请求非法")));
                }
            }
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     * @param uri
     */
    public boolean isNeedFilter(String uri) {
        for (String includeUrl : includeUrls) {
            if(includeUrl.equals(uri)) {
                return false;
            }
        }
        try {
            if(uri.split("/")[1].equals("static")){
                return false;
            }
        }catch (Exception E){
            return false;
        }
        return true;
    }
}
