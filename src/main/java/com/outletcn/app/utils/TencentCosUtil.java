package com.outletcn.app.utils;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.common.Credential;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云OSS工具类
 */
@Slf4j
public class TencentCosUtil {

    //签名过期。Timestamp和服务器时间1652429111相差过大，请使用本地时间并注意开启NTP服务进行时间同步。

    /**
     * SecretId: AKIDwiHacNt8GBpDKAj23nGSN0MRFEwNXQ2N
     * SecretKey: 7xVmmW2HFxPaU8VbPwDF0UifLjriIbdP
     */
    private static final String REGION = "ap-chongqing";

    private static final String ACCESS_KEY_ID = "AKIDwiHacNt8GBpDKAj23nGSN0MRFEwNXQ2N";

    private static final String ACCESS_KEY_SECRET = "7xVmmW2HFxPaU8VbPwDF0UifLjriIbdP";

    private static final String APP_ID = "1311883259";
    /**
     * 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
     */
    private static final String BUCKET_NAME = "test-" + APP_ID;


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
        // clientConfig.setHttpProxyIp("httpProxyIp");
        // clientConfig.setHttpProxyPort(80);

        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    /**
     * 创建 TransferManager 实例，这个实例用来后续调用高级接口
     */
    public static TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        COSClient cosClient = cosClient();

        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(16);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }


    /**
     * 获取临时密钥
     */
    public static Response getCredential() {

        TreeMap<String, Object> config = new TreeMap<>();

        try {
            // 云 api 密钥 SecretId
            config.put("secretId", ACCESS_KEY_ID);
            // 云 api 密钥 SecretKey
            config.put("secretKey", ACCESS_KEY_SECRET);

            // 设置域名
//            config.put("host", "sts.internal.tencentcloudapi.com");

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成你的 bucket
            config.put("bucket", BUCKET_NAME);
            // 换成 bucket 所在地区
            config.put("region", REGION);

            // 可以通过 allowPrefixes 指定前缀数组, 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefixes", new String[]{
                    "exampleobject",
                    "exampleobject2"
            });

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            Response response = CosStsClient.getCredential(config);
            System.out.println(response.credentials.tmpSecretId);
            System.out.println(response.credentials.tmpSecretKey);
            System.out.println(response.credentials.sessionToken);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }


    /**
     * @param file 文件
     * @param key  对象键(Key)是对象在存储桶中的唯一标识。
     */
    public static void upload(File file, String key) {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, file);
        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);

            showTransferProgress(upload);

            UploadResult uploadResult = upload.waitForUploadResult();

            System.out.println(JSON.toJSONString(uploadResult, true));


        } catch (CosClientException | InterruptedException e) {
            e.printStackTrace();
        }
        // 确定本进程不再使用 transferManager 实例之后，关闭之
        shutdownTransferManager(transferManager);
    }

    /**
     * @param key 对象键(Key)是对象在存储桶中的唯一标识。
     */
    public static String upload(InputStream inputStream, String key) {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        try {
            objectMetadata.setContentLength(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, inputStream, objectMetadata);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            showTransferProgress(upload);
            UploadResult uploadResult = upload.waitForUploadResult();

            System.out.println(JSON.toJSONString(uploadResult, true));

        } catch (CosClientException | InterruptedException e) {
            e.printStackTrace();
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之

        shutdownTransferManager(transferManager);

        return getUrl(key);
    }


    /**
     * @param key 对象键(Key)是对象在存储桶中的唯一标识。
     * @return 对象 url 连接
     */
    public static String getUrl(String key) {
        COSClient cosClient = cosClient();
        URL objectUrl = cosClient.getObjectUrl(BUCKET_NAME, key);
        cosClient.shutdown();
        return objectUrl.toString();
    }


    /**
     * 创建存储桶
     */
    public static Bucket createBucket(String bucketName) {
        COSClient cosClient = cosClient();
        Bucket bucket = cosClient.createBucket(bucketName);
        cosClient.shutdown();
        return bucket;
    }

    /**
     * 是否存在存储桶
     *
     * @param bucketName
     * @return
     */
    public static boolean doesBucketExist(String bucketName) {
        COSClient cosClient = cosClient();
        boolean exists = cosClient.doesBucketExist(bucketName);
        cosClient.shutdown();
        return exists;
    }

    /**
     * 删除存储桶
     */
    public static void deleteBucket(String bucketName) {
        COSClient cosClient = cosClient();
        cosClient.deleteBucket(bucketName);
        cosClient.shutdown();
    }

    /**
     * 获取存储桶列表
     *
     * @return
     */
    public static List<Bucket> listBuckets() {
        COSClient cosClient = cosClient();
        List<Bucket> buckets = cosClient.listBuckets();
        cosClient.shutdown();
        return buckets;
    }


    /**
     * 判断是否存在该文件
     *
     * @param key        对象键(Key)是对象在存储桶中的唯一标识。
     * @param bucketName 存储桶名称
     * @return 是否存在¬
     */
    public static boolean doesObjectExist(String bucketName, String key) {
        COSClient cosClient = cosClient();
        boolean exists = cosClient.doesObjectExist(bucketName, key);
        cosClient.shutdown();
        return exists;
    }

    /**
     * 显示上传进度
     *
     * @param transfer
     */
    public static void showTransferProgress(Transfer transfer) {
        // 这里的 Transfer 是异步上传结果 Upload 的父类
        System.out.println(transfer.getDescription());

        // transfer.isDone() 查询上传是否已经完成
        while (!transfer.isDone()) {
            try {
                // 每 2 秒获取一次进度
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            TransferProgress progress = transfer.getProgress();
            long sofar = progress.getBytesTransferred();
            long total = progress.getTotalBytesToTransfer();
            double pct = progress.getPercentTransferred();
            System.out.printf("upload progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
        }

        // 完成了 Completed，或者失败了 Failed
        System.out.println(transfer.getState());
    }


    /**
     * 关闭 TransferManager
     *
     * @param transferManager TransferManager
     */
    public static void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }

    /**
     * 获取文件名
     *
     * @param args
     */
    public static String getFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid + originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    public static void main(String[] args) {
//        upload();
//        getlist();


        getCredential();


    }
}
