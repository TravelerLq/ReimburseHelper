package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.NetUtil.YuangongUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsManagerAddMasterActivity extends AppCompatActivity {

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private List<PersonnameAndHeadimageEntity> peopleinfos = new ArrayList<PersonnameAndHeadimageEntity>();

    private ProgressDialogUtil pdu =new ProgressDialogUtil(ProjectsManagerAddMasterActivity.this,"提示","正在获取人员信息");
    String department_id;
    private JSONObject stufflist;
    private JSONObject deapetmentalterrs;
    private Handler stufflistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                peopleinfos.clear();
                if (stufflist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = stufflist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        PersonnameAndHeadimageEntity pahe = new PersonnameAndHeadimageEntity();
                        pahe.setPname(jObject.get("ygName").toString());
                        pahe.setImagepath(""+ R.mipmap.head);
                        pahe.setStaffID(jObject.get("ygId").toString());
                        peopleinfos.add(pahe);
                        System.out.println(jObject);
                    }
                }
                getData();
                sim_adapter.notifyDataSetChanged();
                ToastUtil.showToast(ProjectsManagerAddMasterActivity.this,stufflist.get("HostTime")+":"+stufflist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ProjectsManagerAddMasterActivity.this,stufflist.get("HostTime")+":"+stufflist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(ProjectsManagerAddMasterActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ProjectsManagerAddMasterActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager_add_master);
//        peopleinfos = getDataFromServer();
        //ProjectsManagerAddMasterActivity.this.setFinishOnTouchOutside(false);
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_select_department_master, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent();
                mIntent.putExtra("slectedpath", ""+data_list.get(position).get("image"));
                mIntent.putExtra("slectedname", ""+data_list.get(position).get("text"));
                mIntent.putExtra("slectedstaffid", ""+data_list.get(position).get("staffid"));
                // 设置结果，并进行传送
                ProjectsManagerAddMasterActivity.this.setResult(1, mIntent);
                System.out.println("aaaaaaaaaa:"+data_list.get(position).get("image")+":"+data_list.get(position).get("text"));
                finish();
            }
        });
        GetAllStaff();
    }

//    public List<PersonnameAndHeadimageEntity> getDataFromServer(){
//        List<PersonnameAndHeadimageEntity> peopleinfo = new ArrayList<PersonnameAndHeadimageEntity>();
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小张",""+R.mipmap.head,"111111"));
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小王",""+R.mipmap.head,"222222"));
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小朱",""+R.mipmap.head,"333333"));
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小李",""+R.mipmap.head,"444444"));
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小田",""+R.mipmap.head,"555555"));
//        peopleinfo.add(new PersonnameAndHeadimageEntity("小信",""+R.mipmap.head,"666666"));
//        return peopleinfo;
//    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<peopleinfos.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Integer.parseInt(peopleinfos.get(i).getImagepath()));
            map.put("text", peopleinfos.get(i).getPname());
            map.put("staffid", peopleinfos.get(i).getStaffID());
            data_list.add(map);
        }

        return data_list;
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
                    stufflist = jo;
                    stufflistback.sendEmptyMessage(1);
                }else{
                    stufflistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                stufflistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
