package com.zw.Aliyun;

import com.aliyun.oss.OSSClient;

public class ZwOSSClient {
    private static ZwOSSClient zwOSSClient=new ZwOSSClient();
    private ZwOSSClient(){}
    public static ZwOSSClient getZwOSSClient(){
        return zwOSSClient;
    }

    private static final String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId = "LTAIgtBNxOMMrr3B";
    private static final String accessKeySecret = "KB2zXeSnWWIB2qKUiCeIiERXg5FmOL";
    public static final String bucketName = "zengwei123";
    private static OSSClient ossClient;
    public OSSClient getOssClient() {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }
        return ossClient;
    }
    public void shutdown(){
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}
