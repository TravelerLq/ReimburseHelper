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

import com.sas.rh.reimbursehelper.Dao.BaoxiaoItem;
import com.sas.rh.reimbursehelper.Bean.BaoxiaoContentEntity;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.MineRadioAdapter;
import com.sas.rh.reimbursehelper.Util.DataHelper;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;
import com.sas.rh.reimbursehelper.view.activity.MyApprovalActivity;
import com.sas.rh.reimbursehelper.view.activity.MyFeeActivity;
import com.sas.rh.reimbursehelper.view.activity.MyReimburseActivity;
import com.sas.rh.reimbursehelper.view.activity.PersonalDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.ReimburseBillCreateActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomepageFragment extends Fragment implements View.OnClickListener, MineRadioAdapter.OnItemClickListener {

    private PopupMenu popupMenu;
    private Menu menu;
    private ImageButton menubt;
    private ImageView personaldetail_btn;
    private RelativeLayout addbxnrbt;
    private List<BaoxiaoItem> resultlist;
    private TextView weibaoxiaofytv;
    private double sum = 0.00;
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
    private List<BaoxiaoContentEntity> mList;
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
    private TextView mybxbt, myspbt, myfybt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        refreshbxlist();
        initListener();
        return view;
    }

    private void initView(View view) {
        menubt = (ImageButton) view.findViewById(R.id.popupmenu_btn);
        addbxnrbt = (RelativeLayout) view.findViewById(R.id.addbxnrbt);
        personaldetail_btn = (ImageView) view.findViewById(R.id.personaldetail_btn);
        weibaoxiaofytv = (TextView) view.findViewById(R.id.weibaoxiaofytv);
        mybxbt = (TextView) view.findViewById(R.id.mybxbt);
        myspbt = (TextView) view.findViewById(R.id.myspbt);
        myfybt = (TextView) view.findViewById(R.id.myfybt);
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
                //  Intent it = new Intent(getActivity(),AddBaoxiaojizhuActivity.class);
                Intent it = new Intent(getActivity(), AddExpenseActivity.class);
                it.putExtra("rcode", 1);
                startActivityForResult(it, 1);
            }
        });

        personaldetail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), PersonalDetailActivity.class);
                startActivity(it);
            }
        });

        mybxbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), MyReimburseActivity.class);
                startActivity(it);
            }
        });

        myspbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), MyApprovalActivity.class);
                startActivity(it);
            }
        });

        myfybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), MyFeeActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2) {
            //int position = data.getExtras().getInt("position");
            refreshbxlist();
            index = 0;
            mTvSelectNum.setText(String.valueOf(index));
        }
    }

    private void initData() {
        mRadioAdapter = new MineRadioAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_main_bg_height_1));
        mRecyclerview.addItemDecoration(itemDecorationHeader);
        mRecyclerview.setAdapter(mRadioAdapter);
//        mRadioAdapter.setOnItemClickListener(this);
        mRadioAdapter.setOnItemClickListener(new MineRadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos, List<BaoxiaoContentEntity> myLiveList) {
                // Intent it = new Intent(getActivity(),AddBaoxiaojizhuActivity.class);
                Intent it = new Intent(getActivity(), AddExpenseActivity.class);
                it.putExtra("rcode", 2);
                it.putExtra("biiid", myLiveList.get(pos).getBxid());
                startActivityForResult(it, 2);
            }
        });

    }

    public void refreshbxlist() {
        sum = 0;
        mList = new ArrayList<>();
        resultlist = DataHelper.getAllBaoxiaoItem(new DataHelper(getActivity(), "BaoxiaoItem_DB", null, 1));
        if (resultlist.size() == 0) {
            //BaoxiaoContentEntity myLiveList = new BaoxiaoContentEntity();
            //mList.add(myLiveList);
            mRadioAdapter.notifyAdapter(mList, false);
        }
        for (int i = 0; i < resultlist.size(); i++) {
            BaoxiaoContentEntity myLiveList = new BaoxiaoContentEntity();
            myLiveList.setBxid(resultlist.get(i).getBillid());
            myLiveList.setBxtype(resultlist.get(i).getXflxsp());
            myLiveList.setBxnum(resultlist.get(i).getSum());
            DecimalFormat df = new DecimalFormat("#####0.00 ");
            sum += Double.parseDouble(resultlist.get(i).getSum().trim());
            myLiveList.setBxdate(resultlist.get(i).getDatepicker());
            mList.add(myLiveList);
            weibaoxiaofytv.setText("¥" + df.format(sum));
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
        //mRadioAdapter.setOnItemClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mBtnEditor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                deleteVideo();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.btn_editor:
                if (editorStatus == true && index >= 1) {
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
                                    //updataEditMode();
                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int index = mRadioAdapter.getMyLiveList().size(), j = 0; index > j; index--) {
                                        BaoxiaoContentEntity myLive = mRadioAdapter.getMyLiveList().get(index - 1);
                                        if (myLive.isSelect()) {
                                            list.add(myLive.getBxid());
                                        }
                                    }
                                    Intent it = new Intent(getActivity(), ReimburseBillCreateActivity.class);
                                    it.putStringArrayListExtra("billids", list);
                                    startActivity(it);
                                }
                            }).show();

                } else {
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
        if (index == 0) {
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
                DecimalFormat df = new DecimalFormat("#####0.00 ");
                double jiansum = 0;
                for (int i = mRadioAdapter.getMyLiveList().size(), j = 0; i > j; i--) {
                    BaoxiaoContentEntity myLive = mRadioAdapter.getMyLiveList().get(i - 1);
                    //jiansum += Double.parseDouble(myLive.getBxnum().trim());
                    if (myLive.isSelect()) {
                        jiansum += Double.parseDouble(myLive.getBxnum().trim());
                        Boolean addsuc = DataHelper.deleteone(new DataHelper(getActivity(), "BaoxiaoItem_DB", null, 1), myLive.getBxid());
                        if (addsuc == false) {
                            Toast.makeText(getActivity(), "删除" + i + "失败", Toast.LENGTH_SHORT).show();
                        }
                        mRadioAdapter.getMyLiveList().remove(myLive);
                        index--;
                    }
                }
                String[] s = weibaoxiaofytv.getText().toString().split("¥");
                //System.out.println("gggggg"+Double.parseDouble(s[1])+"-"+jiansum);
                double result = Double.parseDouble(s[1]) - jiansum;
                weibaoxiaofytv.setText("¥" + df.format(result));
                index = 0;
                mTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (mRadioAdapter.getMyLiveList().size() == 0) {
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
            mRadioAdapter.setOnItemClickListener(this);
            mBtnEditor.setText("完成");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mRadioAdapter.setOnItemClickListener(new MineRadioAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int pos, List<BaoxiaoContentEntity> myLiveList) {
                    //  Intent it = new Intent(getActivity(),AddBaoxiaojizhuActivity.class);
                    Intent it = new Intent(getActivity(), AddExpenseActivity.class);
                    it.putExtra("rcode", 2);
                    it.putExtra("biiid", myLiveList.get(pos).getBxid());
                    startActivityForResult(it, 2);
                }
            });
            mBtnEditor.setText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            mLlMycollectionBottomDialog.bringToFront();
            editorStatus = false;
            clearAll();
            clearSelected();
        }
        mRadioAdapter.setEditMode(mEditMode);
    }

    private void clearSelected() {
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
