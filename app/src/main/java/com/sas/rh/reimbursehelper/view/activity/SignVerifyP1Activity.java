package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileToBase64Util;

import java.io.File;
import java.util.List;

import cn.unitid.spark.cm.sdk.business.SignatureP1Service;
import cn.unitid.spark.cm.sdk.business.VerifyService;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * 演示p1 签名验证
 * 先把文件转为base64 －进行p1签名 －然后再将Base64转为 sas文件上传
 */

public class SignVerifyP1Activity extends FragmentActivity {

    //将图片转为
    private EditText inpuData;
    private EditText ciphertext;
    private EditText cert;
    private EditText content;
    private android.support.v4.app.FragmentManager manager;
    private ImageView ivPic;
    private List<String> photos = null;//已选择照片的张数
    private SharedPreferencesUtil spu;
    private Button sigtwo;
    private String base64Two;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_sign_verify);
        spu = new SharedPreferencesUtil(SignVerifyP1Activity.this);

        manager = this.getSupportFragmentManager();

        //签名原文(需要)
        String data = "/9j/4AAQSkZJRgABAQAAAQABAAD/4TGJRXhpZgAASUkqAAgAAAAHABoBBQABAAAAYgAAABsBBQAB\n" +
                "                                                                   AAAAagAAACgBAwABAAAAAgAAADIBAgAUAAAAcgAAABMCAwABAAAAAQAAAGmHBAABAAAAhgAAACWI\n" +
                "                                                                   BAABAAAA6AAAACoBAABIAAAAAQAAAEgAAAABAAAAMjAxODowMzoyMSAxNjozMzo1NQAHAACQBwAE\n" +
                "                                                                   AAAAMDIxMAGRBwAEAAAAAQIDAAqSBQABAAAA4AAAAACgBwAEAAAAMDEwMAGgAwABAAAA//8AAAKg\n" +
                "                                                                   BAABAAAAgAIAAAOgBAABAAAA4AEAAAAAAADWEAAA6AMAAAIABwAFAAMAAAAGAQAAHQACAAsAAAAe\n" +
                "                                                                   AQAAAAAAAAgAAAABAAAAIQAAAAEAAAA3AAAAAQAAADIwMTg6MDM6MjEAAAIAAQIEAAEAAABIAQAA\n" +
                "                                                                   AgIEAAEAAAA5MAAAAAAAAP/Y/+AAEEpGSUYAAQEAAAEAAQAA/9sAQwACAQEBAQECAQEBAgICAgIE\n" +
                "                                                                   AwICAgIFBAQDBAYFBgYGBQYGBgcJCAYHCQcGBggLCAkKCgoKCgYICwwLCgwJCgoK/9sAQwECAgIC\n" +
                "                                                                   AgIFAwMFCgcGBwoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoK\n" +
                "                                                                   CgoK/8AAEQgA8AFAAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//E\n" +
                "                                                                   ALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJ\n" +
                "                                                                   ChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeI\n" +
                "                                                                   iYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq\n" +
                "                                                                   8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQH\n" +
                "                                                                   BQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJico\n" +
                "                                                                   KSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZ\n" +
                "                                                                   mqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/a\n" +
                "                                                                   AAwDAQACEQMRAD8A/B+iiigD7a/4I4f81F/7hH/t7X21XxL/AMEcP+ai/wDcI/8Ab2vtqgAooooA\n" +
                "                                                                   +Jf+Cx3/ADTr/uL/APtlXxLX21/wWO/5p1/3F/8A2yr4loAKKKKAPtr/AII4f81F/wC4R/7e19tV\n" +
                "                                                                   8S/8EcP+ai/9wj/29r7aoAKKKKAPiX/gsd/zTr/uL/8AtlXxLX21/wAFjv8AmnX/AHF//bKviWgA\n" +
                "                                                                   ooooA+2v+COH/NRf+4R/7e19tV8S/wDBHD/mov8A3CP/AG9r7aoAKKKKAPiX/gsd/wA06/7i/wD7\n" +
                "                                                                   ZV8S19tf8Fjv+adf9xf/ANsq+JaACiiigD7a/wCCOH/NRf8AuEf+3tfbVfEv/BHD/mov/cI/9va+\n" +
                "                                                                   2qACiiigD4l/4LHf806/7i//ALZV8S19tf8ABY7/AJp1/wBxf/2yr4loAKKKKAPtr/gjh/zUX/uE\n" +
                "                                                                   f+3tfbVfEv8AwRw/5qL/ANwj/wBva+2qACiiigD4l/4LHf8ANOv+4v8A+2VfEtfbX/BY7/mnX/cX\n" +
                "                                                                   /wDbKviWgAor6/8A+HQHxK/6LLon/gDNR/w6A+JX/RZdE/8AAGagDb/4I4f81F/7hH/t7X21XxL4\n" +
                "                                                                   Nx/wSe+0/wDCwz/wln/Ce7Psn9jfuPsv2Hdv3+Z97d9rXGOmw56itv8A4e//AA0/6I3rn/gdDQB9\n" +
                "                                                                   f0V8gf8AD3/4af8ARG9c/wDA6Gj/AIe//DT/AKI3rn/gdDQBif8ABY7/AJp1/wBxf/2yr4lr7a8Y\n" +
                "                                                                   4/4Kw/Z/+FeH/hE/+EC3/a/7Z/f/AGr7dt2bPL+7t+yNnPXeMdDWJ/w6A+JX/RZdE/8AAGagD5Ao\n" +
                "                                                                   r6//AOHQHxK/6LLon/gDNR/w6A+JX/RZdE/8AZqANv8A4I4f81F/7hH/ALe19tV8S+Dcf8EnvtP/\n" +
                "                                                                   AAsM/wDCWf8ACe7Psn9jfuPsv2Hdv3+Z97d9rXGOmw56itv/AIe//DT/AKI3rn/gdDQB9f0V8gf8\n" +
                "                                                                   Pf8A4af9Eb1z/wADoaP+Hv8A8NP+iN65/wCB0NAGJ/wWO/5p1/3F/wD2yr4lr7a8Y4/4Kw/Z/wDh\n" +
                "                                                                   Xh/4RP8A4QLf9r/tn9/9q+3bdmzy/u7fsjZz13jHQ1if8OgPiV/0WXRP/AGagD5Aor6//wCHQHxK\n" +
                "                                                                   /wCiy6J/4AzUf8OgPiV/0WXRP/AGagDb/wCCOH/NRf8AuEf+3tfbVfEvg3H/AASe+0/8LDP/AAln\n" +
                "                                                                   /Ce7Psn9jfuPsv2Hdv3+Z97d9rXGOmw56itv/h7/APDT/ojeuf8AgdDQB9f0V8gf8Pf/AIaf9Eb1\n" +
                "                                                                   z/wOho/4e/8Aw0/6I3rn/gdDQBif8Fjv+adf9xf/ANsq+Ja+2vGOP+CsP2f/AIV4f+ET/wCEC3/a\n" +
                "                                                                   /wC2f3/2r7dt2bPL+7t+yNnPXeMdDWJ/w6A+JX/RZdE/8AZqAPkCivr/AP4dAfEr/osuif8AgDNR\n" +
                "                                                                   /wAOgPiV/wBFl0T/AMAZqANv/gjh/wA1F/7hH/t7X21XxL4Nx/wSe+0/8LDP/CWf8J7s+yf2N+4+\n" +
                "                                                                   y/Yd2/f5n3t32tcY6bDnqK2/+Hv/AMNP+iN65/4HQ0AfX9FfIH/D3/4af9Eb1z/wOho/4e//AA0/\n" +
                "                                                                   6I3rn/gdDQBif8Fjv+adf9xf/wBsq+Ja+2vGOP8AgrD9n/4V4f8AhE/+EC3/AGv+2f3/ANq+3bdm\n" +
                "                                                                   zy/u7fsjZz13jHQ1if8ADoD4lf8ARZdE/wDAGagD5Aor6/8A+HQHxK/6LLon/gDNR/w6A+JX/RZd\n" +
                "                                                                   E/8AAGagDb/4I4f81F/7hH/t7X21XxL4Nx/wSe+0/wDCwz/wln/Ce7Psn9jfuPsv2Hdv3+Z97d9r\n" +
                "                                                                   XGOmw56itv8A4e//AA0/6I3rn/gdDQB9f0V8gf8AD3/4af8ARG9c/wDA6Gj/AIe//DT/AKI3rn/g\n" +
                "                                                                   dDQBif8ABY7/AJp1/wBxf/2yr4lr7a8Y4/4Kw/Z/+FeH/hE/+EC3/a/7Z/f/AGr7dt2bPL+7t+yN\n" +
                "                                                                   nPXeMdDWJ/w6A+JX/RZdE/8AAGagD71ooooA+Jf+Cx3/ADTr/uL/APtlXxLX21/wWO/5p1/3F/8A\n" +
                "                                                                   2yr4loAKKKKAPtr/AII4f81F/wC4R/7e19tV8S/8EcP+ai/9wj/29r7aoAKKKKAPiX/gsd/zTr/u\n" +
                "                                                                   L/8AtlXxLX21/wAFjv8AmnX/AHF//bKviWgAooooA+2v+COH/NRf+4R/7e19tV8S/wDBHD/mov8A\n" +
                "                                                                   3CP/AG9r7aoAKKKKAPiX/gsd/wA06/7i/wD7ZV8S19tf8Fjv+adf9xf/ANsq+JaACiiigD7a/wCC\n" +
                "                                                                   OH/NRf8AuEf+3tfbVfEv/BHD/mov/cI/9va+2qACiiigD4l/4LHf806/7i//ALZV8S19tf8ABY7/\n" +
                "                                                                   AJp1/wBxf/2yr4loAKKKKAPtr/gjh/zUX/uEf+3tfbVfEv8AwRw/5qL/ANwj/wBva+2qACiiigD4\n" +
                "                                                                   l/4LHf8ANOv+4v8A+2VfEtfbX/BY7/mnX/cX/wDbKviWgAooooA+2v8Agjh/zUX/ALhH/t7X21Xx\n" +
                "                                                                   L/wRw/5qL/3CP/b2vtqgAor5b/4e4fs1f9CP43/8F1n/APJdH/D3D9mr/oR/G/8A4LrP/wCS6AOJ\n" +
                "                                                                   /wCCx3/NOv8AuL/+2VfEtfbXxj/42p/2d/wzz/xJ/wDhBPO/tb/hMv8AR/O+27PK8n7N5+7H2STd\n" +
                "                                                                   u243Ljdk44j/AIdH/tK/9Dx4I/8ABjef/IlAHy5RX1H/AMOj/wBpX/oePBH/";
        inpuData = (EditText) findViewById(R.id.input_data);
        inpuData.setText(data);
        spu.setTestBase64(data);
//        //签名原文(需要)
//        inpuData = (EditText) findViewById(R.id.input_data);
        //签名数据
        ciphertext = (EditText) findViewById(R.id.ciphertext);
        content = (EditText) findViewById(R.id.content);
        sigtwo = (Button) findViewById(R.id.signature_two);
        //签名证书
        cert = (EditText) findViewById(R.id.cert);
        ivPic = (ImageView) findViewById(R.id.iv_select_test);


        //签名
        Button signature = (Button) findViewById(R.id.signature);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String plantext = inpuData.getText().toString();

                SignVerifyP1Activity.this.getIntent().putExtra("data", plantext);
                SignVerifyP1Activity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
                SignatureP1Service signatureP1Service = new SignatureP1Service(SignVerifyP1Activity.this, new ProcessListener<DataProcessResponse>() {
                    @Override
                    public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                        if (dataProcessResponse.getRet() == 0) {
                            Log.e("密钥", "= " + dataProcessResponse.getResult());
                            spu.setCertKey(dataProcessResponse.getResult());
                            ciphertext.setText(dataProcessResponse.getResult());
                            cert.setText(certificate);
                            Log.e("cert", "= " + certificate);
                            spu.setCert(certificate);


                        } else {
                            Toast.makeText(SignVerifyP1Activity.this, "签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void doException(CmSdkException e) {
                        Toast.makeText(SignVerifyP1Activity.this, "签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        //二次签名
        sigtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // base64Two=FileToBase64Util()
            }
        });

        //验签
        Button verify = (Button) findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sign = ciphertext.getText().toString();
                String data = inpuData.getText().toString();
                String c = cert.getText().toString();
                SignVerifyP1Activity.this.getIntent().putExtra("data", data);
                SignVerifyP1Activity.this.getIntent().putExtra("sign", sign);
                SignVerifyP1Activity.this.getIntent().putExtra("cert", c);
                SignVerifyP1Activity.this.getIntent().putExtra("type", DataProcessType.VERIFY_P1.name());

                //调用验证接口
                VerifyService verifyService = new VerifyService(SignVerifyP1Activity.this, DataProcessType.VERIFY_P1, new ProcessListener<DataProcessResponse>() {
                    @Override
                    public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                        if (dataProcessResponse.getRet() == 0) {
                            content.setText("验签成功");
                            Toast.makeText(SignVerifyP1Activity.this, "验签成功", Toast.LENGTH_SHORT).show();
                        } else {
                            content.setText("验签失败");
                            Toast.makeText(SignVerifyP1Activity.this, "验签失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void doException(CmSdkException e) {
                        Toast.makeText(SignVerifyP1Activity.this, "验签失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片操作
                Log.e("---", "onclick photo");
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .start(SignVerifyP1Activity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //System.out.println("********"+photos.get(0)+"********");

            }


            String path = photos.get(0);
            //photoAdapter.notifyDataSetChanged();
            Uri uri = Uri.fromFile(new File(path));

            try {
                String base64 = FileToBase64Util.encodeBase64File(path);

                Log.e("base64=", "--" + base64);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Glide.with(this)
                    .load(uri)
                    .thumbnail(0.1f)
                    .into(ivPic);
        }
    }
}
