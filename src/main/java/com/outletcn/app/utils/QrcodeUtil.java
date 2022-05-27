package com.outletcn.app.utils;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.iherus.codegen.qrcode.SimpleQrcodeGenerator;
import org.iherus.codegen.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

public class QrcodeUtil {

    private static final String QRCODE_HEADER = "data:image/png;base64,";

    public static String getQrcodeBase64(String content) throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        try {
            new SimpleQrcodeGenerator().generate(content).toStream(o);
            byte[] array = o.toByteArray();
            String qrcodeBase64 = Base64.getEncoder().encodeToString(array).trim();
            return QRCODE_HEADER + qrcodeBase64;
        } finally {
            IOUtils.closeQuietly(o);
        }
    }

    public static ByteArrayOutputStream outputStream(String content) throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        try {
            new SimpleQrcodeGenerator().generate(content).toStream(o);
            return o;
        } finally {
            IOUtils.closeQuietly(o);
        }
    }

/*    public static void main(String[] args) {
        try {
            System.out.println(getQrcodeBase64("https://baidu.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
