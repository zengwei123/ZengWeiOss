package com.zw.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zw.Aliyun.Operation.OssOperation;
import com.zw.Model.OutPutJson;
import com.zw.Service.LoginService;
import com.zw.Util.ZwGson;
import com.zw.Util.ZwJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OssOperation ossOperation;

    @ResponseBody
    @PostMapping("/SignIn")
    public OutPutJson login(@RequestBody String Json) {
        String UserName=ZwGson.Json(Json,"UserName");
        String PassWord=ZwGson.Json(Json,"PassWord");
        HttpSession session = request.getSession(true);
        if(loginService.getPasswordByUsername(UserName,PassWord)){
            String token=ZwJWT.getToken(UserName,PassWord);
            if(session!=null){
                System.out.println("nihao");
                session.setAttribute("ZwToken",token);
            }
            return new OutPutJson<String>("success","0",token);
        }else{
            return new OutPutJson<String>("error","1","用户名或密码错误");
        }
    }

    /**根目录**/
    @ResponseBody
    @PostMapping("/FatherQueryFile")
    public OutPutJson FatherQueryFile(){
        List<String> keys=ossOperation.QueryFile("",new ArrayList<String>());
            return new OutPutJson("success","0",keys);
    }
    /**根目录**/
    @ResponseBody
    @PostMapping("/SonQueryFile")
    public OutPutJson SonQueryFile(@RequestBody String Json){
        String name=ZwGson.Json(Json,"name");
        List<String> keys=ossOperation.QueryFile(name+"/",new ArrayList<String>());
        return new OutPutJson("success","0",keys);
    }
    /**所有文件**/
    @ResponseBody
    @PostMapping("/QueryFiles")
    public OutPutJson QueryFiles(){
        List<String> keys=ossOperation.QueryFiles(new ArrayList<String>());
        return new OutPutJson("success","0",keys);
    }
    /**新建文件夹**/
    @ResponseBody
    @PostMapping("/PutFolder")
    public OutPutJson PutFolder(@RequestBody String Json){
        String FolderName=ZwGson.Json(Json,"FolderName");
        if(ossOperation.PutFolder(FolderName)){
            return new OutPutJson("success","0","成功");
        }else{
            return new OutPutJson("error","1","创建文件夹失败");
        }
    }

    /**文件夹删除**/
    @ResponseBody
    @PostMapping("/DeleteFile_1")
    public OutPutJson DeleteFile_1(@RequestBody String Json){
        String FileName=ZwGson.Json(Json,"FileName");
        if(ossOperation.DeleteFile_1(FileName)){
            return new OutPutJson("success","0","成功");
        }else{
            return new OutPutJson("error","1","删除失败");
        }
    }

    /**文件删除**/
    @ResponseBody
    @PostMapping("/DeleteFile_2")
    public OutPutJson DeleteFile_2(@RequestBody String Json){
        List<String> FileName=ZwGson.Json1(Json,"list");
        if(FileName.size()>0){
            if(ossOperation.DeleteFile_2(FileName)){
                return new OutPutJson("success","0","成功");
            }
        }
        return new OutPutJson("error","1","删除失败");
    }

    /**文件上传**/
    @ResponseBody
    @PostMapping("/PutFile")
    public OutPutJson PutFile( @RequestParam("file")MultipartFile file,@RequestParam("path")String path){
        if(file!=null){
            try {
                if(ossOperation.PutFile(path,file.getInputStream(),file.getBytes().length)){
                    return new OutPutJson("success","0","成功");
                }else{
                    return new OutPutJson("error","1","删除失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new OutPutJson("error","1","上传失败");
            }
        }else{
            return new OutPutJson("error","1","上传失败");
        }
    }

    /**文件下载**/
    @ResponseBody
    @GetMapping ("/DownloadFile")
    public void DownloadFile(HttpServletResponse res,@RequestParam("file") String file) throws IOException {
      //  String FileName=ZwGson.Json(Json,"FileName");
        byte[] buff = new byte[1024];
        InputStream in=ossOperation.DownloadFile(file);
        BufferedInputStream bis = null;
        OutputStream os = null;
        int len=0;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(in);
            while ((len=in.read(buff))>0) {
                os.write(buff, 0, len);
            }
        } catch (IOException e) {

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**文件分享**/
    @ResponseBody
    @PostMapping ("/Share")
    public OutPutJson Share(@RequestBody String Json){
        try {
            String FileName=ZwGson.Json(Json,"FileName");
            return new OutPutJson("success","0",ZwJWT.getShareToken(FileName));
        }catch (Exception e){
            return new OutPutJson("error","0","生成链接失败");
        }
    }
    /**分享下载**/
    @ResponseBody
    @PostMapping ("/Share1")
    public OutPutJson Share1(@RequestBody String Json){
        try {
            String shareToken=ZwGson.Json(Json,"shareToken");
            DecodedJWT urls=ZwJWT.deToken(shareToken);
            return new OutPutJson("success","0",ossOperation.Share(urls.getClaim("urls").asString(),5));
        }catch (Exception e){
            return new OutPutJson("error","0","生成下载链接失败");
        }
    }
}
