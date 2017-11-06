package com.sas.rh.reimbursehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SubjectsManagerActivity extends AppCompatActivity {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<String>();
    private SubjectsManagerActivity.RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    private ImageView add_subjectitem,backbt;

    int page = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_manage);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_recycler_view_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        add_subjectitem = (ImageView)findViewById(R.id.add_subjectitem);
        backbt = (ImageView)findViewById(R.id.backbt);
        add_subjectitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SubjectsManagerActivity.this,SubjectsManagerAddItemActivity.class);
                startActivity(it);
            }
        });
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
        adapter = new SubjectsManagerActivity.RecyclerAdapter(SubjectsManagerActivity.this, mData);
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        mData.clear();
                        for (int i = 0; i < 17; i++) {
                            mData.add(new String("  RecyclerView item  -" + i));
                        }
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        ptrClassicFrameLayout.setLoadMoreEnable(false);
                    }
                }, 1500);
            }
        });
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> datas;
        private LayoutInflater inflater;

        public RecyclerAdapter(Context context, List<String> data) {
            super();
            inflater = LayoutInflater.from(context);
            datas = data;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            SubjectsManagerActivity.ChildViewHolder holder = (SubjectsManagerActivity.ChildViewHolder) viewHolder;
            //holder.itemTv.setText(datas.get(position));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item_subjectsmanage_list, viewHolder,false);
            return new SubjectsManagerActivity.ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;

        public ChildViewHolder(View view) {
            super(view);
            //itemTv = (TextView) view;
        }

    }
}
