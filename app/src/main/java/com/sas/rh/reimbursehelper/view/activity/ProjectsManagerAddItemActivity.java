package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntitySerializableMap;
import com.sas.rh.reimbursehelper.NetUtil.BumenUtils;
import com.sas.rh.reimbursehelper.NetUtil.XiangmuUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.widget.CircleImageView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsManagerAddItemActivity extends AppCompatActivity {

    private EditText pname,pnum;
    private LinearLayout savebt;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private ImageView backbt,addPM,addmember;
    private CircleImageView project_master_head;
    private TextView project_master;
    private Spinner dp_sp;
    private ArrayAdapter<String> dp_dapter;
    private List<String> dpsid = new ArrayList<String>();
    private List<String> dpsname = new ArrayList<String>();

    private ProgressDialogUtil pdu =new ProgressDialogUtil(ProjectsManagerAddItemActivity.this,"提示","正在获取人员信息");
    private ProgressDialogUtil pdu2 =new ProgressDialogUtil(ProjectsManagerAddItemActivity.this,"提示","正在提交");
    private String dp_id;
    private String project_name;
    private String project_num;
    private String master_id;
    private String members_id;
    private JSONObject deapetmentlist;
    private Handler deapetmentlistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(pdu2.getMypDialog() != null){
                pdu2.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                dpsid.clear();
                dpsname.clear();
                if (deapetmentlist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = deapetmentlist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        String name = jObject.get("bmName").toString();
                        String id = jObject.get("bmId").toString();
                        dpsname.add(name);
                        dpsid.add(id);
                    }
                }
                if(dpsid.size() > 0){
                    dp_id = dpsid.get(0);
                }
                initDp_sp(dpsname);
                ToastUtil.showToast(ProjectsManagerAddItemActivity.this,deapetmentlist.get("HostTime")+":"+deapetmentlist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ProjectsManagerAddItemActivity.this,deapetmentlist.get("HostTime")+":"+deapetmentlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
                if(deapetmentlist.get("ResultCode").toString().trim().equals("00")){
                    finish();
                }
            }else if(msg.what == 0){
                ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager_add_item);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        savebt = (LinearLayout) findViewById(R.id.savebt) ;
        pname = (EditText) findViewById(R.id.pname);
        pnum = (EditText) findViewById(R.id.pnum);
        addPM = (ImageView)findViewById(R.id.addPM) ;
        addmember = (ImageView)findViewById(R.id.addmember) ;
        gview = (GridView) findViewById(R.id.gview);
        project_master = (TextView)findViewById(R.id.project_master) ;
        project_master_head = (CircleImageView)findViewById(R.id.project_master_head) ;
        dp_sp = (Spinner)findViewById(R.id.dp_sp) ;

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent(ProjectsManagerAddItemActivity.this,ProjectsManagerAddMasterActivity.class);
                startActivityForResult(it,1);
            }
        });

        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent(ProjectsManagerAddItemActivity.this,ProjectsManagerAddMemberActivity.class);
                startActivityForResult(it,2);
            }
        });

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProjectInfo();
            }
        });

        GetallDepartmentInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       System.out.println("返回的请求码:"+requestCode);
       if(data == null){
            System.out.println("传入数据为空，aaaaaaaaaaa");
       }else{
            switch (requestCode){
                //选择的主管
                case 1:
                    super.onActivityResult(requestCode, resultCode, data);
                    String sn = data.getStringExtra("slectedname");
                    String sp = data.getStringExtra("slectedpath");
                    String sid = data.getStringExtra("slectedstaffid");
                    System.out.println("aaaaaaaaaa:"+requestCode+":"+sn+":"+sp+":"+sid);
                    project_master_head.setImageResource(Integer.parseInt(sp));
                    project_master.setText(sn+"("+sid+")");
                    master_id = sid;
                    break;
                //选择的成员
                case 2:
                    data.getExtras().get("map");
                    PersonnameAndHeadimageEntitySerializableMap selectedmenmbers = (PersonnameAndHeadimageEntitySerializableMap) data.getSerializableExtra("map");
                    String [] from ={"image","text"};
                    int [] to = {R.id.image_item,R.id.username_item};
                    data_list = new ArrayList<Map<String, Object>>();
                    //获取数据
                    getData(selectedmenmbers.getMap());
                    sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_select_project_member, from, to);
                    //配置适配器
                    gview.setAdapter(sim_adapter);
                    break;
            }
       }
    }

    public List<Map<String, Object>> getData(Map<String, PersonnameAndHeadimageEntity> selectedmembers){
        members_id = "";
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (String key : selectedmembers.keySet()) {
            System.out.println("keykeykeykeykeykeykey  ============= "+ key+":"+selectedmembers.get(key).getImagepath()+":" +selectedmembers.get(key).getPname() );
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Long.parseLong(selectedmembers.get(key).getImagepath()));
            map.put("text", selectedmembers.get(key).getPname());
            members_id +=selectedmembers.get(key).getStaffID()+"#";
            data_list.add(map);
        }

        return data_list;
    }

    private void initDp_sp(List<String> dplist){
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        dp_dapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dplist);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        dp_dapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        dp_sp.setAdapter(dp_dapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        dp_sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                dp_id = dpsid.get(arg2);
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
    }

    private void GetallDepartmentInfo(){
        pdu.showpd();
        new Thread(GetDepartmentInfoThread).start();
    }

    private void sendProjectInfo(){
        if(dpsname.size() <= 0){
            ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"当前无部门可选，请重新加载或新增部门", Toast.LENGTH_LONG);
            return;
        }
        if(pname.getText().toString().trim().equals("")){
            ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"请填写项目名称", Toast.LENGTH_LONG);
            return;
        }else if(pnum.getText().toString().trim().equals("")){
            ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"请填写报销限额", Toast.LENGTH_LONG);
            return;
        }
        if(master_id.trim().equals("")){
            ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"请选择部门主管", Toast.LENGTH_LONG);
            return;
        }
        if(members_id.trim().equals("")){
            ToastUtil.showToast(ProjectsManagerAddItemActivity.this,"请选择部门成员", Toast.LENGTH_LONG);
            return;
        }

        project_name = pname.getText().toString().trim();
        project_num = pnum.getText().toString().trim();

        pdu2.showpd();
        new Thread(sendProjectInfoThread).start();
    }


    Runnable GetDepartmentInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new BumenUtils().queryByGongsiId(1);
                if(jo != null){
                    deapetmentlist = jo;
                    deapetmentlistback.sendEmptyMessage(1);
                }else{
                    deapetmentlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                deapetmentlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    Runnable sendProjectInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XiangmuUtils().insert(1,Integer.parseInt(dp_id.trim()),1,2,Double.parseDouble(project_num.trim()),project_name,Integer.parseInt(master_id.trim()),members_id);;
                if(jo != null){
                    deapetmentlist = jo;
                    deapetmentlistback.sendEmptyMessage(2);
                }else{
                    deapetmentlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                deapetmentlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
