package com.outletcn.app.utils;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 腾讯云OSS工具类
 */
public class TencentCosUtil {

    /**
     * SecretId: AKIDwiHacNt8GBpDKAj23nGSN0MRFEwNXQ2N
     * SecretKey: 7xVmmW2HFxPaU8VbPwDF0UifLjriIbdP
     */
    private static final String REGION = "ap-chongqing";

    private static final String ACCESS_KEY_ID = "AKIDwiHacNt8GBpDKAj23nGSN0MRFEwNXQ2N";

    private static final String ACCESS_KEY_SECRET = "7xVmmW2HFxPaU8VbPwDF0UifLjriIbdP";

    private static final String BUCKET_NAME = "test-1311883259";


    public static COSClient cosClient() {
        // 创建 COSClient 实例，这个实例用来后续调用请求

        // 设置用户身份信息。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理

        COSCredentials cred = new BasicCOSCredentials(ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        ClientConfig clientConfig = new ClientConfig();

        // 设置 bucket 的地域
        // COS_REGION 请参照 https://cloud.tencent.com/document/product/436/6224
        clientConfig.setRegion(new Region(REGION));

        // 设置请求协议, http 或者 https
        // 5.6.53 及更低的版本，建议设置使用 https 协议
        // 5.6.54 及更高版本，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 以下的设置，是可选的：

        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30 * 1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30 * 1000);

        // 如果需要的话，设置 http 代理，ip 以及 port
//            clientConfig.setHttpProxyIp("httpProxyIp");
//            clientConfig.setHttpProxyPort(80);

        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    public static void upload() {

        COSClient cosClient = cosClient();

        // 指定要上传的文件
        File localFile = new File("/Users/felix/Desktop/test.pdf");
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "1.pdf";

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localFile);

        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println(JSON.toJSONString(putObjectResult,true));
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }

        URL objectUrl = cosClient.getObjectUrl(BUCKET_NAME, key);

        System.out.println("objectUrl "+objectUrl.toString());
    }


    public static void getlist() {
        // 如果只调用 listBuckets 方法，则创建 cosClient 时指定 region 为 new Region("") 即可
        List<Bucket> buckets = cosClient().listBuckets();
        for (Bucket bucketElement : buckets) {
            String bucketName = bucketElement.getName();
            String bucketLocation = bucketElement.getLocation();

            System.out.println(bucketName + " " + bucketLocation);
        }
    }

    public static void main(String[] args) {
        upload();
//        getlist();
        COSClient cosClient = cosClient();

        ObjectListing objectListing = cosClient.listObjects(BUCKET_NAME);

        List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();

        System.out.println(JSON.toJSONString(cosObjectSummaries,true));


//        cosClient.shutdown();

    }
}
