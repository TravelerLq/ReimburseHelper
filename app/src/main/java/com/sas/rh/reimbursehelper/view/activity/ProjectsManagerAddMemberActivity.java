package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.Adapter.ProjectMembersSelectGridviewAdpter;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntitySerializableMap;
import com.sas.rh.reimbursehelper.NetUtil.YuangongUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectsManagerAddMemberActivity extends AppCompatActivity {

    private ProjectMembersSelectGridviewAdpter adpter;
    private List<PersonnameAndHeadimageEntity> infos = new ArrayList<PersonnameAndHeadimageEntity>();
    private GridView gridView;
    private TextView backbt,okbt;

    private ProgressDialogUtil pdu =new ProgressDialogUtil(ProjectsManagerAddMemberActivity.this,"提示","正在获取人员信息");
    String department_id;
    private JSONObject stafflist;
    private JSONObject deapetmentalterrs;
    private Handler stafflistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                infos.clear();
                if (stafflist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = stafflist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        PersonnameAndHeadimageEntity pahe = new PersonnameAndHeadimageEntity();
                        pahe.setPname(jObject.get("ygName").toString());
                        pahe.setImagepath(""+ R.mipmap.head);
                        pahe.setStaffID(jObject.get("ygId").toString());
                        infos.add(pahe);
                        System.out.println(jObject);
                    }
                }
                adpter = new ProjectMembersSelectGridviewAdpter(infos, ProjectsManagerAddMemberActivity.this);
                gridView.setAdapter(adpter);
                okbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent mIntent = new Intent();
                            final PersonnameAndHeadimageEntitySerializableMap myMap=new PersonnameAndHeadimageEntitySerializableMap();
                            myMap.setMap(adpter.getSelectedMembers());
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("map", myMap);
                            mIntent.putExtras(bundle);
                            // 设置结果，并进行传送
                            ProjectsManagerAddMemberActivity.this.setResult(1, mIntent);
                            finish();

                        }catch (Exception e){
                            System.out.println("项目管理增加成员异常:");
                            e.printStackTrace();
                        }

                    }
                });
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        adpter.chiceState(position);

                        //System.out.println("aaaaaaaaaaaa::"+ adpter.getSelectedMembers().size());
                    }
                });
                ToastUtil.showToast(ProjectsManagerAddMemberActivity.this,stafflist.get("HostTime")+":"+stafflist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ProjectsManagerAddMemberActivity.this,stafflist.get("HostTime")+":"+stafflist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(ProjectsManagerAddMemberActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ProjectsManagerAddMemberActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager_add_member);
        //int [] image={,,,,,R.drawable.dog2};
        //infos.add(new PersonnameAndHeadimageEntity("无",""+R.mipmap.head,""));

        gridView = (GridView) findViewById(R.id.grid);
        backbt = (TextView)findViewById(R.id.backbt);
        okbt = (TextView)findViewById(R.id.okbt);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetAllStaff();
    }

    private void GetAllStaff(){
        pdu.showpd();
        new Thread(GetAllStaffThread).start();
    }

    Runnable GetAllStaffThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new YuangongUtils().getYgByGongsiId(1);
                if(jo != null){
                    stafflist = jo;
                    stafflistback.sendEmptyMessage(1);
                }else{
                    stafflistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                stafflistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
