package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.sas.rh.reimbursehelper.Bean.SubjectInfoEntity;
import com.sas.rh.reimbursehelper.NetUtil.XfkmUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.zcw.togglebutton.ToggleButton;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsManagerActivity extends AppCompatActivity {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<SubjectInfoEntity> mData = new ArrayList<SubjectInfoEntity>();
    private SubjectsManagerActivity.RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    private ImageView add_subjectitem,backbt;
    int page = 0;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(SubjectsManagerActivity.this,"提示","提交更改中");
    String subject_id;
    private JSONObject subjectlist;
    private JSONObject subjectalterrs;
    private Handler subjectlistback = new Handler(){
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
                if (subjectlist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = subjectlist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        SubjectInfoEntity sie = new SubjectInfoEntity();
                        sie.setSubject_gsid(jObject.get("gongsiId").toString());
                        sie.setSubject_id(jObject.get("xfkmId").toString());
                        sie.setSubject_name(jObject.get("xfkmName").toString());
                        if(jObject.get("isOpen")!=null){
                            sie.setSubject_state(jObject.get("isOpen").toString());
                        }

                        mData.add(sie);
                        System.out.println(jObject);
                    }
                }
                mAdapter.notifyDataSetChanged();
                ToastUtil.showToast(SubjectsManagerActivity.this,subjectlist.get("HostTime")+":"+subjectlist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(SubjectsManagerActivity.this,subjectlist.get("HostTime")+":"+subjectlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(SubjectsManagerActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(SubjectsManagerActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_manage);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_recycler_view_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
//        add_subjectitem = (ImageView)findViewById(R.id.add_subjectitem);
        backbt = (ImageView)findViewById(R.id.backbt);
//        add_subjectitem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(SubjectsManagerActivity.this,SubjectsManagerAddItemActivity.class);
//                startActivity(it);
//            }
//        });
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化Recyclerview
        init();
    }

    private void init() {
        adapter = new SubjectsManagerActivity.RecyclerAdapter(SubjectsManagerActivity.this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SubjectsManagerActivity.this));
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(SubjectsManagerActivity.this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(SubjectsManagerActivity.this, R.drawable.divider_main_bg_height_1));
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
                GetallSubjectInfo();
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
            SubjectsManagerActivity.ChildViewHolder holder = (SubjectsManagerActivity.ChildViewHolder) viewHolder;
            holder.itemTv.setText(mData.get(position).getSubject_name());
            holder.project_icon_text.setText(mData.get(position).getSubject_name().subSequence(0, 1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(SubjectsManagerActivity.this)
                            .setMessage("确定删除该项？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    subject_id = mData.get(position).getSubject_id();
                                    DeleteSubject();
                                }
                            }).show();
                    return false;
                }
            });
            if(mData.get(position).getSubject_state().trim().equals("Y")){
                holder.toggleBtn.setToggleOn();
            }else{
                holder.toggleBtn.setToggleOff();
            }
            holder.toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
                @Override
                public void onToggle(boolean on) {
                    if(on == true){
                        System.out.println("=========kai============");
                        subject_id = mData.get(position).getSubject_id();
                        OpenSubject();
                    }else{
                        System.out.println("=========guan============");
                        subject_id = mData.get(position).getSubject_id();
                        CloseSubject();
                    }
                }
            });

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item_subjectsmanage_list, viewHolder,false);
            return new SubjectsManagerActivity.ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv,project_icon_text;
        public ToggleButton toggleBtn;

        public ChildViewHolder(View view) {
            super(view);
            itemTv = (TextView) view.findViewById(R.id.itemtv);
            project_icon_text = (TextView) view.findViewById(R.id.project_icon_text);
            toggleBtn = (ToggleButton)view.findViewById(R.id.togglebutton) ;
        }

    }

    private void GetallSubjectInfo(){
        new Thread(GetSubjectInfoThread).start();
    }

    private void OpenSubject(){
        pdu.showpd();
        new Thread(OpenSubjectThread).start();
    }

    private void CloseSubject(){
        pdu.showpd();
        new Thread(CloseSubjectThread).start();
    }

    private void DeleteSubject(){
        pdu.showpd();
        new Thread(DeleteSubjectThread).start();
    }

    Runnable GetSubjectInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XfkmUtils().getAllXfkmId(1);
                if(jo != null){
                    subjectlist = jo;
                    subjectlistback.sendEmptyMessage(1);
                }else{
                    subjectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                subjectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable OpenSubjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XfkmUtils().addXfkmId(1,1,Integer.parseInt(subject_id.trim()));
                if(jo != null){
                    subjectlist = jo;
                    subjectlistback.sendEmptyMessage(2);
                }else{
                    subjectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                subjectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable CloseSubjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XfkmUtils().removeXfkmId(1,1,Integer.parseInt(subject_id.trim()));
                if(jo != null){
                    subjectlist = jo;
                    subjectlistback.sendEmptyMessage(2);
                }else{
                    subjectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                subjectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable DeleteSubjectThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XfkmUtils().delete(1,1,Integer.parseInt(subject_id.trim()));
                if(jo != null){
                    subjectlist = jo;
                    subjectlistback.sendEmptyMessage(2);
                }else{
                    subjectlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                subjectlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
