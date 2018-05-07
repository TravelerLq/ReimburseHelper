package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

/**
 * Created by liqing on 18/5/2.
 */

public class VerifyCertUtil {

    //验证证书
    public static JSONObject verifyCert(int userId, String cert) {
        JSONObject jsonObject = new JSONObject();
        //  String cer = "MIIFoTCCBImgAwIBAgIQVJ0Sz/iTeohK2DFGLQzv7jANBgkqhkiG9w0BAQsFADAzMQswCQYDVQQGEwJDTjERMA8GA1UECgwIVW5pVHJ1c3QxETAPBgNVBAMMCFNIRUNBIEcyMB4XDTE4MDQyMDA2NDcxM1oXDTE4MDcyMDE1NTk1OVowNDELMAkGA1UEBhMCQ04xDzANBgNVBAMMBueOi+aclTEUMBIGA1UEFBMLMTgyMDUxODg5ODEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDgfP1huFVaRQKqUenC8Nvphq7IaNAhp0mh0AUveksiIe1eyQ3Q/pdRR4RVPB98cXW6TrazhDeGvvj9mwRXy1xPunvYymtn43aH20f8l6ojzJMwRZvTMxwxp+g4V5qLegUL1wwfzNn3YFaAEmaxJ/hgB0zx6YsqO3O39PyO58NWB0kW0ThMJ5wsgmWKd8sq5Wri57V05L6T15fok+LljrDS9Pzbi6MEY+eFir544N9tOXZkV+VFj+jPnLQrguBB3Hy2FCy8aaIEEoGA6SCG1akJ6F6VXyoxyfG8Y5JUM7ODusFGFBxE37AanhITkkV2s76SUT7j78QSHyqnsfmLR0O3AgMBAAGjggKuMIICqjAfBgNVHSMEGDAWgBRWiN7jGEOCt3KkJutEqWLQh8SsJjAdBgNVHQ4EFgQUReh5qEgmH8EgzjVKFZef/BRYjYswCwYDVR0PBAQDAgbAMB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDBCBgNVHSAEOzA5MDcGCSqBHAGG7zqBFTAqMCgGCCsGAQUFBwIBFhxodHRwOi8vd3d3LnNoZWNhLmNvbS9wb2xpY3kvMAkGA1UdEwQCMAAwfQYIKwYBBQUHAQEEcTBvMDgGCCsGAQUFBzABhixodHRwOi8vb2NzcDMuc2hlY2EuY29tL29jc3Avc2hlY2Evc2hlY2Eub2NzcDAzBggrBgEFBQcwAoYnaHR0cDovL2xkYXAyLnNoZWNhLmNvbS9yb290L3NoZWNhZzIuZGVyMIHcBgNVHR8EgdQwgdEwNaAzoDGGL2h0dHA6Ly9sZGFwMi5zaGVjYS5jb20vQ0EyMDAxMS9SQTkwMzEvQ1JMMTMuY3JsMIGXoIGUoIGRhoGObGRhcDovL2xkYXAyLnNoZWNhLmNvbTozODkvY249Q1JMMTMuY3JsLG91PVJBOTAzMSxvdT1DQTIwMDExLG91PWNybCxvPVVuaVRydXN0P2NlcnRpZmljYXRlUmV2b2NhdGlvbkxpc3Q/YmFzZT9vYmplY3RDbGFzcz1jUkxEaXN0cmlidXRpb25Qb2ludDCBjgYGKoEcAcU4BIGDMIGAMEkGCCqBHAHFOIEQBD1sZGFwOi8vbGRhcDIuc2hlY2EuY29tL291PXNoZWNhIGNlcnRpZmljYXRlIGNoYWluLG89c2hlY2EuY29tMBEGCCqBHAHFOIETBAU2NjA1NTAgBggqgRwBxTiBFAQUU0YzMjAxMTMxOTk0MDcyNjY0MTAwDQYJKoZIhvcNAQELBQADggEBAJBaUhxFK5kf0wfstW7v7AFvInmNPz88y89EJNKh4iiLwuof+d9GBqB2uhvGVf7TrlNG42b4Iy/9DTgE3HHuKOjgUijFJW5/VbFyjZT7m6geMB2OFqwNwDX+1D34+e8QWMRI1seBvhsEiKle7KNI5Xv7nNMnJu1icOcSqO0kL2labqADk0ft+VYXvIZG27VQJvt9WgfnS0+gFGsjTsuRt03p74FwhWacZlFD3vKG/YLkX9Ju4nbWJbYQBC0BZShpMiP6Lc8Qwy7gLjQkCDuslJJIgWq5BRaGknWjOOv5mkGYkvl5AdTB6Lsj9k/9OiGT4stj4OVP8D47yHzBvTL7RHE=";
        jsonObject.put("userId", userId);
        jsonObject.put("cer", cert);

        String url = RootAddress + "yuanshensystem/sign/verifcert";
        //上传到接口后，接口返回的数据
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
    }
}