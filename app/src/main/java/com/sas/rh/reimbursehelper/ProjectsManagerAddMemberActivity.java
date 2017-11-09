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
        infos.add(0,new PersonnameAndHeadimageEntity("张三",""+R.mipmap.head,"111111"));
        infos.add(1,new PersonnameAndHeadimageEntity("李四",""+R.mipmap.head,"222222"));
        infos.add(2,new PersonnameAndHeadimageEntity("赵五",""+R.mipmap.head,"333333"));
        infos.add(3,new PersonnameAndHeadimageEntity("王六",""+R.mipmap.head,"444444"));
        infos.add(4,new PersonnameAndHeadimageEntity("张李",""+R.mipmap.head,"555555"));
        infos.add(5,new PersonnameAndHeadimageEntity("李四",""+R.mipmap.head,"666666"));
        infos.add(6,new PersonnameAndHeadimageEntity("赵五",""+R.mipmap.head,"777777"));
        infos.add(7,new PersonnameAndHeadimageEntity("王六",""+R.mipmap.head,"888888"));
        infos.add(8,new PersonnameAndHeadimageEntity("张五",""+R.mipmap.head,"999999"));
        infos.add(9,new PersonnameAndHeadimageEntity("钱五",""+R.mipmap.head,"000000"));
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
    }
}
