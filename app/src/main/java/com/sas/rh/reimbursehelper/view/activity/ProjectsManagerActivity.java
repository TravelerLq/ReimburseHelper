package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.Bean.ProjectDetailInfo;
import com.sas.rh.reimbursehelper.NetUtil.XiangmuUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.zcw.togglebutton.ToggleButton;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectsManagerActivity extends AppCompatActivity {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<ProjectDetailInfo> mData = new ArrayList<ProjectDetailInfo>();
    private ProjectsManagerActivity.RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    private ImageView add_departmentitem,backbt;
    int page = 0;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(ProjectsManagerActivity.this,"提示","提交更改中");
    String project_id;
    private JSONObject projectlist;
    private JSONObject projectalterrs;
    private Handler projectlistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            ptrClassicFrameLayout.refreshComplete();
            ptrClassicFrameLayout.setLoadMoreEnable(false);
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                mData.clear();
                if (projectlist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = projectlist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        ProjectDetailInfo pdi = new ProjectDetailInfo();
                        pdi.setProject_id(jObject.get("xmId").toString());
                        pdi.setProject_name(jObject.get("xmName").toString());
                        pdi.setProject_gsid(jObject.get("gongsiId").toString());
                        pdi.setProject_bumenid(jObject.get("bmId").toString());
                        pdi.setProject_state(jObject.get("isOpen").toString());

                        mData.add(pdi);
                        System.out.println(jObject);
                    }
                }
                mAdapter.notifyDataSetChanged();
                ToastUtil.showToast(ProjectsManagerActivity.this,projectlist.get("HostTime")+":"+projectlist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ProjectsManagerActivity.this,projectlist.get("HostTime")+":"+projectlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(ProjectsManagerActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ProjectsManagerActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_manager);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.project_recycler_view_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.project_recycler_view);
        add_departmentitem = (ImageView) findViewById(R.id.add_projectitem);
        backbt = (ImageView)findViewById(R.id.backbt);

        add_departmentitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ProjectsManagerActivity.this,ProjectsManagerAddItemActivity.class);
                startActivity(it);
            }
        });

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();

    }


    private void init() {
        adapter = new ProjectsManagerActivity.RecyclerAdapter(ProjectsManagerActivity.this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProjectsManagerActivity.this));
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(ProjectsManagerActivity.this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(ProjectsManagerActivity.this, R.drawable.divider_main_bg_height_1));
        mRecyclerView.addItemDecoration(itemDecorationHeader);
        mRecyclerView.setAdapter(mAdapter);
        ptrClassicFrameLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetallProjectInfo();
            }
        });
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        public RecyclerAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            ProjectsManagerActivity.ChildViewHolder holder = (ProjectsManagerActivity.ChildViewHolder) viewHolder;
            holder.itemTv.setText(mData.get(position).getProject_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(ProjectsManagerActivity.this)
                            .setMessage("确定删除该项？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    project_id = mData.get(position).getProject_id();
                                    DeleteProject();
                                }
                            }).show();
                    return false;
                }
            });
            if(mData.get(position).getProject_state().trim().equals("Y")){
                holder.toggleBtn.setToggleOn();
            }else{
                holder.toggleBtn.setToggleOff();
            }
            holder.toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
                @Override
                public void onToggle(boolean on) {
                    if(on == true){
                        System.out.println("=========kai============");
                        project_id = mData.get(position).getProject_id();
                        OpenProject();
                    }else{
                        System.out.println("=========guan============");
                        project_id = mData.get(position).getProject_id();
                        CloseProject();
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item_projectmanage_list, viewHolder,false);
            return new ProjectsManagerActivity.ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;
        public ToggleButton toggleBtn;

        public ChildViewHolder(View view) {
            super(view);
            itemTv = (TextView) view.findViewById(R.id.itemTv);
            toggleBtn = (ToggleButton)view.findViewById(R.id.togglebutton) ;
        }

    }

    private void GetallProjectInfo(){

        new Thread(GetProjectInfoThread).start();
    }

    private void OpenProject(){
        pdu.showpd();
        new Thread(OpenProjectThread).start();
    }

    private void CloseProject(){
        pdu.showpd();
        new Thread(CloseProjectThread).start();
    }

    private void DeleteProject(){
        pdu.showpd();
        new Thread(DeleteProjectThread).start();
    }

    Runnable GetProjectInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XiangmuUtils().queryByGongsiId(1);;
                if(jo != null){
                    projectlist = jo;
                    projectlistback.sendEmptyMessage(1);
                }else{
                    projectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                projectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable OpenProjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XiangmuUtils().setOpen(Integer.parseInt(project_id.trim()));
                if(jo != null){
                    projectlist = jo;
                    projectlistback.sendEmptyMessage(2);
                }else{
                    projectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                projectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable CloseProjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XiangmuUtils().setClose(Integer.parseInt(project_id.trim()));
                if(jo != null){
                    projectlist = jo;
                    projectlistback.sendEmptyMessage(2);
                }else{
                    projectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                projectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable DeleteProjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XiangmuUtils().delete(Integer.parseInt(project_id.trim()));
                if(jo != null){
                    projectlist = jo;
                    projectlistback.sendEmptyMessage(2);
                }else{
                    projectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                projectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

}
