package cn.cloud9.alipay;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月30日 下午 09:40
 */
public class AlipayConfig {
    //应用的ID，申请时的APPID
    public static String app_id = "2021000119652452";
    //SHA256withRsa对应支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqOQW5KIQhB/4YrcqgFjdJDlh+ntb4TmuDepNdsCKKrIPIcc+A82czF51Q4QzJhK16dwL9zQHAV6DOD5SKJsAgDZL5qInEY/ihy7nKIWFYlH6TPRp4yNump0Js2i7TYeKLd24SAl6lA1TfZVu+zKjMV8vygZc/5b4flLjmVuz/dZWwD1PqQ+5dybKevu02Kx57fhdMaXTkAV4AObMCL6qknmVJupYmQLvHs6nTOKSelHeZdRB+/uyET3he94C5+kV9Vxa/oo9p5lMNisdQrptGiBbzPA0QECXw12f0mwbyI4VzY1F9hyQZHmjFF370TsdIljKwHosxdBBX7K4HkFedQIDAQAB";
    //签名方式
    public static String sign_type = "RSA2";
    //字码编码格式
    public static String charset = "utf-8";
}
