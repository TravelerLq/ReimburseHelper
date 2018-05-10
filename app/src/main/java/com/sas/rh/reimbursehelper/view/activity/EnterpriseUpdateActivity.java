package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.Company;
import com.sas.rh.reimbursehelper.Bean.EnterpriseDetailInfoEntity;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.fragment.EnterpriseFragment;
import com.sas.rh.reimbursehelper.newactivity.NewLoginActivity;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseUpdateActivity extends AppCompatActivity {
    private ImageView backbt;
    private EditText cname, taxnum, khbankname, khbankid, caddr, ctel, ccid, climit;
    private Spinner gsxz_span, zztax_span, sdtax_span, kpfs_span;
    private List<String> gsxz_list = new ArrayList<>();
    private List<String> zztax_list = new ArrayList<>();
    private List<String> sdtax_list = new ArrayList<>();
    private List<String> kpfs_list = new ArrayList<>();

    private ArrayAdapter<String> gsxz_dapter, zztax_dapter, sdtax_dapter, kpfs_dapter;
    private LinearLayout editandsavebt;
    private SharedPreferencesUtil spu;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(EnterpriseUpdateActivity.this, "上传提示", "正在提交中");
    private EnterpriseDetailInfoEntity edie;
    private JSONObject jsonresult;

    private Handler gongsixinxiback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            //
            pdu.dismisspd();
            if (msg.what == 1) {
                if (jsonresult.getIntValue("status") == 200) {
                    Toast.makeText(EnterpriseUpdateActivity.this, "公司信息更新成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EnterpriseUpdateActivity.this, MainActivity.class);
                    intent.putExtra("type","update");
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(EnterpriseUpdateActivity.this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
                }
                // ToastUtil.showToast(EnterpriseDetailActivity.this, jsonresult.get("HostTime") + ":" + jsonresult.get("Note").toString(), Toast.LENGTH_LONG);
            } else if (msg.what == 2) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    Toast.makeText(EnterpriseUpdateActivity.this, "企业用户关联成功", Toast.LENGTH_SHORT).show();
                    Intent intent = null;
                    if (spu.getRoleType().equals("0")) {
                        intent = new Intent(EnterpriseUpdateActivity.this, NewLoginActivity.class);
                    } else {
                        intent = new Intent(EnterpriseUpdateActivity.this, MainActivity.class);
                    }

                    startActivity(intent);
                    finish();

                }

                // ToastUtil.showToast(EnterpriseDetailActivity.this,jsonresult.get("HostTime")+":"+jsonresult.get("Note").toString(), Toast.LENGTH_LONG);
            } else if (msg.what == 0) {
                ToastUtil.showToast(EnterpriseUpdateActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(EnterpriseUpdateActivity.this, "公司信息通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String compnyId;
    private String compName;
    private TextView tvUpdate;
    private int companySelect;
    private int vatSelect;
    private int incomeSelect;
    private int invoiceSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
        backbt = (ImageView) findViewById(R.id.backbt);

        cname = (EditText) findViewById(R.id.cname);
        taxnum = (EditText) findViewById(R.id.taxnum);
        gsxz_span = (Spinner) findViewById(R.id.gsxz_span);
        zztax_span = (Spinner) findViewById(R.id.zztax_span);
        sdtax_span = (Spinner) findViewById(R.id.sdtax_span);
        kpfs_span = (Spinner) findViewById(R.id.kpfs_span);
        khbankname = (EditText) findViewById(R.id.khbankname);
        khbankid = (EditText) findViewById(R.id.khbankid);
        caddr = (EditText) findViewById(R.id.caddr);
        ctel = (EditText) findViewById(R.id.ctel);
        ccid = (EditText) findViewById(R.id.ccid);
        climit = (EditText) findViewById(R.id.climit);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        tvUpdate.setText("确定更新");
        initSpanData();


        editandsavebt = (LinearLayout) findViewById(R.id.editandsavebt);
        Company company = (Company) getIntent().getSerializableExtra("data");
        if (company != null) {
            cname.setText(company.getCompanyName());
            //税号
            taxnum.setText(company.getTaxId());
            //性质
            String companyType = company.getCompanyNature();
            if (companyType.equals(gsxz_list.get(1))) {
                companySelect = 1;
            } else {
                companySelect = 2;
            }
            //增值税方式
            String vatMethod = company.getVatCollectionMethods();
            if (vatMethod.equals(zztax_list.get(1))) {
                vatSelect = 1;

            } else {
                vatSelect = 2;
            }

            //所得税增收方式
            String incomeTax = company.getIncomeTaxCollectionMethods();
            //(x==y)?'Y':'N'
//            incomeTax.equals("")
            if (incomeTax.equals(sdtax_list.get(1))) {
                incomeSelect = 1;
            } else {
                incomeSelect = 2;
            }

            //开票方式
            String invoiceMethod = company.getInvoiceMethod();
            if (invoiceMethod.equals(kpfs_list.get(1))) {
                invoiceSelect = 1;
            } else {
                invoiceSelect = 2;
            }
            //开户银行
            khbankname.setText(company.getOpeningBank());
            //银行账号
            khbankid.setText(company.getBankAccount());
            // 公司地址
            caddr.setText(company.getAddress());
            //公司电话
            ctel.setText(company.getTelephone());
            //创始人证件号码
            ccid.setText(company.getLegalIdNumber());

            //报销金额
            climit.setText(company.getCompanyQuota());

        }

        initGsxz_span();
        initZztax_span();
        initSdtax_span();
        initKpfs_span();

        spu = new SharedPreferencesUtil(EnterpriseUpdateActivity.this);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //点击"更新"
        editandsavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initTestData();
                CreateCompanyInfo();
            }
        });

    }

    private void initSpanData() {
        gsxz_list.add("--请选择--");
        gsxz_list.add("有限责任公司");
        gsxz_list.add("个人商户");

        zztax_list.add("--请选择--");
        zztax_list.add("小规模纳税人");
        zztax_list.add("一般纳税人");

        sdtax_list.add("--请选择--");
        sdtax_list.add("查账征收");
        sdtax_list.add("核定征收");

        kpfs_list.add("--请选择--");
        kpfs_list.add("增值税普通发票");
        kpfs_list.add("增值税专用发票");
        kpfs_list.add("增值税电子发票");
    }

    private void initTestData() {
        cname.setText("御安神科技");
        taxnum.setText("234958710cidle");
        khbankname.setText("中国建设银行");
        khbankid.setText("32039483956");
        caddr.setText("江苏南京玄武区");
        ctel.setText("051203945");
        ccid.setText("32047859098723");
        climit.setText("334");
    }

    private void initGsxz_span() {

//        gsxz_list.add("--请选择--");
//        gsxz_list.add("有限责任公司");
//        gsxz_list.add("个人商户");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        gsxz_dapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gsxz_list);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        gsxz_dapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        gsxz_span.setAdapter(gsxz_dapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        gsxz_span.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
        gsxz_span.setSelection(companySelect);

    }

    private void initZztax_span() {
        // zztax_list = new ArrayList<String>();
//        zztax_list.add("--请选择--");
//        zztax_list.add("小规模纳税人");
//        zztax_list.add("一般纳税人");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        zztax_dapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zztax_list);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        zztax_dapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        zztax_span.setAdapter(zztax_dapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        zztax_span.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
        zztax_span.setSelection(vatSelect);
    }

    private void initSdtax_span() {
        //  sdtax_list = new ArrayList<String>();
//        sdtax_list.add("--请选择--");
//        sdtax_list.add("查账征收");
//        sdtax_list.add("核定征收");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        sdtax_dapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sdtax_list);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        sdtax_dapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        sdtax_span.setAdapter(sdtax_dapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        sdtax_span.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
        sdtax_span.setSelection(incomeSelect);
    }

    private void initKpfs_span() {
        //  kpfs_list = new ArrayList<String>();
//        kpfs_list.add("--请选择--");
//        kpfs_list.add("增值税普通发票");
//        kpfs_list.add("增值税专用发票");
//        kpfs_list.add("增值税电子发票");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        kpfs_dapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kpfs_list);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        kpfs_dapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        kpfs_span.setAdapter(kpfs_dapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        kpfs_span.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
        kpfs_span.setSelection(invoiceSelect);
    }

    private void CreateCompanyInfo() {
        compName = cname.getText().toString().trim();

        if (cname.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写公司名称", Toast.LENGTH_LONG);
            return;
        } else if (taxnum.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写税号", Toast.LENGTH_LONG);
            return;
        } else if (gsxz_span.getSelectedItem().toString().trim().equals("--请选择--")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请选择公司性质", Toast.LENGTH_LONG);
            return;
        } else if (zztax_span.getSelectedItem().toString().trim().equals("--请选择--")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请选择增值税征收方式", Toast.LENGTH_LONG);
            return;
        } else if (sdtax_span.getSelectedItem().toString().trim().equals("--请选择--")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请选择所得税增收方式", Toast.LENGTH_LONG);
            return;
        } else if (kpfs_span.getSelectedItem().toString().trim().equals("--请选择--")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请选择开票方式", Toast.LENGTH_LONG);
            return;
        } else if (khbankname.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写开户银行名称", Toast.LENGTH_LONG);
            return;
        } else if (khbankid.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写开户银行账号", Toast.LENGTH_LONG);
            return;
        } else if (caddr.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写公司地址", Toast.LENGTH_LONG);
            return;
        } else if (ctel.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写公司电话", Toast.LENGTH_LONG);
            return;
        } else if (ccid.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写证件号码", Toast.LENGTH_LONG);
            return;
        } else if (climit.getText().toString().trim().equals("")) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "请填写报销限额", Toast.LENGTH_LONG);
            return;
        } else if (spu.getUidNum() == -1) {
            ToastUtil.showToast(EnterpriseUpdateActivity.this, "userId不可以为空！", Toast.LENGTH_LONG);
            return;
        }

        edie = new EnterpriseDetailInfoEntity();
        edie.setCname(cname.getText().toString().trim());
        edie.setTaxnum(taxnum.getText().toString().trim());
        edie.setGsxz(gsxz_span.getSelectedItem().toString().trim());
        edie.setZztax(zztax_span.getSelectedItem().toString().trim());
        edie.setSdtax(sdtax_span.getSelectedItem().toString().trim());
        edie.setKpfs(kpfs_span.getSelectedItem().toString().trim());
        edie.setKhbankname(khbankname.getText().toString().trim());
        edie.setKhbankid(khbankid.getText().toString().trim());
        edie.setCaddr(caddr.getText().toString().trim());
        edie.setCtel(ctel.getText().toString().trim());
        edie.setCcid(ccid.getText().toString().trim());
        edie.setClimit(climit.getText().toString().trim());

        System.out.println("aaaaaaaa:" + new SharedPreferencesUtil(EnterpriseUpdateActivity.this).getCid());


//        new GongsiUtils().insert("测试公司","上市","增值税征收方式","所得税征收方式","公司税号",
//				"开户银行","银行账号","公司地址","公司电话","开票方式",2);
        //cid存在 －就去create （注册）
//        if (new SharedPreferencesUtil(EnterpriseDetailActivity.this).isCidEmpty()) {
//            pdu.showpd();
//            new Thread(sendCreateCompanyInfoThread).start();
//        } else {
//            pdu.showpd();
//            new Thread(sendUpdateCompanyInfoThread).start();
//        }
        pdu.showpd();
        new Thread(sendCreateCompanyInfoThread).start();

    }

    private void relateWithUser() {

        new Thread(relateWithuserThread).start();
    }

    Runnable sendCreateCompanyInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = CompanyUtil.companyUpdate(
                        edie.getCname(), edie.getGsxz(), edie.getZztax(),
                        edie.getSdtax(), edie.getTaxnum(), edie.getKhbankname(),
                        edie.getKhbankid(), edie.getCaddr(), edie.getCtel(),
                        edie.getKpfs(), "", edie.getCcid(), (Integer) spu.getUidNum()
                        ,Double.parseDouble(edie.getClimit())

                );


//                new GongsiUtils().insert(
//                        edie.getCname(),
//                        edie.getGsxz(),
//                        edie.getZztax(),
//                        edie.getSdtax(),
//                        edie.getTaxnum(),
//                        edie.getKhbankname(),
//                        edie.getKhbankid(),
//                        edie.getCaddr(),
//                        edie.getCtel(),
//                        edie.getKpfs(),
//                        edie.getCcid(),
//                        Double.parseDouble(edie.getClimit()),
//                        2);
                if (jo != null) {
                    jsonresult = jo;
                    gongsixinxiback.sendEmptyMessage(1);
                } else {
                    gongsixinxiback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                gongsixinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    //relate with usetId
    Runnable relateWithuserThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = CompanyUtil.updateUser(spu.getUidNum(), spu.getCidNum());


                if (jo != null) {
                    jsonresult = jo;
                    gongsixinxiback.sendEmptyMessage(2);
                } else {
                    gongsixinxiback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                gongsixinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable sendUpdateCompanyInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = CompanyUtil.updateCompany((Integer) spu.getCidNum(), edie.getCname(), (Integer) spu.getUidNum());

                if (jo != null) {
                    jsonresult = jo;
                    gongsixinxiback.sendEmptyMessage(2);
                } else {
                    gongsixinxiback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                gongsixinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

}