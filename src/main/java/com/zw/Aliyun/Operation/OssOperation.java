package com.zw.Aliyun.Operation;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.*;
import com.zw.Aliyun.ZwOSSClient;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.List;

@Service
public class OssOperation {
    /**上传文件**/
   public boolean PutFile(String OssFile,InputStream inputStream,long size){
       try {
           // 带进度条的上传。
           PutObjectProgressListener.size=size;
           ZwOSSClient.getZwOSSClient().getOssClient().putObject(new PutObjectRequest(ZwOSSClient.bucketName,
                       OssFile, inputStream).<PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
           // 关闭OSSClient。
           //ZwOSSClient.getZwOSSClient().getOssClient().shutdown();
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }
    /**创建文件夹**/
   public boolean PutFolder(String FolderName){
       /*
        * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
        * 照样可以上传下载,只是控制台会对以"/"结尾的Object以文件夹的方式展示,用户可以利用这种方式来实现文件夹模拟功能,创建形式上的文件夹
        */
       try {
           ObjectMetadata objectMeta = new ObjectMetadata();
           byte[] buffer = new byte[0];
           ByteArrayInputStream in = new ByteArrayInputStream(buffer);
           objectMeta.setContentLength(0);
           ZwOSSClient.getZwOSSClient().getOssClient().putObject(ZwOSSClient.bucketName, FolderName+"/", in,objectMeta);
           // 关闭OSSClient。
           //ZwOSSClient.getZwOSSClient().getOssClient().shutdown();
           return true;
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }

   }
    /**下载文件**/
   public InputStream DownloadFile(String OssFile){
       try {
           // 带进度条的下载。
           OSSObject ossObject=ZwOSSClient.getZwOSSClient().getOssClient().getObject(new GetObjectRequest(ZwOSSClient.bucketName, OssFile).<GetObjectRequest>withProgressListener(new PutObjectProgressListener()));
          InputStream inputStream=ossObject.getObjectContent();
          return inputStream;
       } catch (Exception e) {
           return null;
       }
       // 关闭OSSClient。
       //ZwOSSClient.getZwOSSClient().getOssClient().shutdown();
    }
    /**批量删除文件夹及文件夹中的文件**/
   public boolean DeleteFile_1(String str) {
       try {
           List<String> arrayList=QueryFile(str,new ArrayList<String>());
           for(int i=0;i<arrayList.size();i++){
               char c=arrayList.get(i).charAt(arrayList.get(i).length()-1);
               if(c=='/'){
                   DeleteFile_1(arrayList.get(i));
               }
           }
           if(arrayList.size()>0){
               DeleteObjectsResult deleteObjectsResult =  ZwOSSClient.getZwOSSClient().getOssClient().deleteObjects(new DeleteObjectsRequest(ZwOSSClient.bucketName).withKeys(arrayList));
               List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
           }
           ZwOSSClient.getZwOSSClient().getOssClient().deleteObject(ZwOSSClient.bucketName,str);
           //ZwOSSClient.getZwOSSClient().getOssClient().shutdown();
           return true;
       }catch (Exception E){
           E.printStackTrace();
           return false;
       }
    }
    /**单独的文件删除**/
    public boolean DeleteFile_2(List<String> arrayList) {
        try {
            DeleteObjectsResult deleteObjectsResult =  ZwOSSClient.getZwOSSClient().getOssClient().deleteObjects(new DeleteObjectsRequest(ZwOSSClient.bucketName).withKeys(arrayList));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
            return true;
        }catch (Exception E){
            E.printStackTrace();
            return false;
        }
    }
    /**列出所有文件**/
   public List<String> QueryFile(String str,List<String> list){
       // 构造ListObjectsRequest请求。
       ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ZwOSSClient.bucketName);
       // 设置正斜线（/）为文件夹的分隔符。
       listObjectsRequest.setDelimiter("/");
       // 列出fun目录下的所有文件和文件夹。
       listObjectsRequest.setPrefix(str);
       ObjectListing listing = ZwOSSClient.getZwOSSClient().getOssClient().listObjects(listObjectsRequest);
      // System.out.println("根目录文件夹:");
       for (String commonPrefix : listing.getCommonPrefixes()) {
           list.add(commonPrefix);
       }
      // System.out.println("根目录文件:");
       for (OSSObjectSummary objectSummary  : listing.getObjectSummaries()) {
           if(!str.equals(objectSummary.getKey())){
               list.add(objectSummary.getKey());
           }
       }
       // 关闭OSSClient。
      // ZwOSSClient.getZwOSSClient().getOssClient().shutdown();
       return list;
   }
    /**列出所有文件**/
    public List<String> QueryFiles(List<String> list){
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ZwOSSClient.bucketName);
        // 列出文件。
        ObjectListing listing =  ZwOSSClient.getZwOSSClient().getOssClient().listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            if(!objectSummary.getKey().substring(objectSummary.getKey().length()-1,objectSummary.getKey().length()).equals("/")){
                list.add(objectSummary.getKey());
            }
        }
        return list;
    }
   /**分享文件链接**/
   public URL  Share(String OssFile,int i){
       try {
           if(i<1){
               i=1;
           }
           Date expiration = new Date(new Date().getTime() + 3600 * i * 1000);
           GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ZwOSSClient.bucketName,OssFile, HttpMethod.GET);
            // 设置过期时间。
           request.setExpiration(expiration);
            // 生成签名URL（HTTP GET请求）。
           URL signedUrl = ZwOSSClient.getZwOSSClient().getOssClient() .generatePresignedUrl(request);
            // 使用签名URL发送请求。
           Map<String, String> customHeaders = new HashMap<String, String>();
            // 添加GetObject请求头。
           customHeaders.put("Range", "bytes=100-1000");
           OSSObject object = ZwOSSClient.getZwOSSClient().getOssClient().getObject(signedUrl,customHeaders);
            // 关闭OSSClient。
           return signedUrl;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   public static  void main(String[] a){
       List<String> list=new OssOperation().QueryFiles(new ArrayList<String>());
       for (String s:list){
           System.out.println(s);
       }
   }
}
