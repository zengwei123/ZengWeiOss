import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;

public class Test {
    public static  void main(String[] a){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAIgtBNxOMMrr3B";
        String accessKeySecret = "KB2zXeSnWWIB2qKUiCeIiERXg5FmOL";
        String bucketName = "zengwei123";
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        //ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        //ossClient.shutdown();
        // 创建OSSClient实例。
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        ObjectMetadata objectMeta = new ObjectMetadata();
        /*
         * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
         * 照样可以上传下载,只是控制台会对以"/"结尾的Object以文件夹的方式展示,用户可以利用这种方式来实现文件夹模拟功能,创建形式上的文件夹
         */
        byte[] buffer = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        objectMeta.setContentLength(0);
        ossClient.putObject(bucketName, "users/", in,objectMeta);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
