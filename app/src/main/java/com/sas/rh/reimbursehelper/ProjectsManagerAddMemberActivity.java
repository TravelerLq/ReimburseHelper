package com.sas.rh.reimbursehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Adapter.ProjectMembersSelectGridviewAdpter;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntitySerializableMap;

import java.util.ArrayList;
import java.util.List;

public class ProjectsManagerAddMemberActivity extends AppCompatActivity {

    private ProjectMembersSelectGridviewAdpter adpter;
    private GridView gridView;
    private TextView backbt,okbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager_add_member);
        List<PersonnameAndHeadimageEntity> infos = new ArrayList<PersonnameAndHeadimageEntity>();
        infos.add(0,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(1,new PersonnameAndHeadimageEntity("李四",""+R.mipmap.head));
        infos.add(2,new PersonnameAndHeadimageEntity("赵五",""+R.mipmap.head));
        infos.add(3,new PersonnameAndHeadimageEntity("王六",""+R.mipmap.head));
        infos.add(4,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(5,new PersonnameAndHeadimageEntity("钱三",""+R.mipmap.head));
        infos.add(6,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(7,new PersonnameAndHeadimageEntity("李四",""+R.mipmap.head));
        infos.add(8,new PersonnameAndHeadimageEntity("赵五",""+R.mipmap.head));
        infos.add(9,new PersonnameAndHeadimageEntity("王六",""+R.mipmap.head));
        infos.add(10,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(11,new PersonnameAndHeadimageEntity("钱三",""+R.mipmap.head));
        infos.add(12,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(13,new PersonnameAndHeadimageEntity("李四",""+R.mipmap.head));
        infos.add(14,new PersonnameAndHeadimageEntity("赵五",""+R.mipmap.head));
        infos.add(15,new PersonnameAndHeadimageEntity("王六",""+R.mipmap.head));
        infos.add(16,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head));
        infos.add(17,new PersonnameAndHeadimageEntity("钱三",""+R.mipmap.head));
        //int [] image={,,,,,R.drawable.dog2};
        adpter = new ProjectMembersSelectGridviewAdpter(infos, this);
        gridView = (GridView) findViewById(R.id.grid);
        backbt = (TextView)findViewById(R.id.backbt);
        okbt = (TextView)findViewById(R.id.okbt);
        gridView.setAdapter(adpter);
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    System.out.println("aaaaaaaa:");
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
    }
}
