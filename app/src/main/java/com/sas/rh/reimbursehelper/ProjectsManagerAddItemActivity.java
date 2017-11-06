package com.sas.rh.reimbursehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.Entity.PersonnameAndHeadimageEntitySerializableMap;
import com.sas.rh.reimbursehelper.View.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectsManagerAddItemActivity extends AppCompatActivity {

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private ImageView backbt,addPM,addmember;
    private CircleImageView project_master_head;
    private TextView project_master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager_add_item);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        addPM = (ImageView)findViewById(R.id.addPM) ;
        addmember = (ImageView)findViewById(R.id.addmember) ;
        gview = (GridView) findViewById(R.id.gview);
        project_master = (TextView)findViewById(R.id.project_master) ;
        project_master_head = (CircleImageView)findViewById(R.id.project_master_head) ;

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
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (String key : selectedmembers.keySet()) {
            System.out.println("keykeykeykeykeykeykey  ============= "+ key+":"+selectedmembers.get(key).getImagepath()+":" +selectedmembers.get(key).getPname() );
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", Long.parseLong(selectedmembers.get(key).getImagepath()));
            map.put("text", selectedmembers.get(key).getPname());
            data_list.add(map);
        }

        return data_list;
    }
}
