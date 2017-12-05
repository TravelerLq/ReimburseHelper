package com.sas.rh.reimbursehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveProcedureManageConfigAddApproverActivity extends AppCompatActivity {


    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private List<PersonnameAndHeadimageEntity> peopleinfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_procedure_manage_config_add_approver);
        peopleinfos = getDataFromServer();
        ApproveProcedureManageConfigAddApproverActivity.this.setFinishOnTouchOutside(false);
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
                mIntent.putExtra("position", getIntent().getIntExtra("position",-1));
                mIntent.putExtra("approver_name", ""+ peopleinfos.get(position).getPname());
                mIntent.putExtra("approver_id", ""+peopleinfos.get(position).getStaffID());
                // 设置结果，并进行传送
                ApproveProcedureManageConfigAddApproverActivity.this.setResult(1, mIntent);
                //System.out.println("aaaaaaaaaa:"+data_list.get(position).get("image")+":"+data_list.get(position).get("text"));
                finish();
            }
        });
    }

    public List<PersonnameAndHeadimageEntity> getDataFromServer(){
        List<PersonnameAndHeadimageEntity> peopleinfo = new ArrayList<PersonnameAndHeadimageEntity>();
        peopleinfo.add(new PersonnameAndHeadimageEntity("小张",""+R.mipmap.head,"1120170711"));
        peopleinfo.add(new PersonnameAndHeadimageEntity("小王",""+R.mipmap.head,"1120170712"));
        peopleinfo.add(new PersonnameAndHeadimageEntity("小朱",""+R.mipmap.head,"1120170713"));
        peopleinfo.add(new PersonnameAndHeadimageEntity("小李",""+R.mipmap.head,"1120170714"));
        peopleinfo.add(new PersonnameAndHeadimageEntity("小田",""+R.mipmap.head,"1120170715"));
        peopleinfo.add(new PersonnameAndHeadimageEntity("小信",""+R.mipmap.head,"1120170716"));
        return peopleinfo;
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<peopleinfos.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Integer.parseInt(peopleinfos.get(i).getImagepath()));
            map.put("text", "姓名:"+peopleinfos.get(i).getPname()+"\n工号:"+peopleinfos.get(i).getStaffID());
            map.put("staffid", peopleinfos.get(i).getStaffID());
            data_list.add(map);
        }

        return data_list;
    }
}
