package com.sas.rh.reimbursehelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.Entity.BaoxiaoContentEntity;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.MineRadioAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomepageFragment extends Fragment implements View.OnClickListener, MineRadioAdapter.OnItemClickListener{

    private PopupMenu popupMenu;
    private Menu menu;
    private ImageButton menubt;
    private ImageView personaldetail_btn;
    private RelativeLayout addbxnrbt;
    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;

    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.tv_select_num)
    TextView mTvSelectNum;
    @InjectView(R.id.btn_delete)
    Button mBtnDelete;
    @InjectView(R.id.select_all)
    TextView mSelectAll;
    @InjectView(R.id.ll_mycollection_bottom_dialog)
    LinearLayout mLlMycollectionBottomDialog;
    @InjectView(R.id.btn_editor)
    TextView mBtnEditor;
    private MineRadioAdapter mRadioAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private List<BaoxiaoContentEntity> mList ;
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
    private TextView mybxbt,myspbt,myfybt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.inject(this,view);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view){
        menubt = (ImageButton) view.findViewById(R.id.popupmenu_btn);
        addbxnrbt = (RelativeLayout)view.findViewById(R.id.addbxnrbt);
        personaldetail_btn = (ImageView)view.findViewById(R.id.personaldetail_btn);
        mybxbt = (TextView)view.findViewById(R.id.mybxbt);
        myspbt = (TextView)view.findViewById(R.id.myspbt);
        myfybt = (TextView)view.findViewById(R.id.myfybt);
        popupMenu = new PopupMenu(getActivity(), view.findViewById(R.id.popupmenu_btn));
        menu = popupMenu.getMenu();

        // 通过代码添加菜单项
        menu.add(Menu.NONE, Menu.FIRST + 0, 0, "复制");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "粘贴");

        // 通过XML文件添加菜单项
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.popupmenu, menu);

        // 监听事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.news:
                        break;
                    case R.id.open:
                        break;
                    case Menu.FIRST + 0:
                        break;
                    case Menu.FIRST + 1:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        menubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        addbxnrbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(),AddBaoxiaojizhuActivity.class);
                startActivityForResult(it,1);
            }
        });

        personaldetail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(),PersonalDetailActivity.class);
                startActivity(it);
            }
        });

        mybxbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),MyReimburseActivity.class);
                startActivity(it);
            }
        });

        myspbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),MyApprovalActivity.class);
                startActivity(it);
            }
        });

        myfybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),MyFeeActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null){
            //int position = data.getExtras().getInt("position");

        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mRadioAdapter = new MineRadioAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_main_bg_height_1));
        mRecyclerview.addItemDecoration(itemDecorationHeader);
        mRecyclerview.setAdapter(mRadioAdapter);
        for (int i = 0; i < 30; i++) {
            BaoxiaoContentEntity myLiveList = new BaoxiaoContentEntity();
            myLiveList.setBxtype("第" + i + "个");
            myLiveList.setBxdate("来源" + i);
            myLiveList.setBxnum(""+i);
            mList.add(myLiveList);
            mRadioAdapter.notifyAdapter(mList, false);
        }
    }

    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
            mBtnDelete.setTextColor(Color.WHITE);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnDelete.setEnabled(false);
            mBtnDelete.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_b7b8bd));
        }
    }

    private void initListener() {
        mRadioAdapter.setOnItemClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mBtnEditor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                deleteVideo();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.btn_editor:
                if(editorStatus == true && index >= 1){
                    new AlertDialog.Builder(getActivity())
                            .setMessage("确定报销选定项？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updataEditMode();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updataEditMode();
                                    Intent it = new Intent(getActivity(),ReimburseBillCreateActivity.class);
                                    startActivity(it);
                                }
                            }).show();

                }else{
                    updataEditMode();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mRadioAdapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(true);
            }
            index = mRadioAdapter.getMyLiveList().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        mRadioAdapter.notifyDataSetChanged();
        setBtnBackground(index);
        mTvSelectNum.setText(String.valueOf(index));
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0){
            mBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg = (TextView) builder.findViewById(R.id.tv_msg);
        Button cancle = (Button) builder.findViewById(R.id.btn_cancle);
        Button sure = (Button) builder.findViewById(R.id.btn_sure);
        if (msg == null || cancle == null || sure == null) return;

        if (index == 1) {
            msg.setText("删除后不可恢复，是否删除该条目？");
        } else {
            msg.setText("删除后不可恢复，是否删除这" + index + "个条目？");
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = mRadioAdapter.getMyLiveList().size(), j =0 ; i > j; i--) {
                    BaoxiaoContentEntity myLive = mRadioAdapter.getMyLiveList().get(i-1);
                    if (myLive.isSelect()) {

                        mRadioAdapter.getMyLiveList().remove(myLive);
                        index--;
                    }
                }
                index = 0;
                mTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (mRadioAdapter.getMyLiveList().size() == 0){
                    mLlMycollectionBottomDialog.setVisibility(View.GONE);
                }
                mRadioAdapter.notifyDataSetChanged();
                builder.dismiss();
            }
        });
    }

    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {
            mBtnEditor.setText("完成");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mBtnEditor.setText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            mLlMycollectionBottomDialog.bringToFront();
            editorStatus = false;
            clearAll();
            clearSelected();
        }
        mRadioAdapter.setEditMode(mEditMode);
    }

    private void clearSelected(){
        for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
            mRadioAdapter.getMyLiveList().get(i).setSelect(false);
        }
        index = 0;
        mBtnDelete.setEnabled(false);
        isSelectAll = false;
    }

    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        setBtnBackground(0);
    }

    @Override
    public void onItemClickListener(int pos, List<BaoxiaoContentEntity> myLiveList) {
        if (editorStatus) {
            BaoxiaoContentEntity myLive = myLiveList.get(pos);
            boolean isSelect = myLive.isSelect();
            if (!isSelect) {
                index++;
                myLive.setSelect(true);
                if (index == myLiveList.size()) {
                    isSelectAll = true;
                    mSelectAll.setText("取消全选");
                }

            } else {
                myLive.setSelect(false);
                index--;
                isSelectAll = false;
                mSelectAll.setText("全选");
            }
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
            mRadioAdapter.notifyDataSetChanged();
        }
    }
}
