package com.sas.rh.reimbursehelper.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.MemberDetailInfoEntity;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Sortlist.CharacterParser;
import com.sas.rh.reimbursehelper.Sortlist.ClearEditText;
import com.sas.rh.reimbursehelper.Sortlist.PinyinComparator;
import com.sas.rh.reimbursehelper.Sortlist.SideBar;
import com.sas.rh.reimbursehelper.Sortlist.SortGroupMemberAdapter;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MembersManageOldActivity extends AppCompatActivity implements SectionIndexer {

    private ImageView backbt, add_memberitem;
    PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortGroupMemberAdapter adapter;
    private ClearEditText mClearEditText;

    private LinearLayout titleLayout;
    private TextView title;
    private TextView tvNofriends;

    private int lastFirstVisibleItem = -1;
    private SharedPreferencesUtil spu;

    private CharacterParser characterParser;
    private List<MemberDetailInfoEntity> SourceDateList = new ArrayList<MemberDetailInfoEntity>();
    private List<UserBean> userList = new ArrayList<>();

    private PinyinComparator pinyinComparator;

    private ProgressDialogUtil pdu = new ProgressDialogUtil(MembersManageOldActivity.this, "提示", "提交更改中");
    String member_id;
    private Handler memberlistback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            ptrClassicFrameLayout.refreshComplete();
            ptrClassicFrameLayout.setLoadMoreEnable(false);
            if (pdu.getMypDialog() != null) {
                pdu.dismisspd();
            }

            if (msg.what == 1) {
                List<UserBean> list = JSONArray.parseArray(jsonArray.toJSONString(), UserBean.class);
                userList.clear();
                userList.addAll(list);


//
//                if (memberlist.get("resultList") != null) {
//                    //System.out.print("resultList:");
//                    JSONArray jsonArray = memberlist.getJSONArray("resultList");
//                    for (Object object : jsonArray) {
//                        JSONObject jObject = JSONObject.fromObject(object);
//                        MemberDetailInfoEntity mdi = new MemberDetailInfoEntity();
//                        mdi.setMember_id(jObject.get("ygId").toString());
//                        mdi.setMember_name(jObject.get("ygName").toString());
//                        mdi.setMember_sex(jObject.get("ygGender").toString());
//                        mdi.setMember_telnum(jObject.get("ygDianhua").toString());
//                        //mdi.setMember_mail(jObject.get("ygYoujian").toString());
//                        //mdi.setMember_birthday(jObject.get("ygBrith").toString());
//                        //mdi.setMember_enterday(jObject.get("ygEnterdate").toString());
//                        mdi.setMember_gsid(jObject.get("gongsiId").toString());
//                        SourceDateList.add(mdi);
//                    }
//                }


//                if (SourceDateList.size() != 0) {
//
//                        filledData(SourceDateList);
//                        Collections.sort(SourceDateList, pinyinComparator);
//                        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                            }
//
//                            @Override
//                            public void onScroll(AbsListView view, int firstVisibleItem,
//                                                 int visibleItemCount, int totalItemCount) {
//                                int section = getSectionForPosition(firstVisibleItem);
//                                int nextSection = getSectionForPosition(firstVisibleItem + 1);
//                                int nextSecPosition = getPositionForSection(+nextSection);
//                                if (firstVisibleItem != lastFirstVisibleItem) {
//                                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
//                                            .getLayoutParams();
//                                    params.topMargin = 0;
//                                    titleLayout.setLayoutParams(params);
//                                    title.setText(SourceDateList.get(
//                                            getPositionForSection(section)).getSortLetters());
//                                }
//                                if (nextSecPosition == firstVisibleItem + 1) {
//                                    View childView = view.getChildAt(0);
//                                    if (childView != null) {
//                                        int titleHeight = titleLayout.getHeight();
//                                        int bottom = childView.getBottom();
//                                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
//                                                .getLayoutParams();
//                                        if (bottom < titleHeight) {
//                                            float pushedDistance = bottom - titleHeight;
//                                            params.topMargin = (int) pushedDistance;
//                                            titleLayout.setLayoutParams(params);
//                                        } else {
//                                            if (params.topMargin != 0) {
//                                                params.topMargin = 0;
//                                                titleLayout.setLayoutParams(params);
//                                            }
//                                        }
//                                    }
//                                }
//                                lastFirstVisibleItem = firstVisibleItem;
//                            }
//                        });
//                }
                adapter.notifyDataSetChanged();
                //  ToastUtil.showToast(MembersManageActivity.this, memberlist.get("HostTime") + ":" + memberlist.get("Note").toString(), Toast.LENGTH_LONG);

            } else if (msg.what == 2) {
                // ToastUtil.showToast(MembersManageActivity.this, memberlist.get("HostTime") + ":" + memberlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            } else if (msg.what == 0) {
                ToastUtil.showToast(MembersManageOldActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(MembersManageOldActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_manage);
        spu=new SharedPreferencesUtil(MembersManageOldActivity.this);

        backbt = (ImageView) findViewById(R.id.backbt);
        add_memberitem = (ImageView) findViewById(R.id.add_memberitem);


        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_memberitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MembersManageOldActivity.this, MembersManageAddStaffActivity.class);
                startActivityForResult(it, 1);
            }
        });

        initViews();
    }


    private void initViews() {
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        title = (TextView) this.findViewById(R.id.title_layout_catalog);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_recycler_view_frame);
        tvNofriends = (TextView) this
                .findViewById(R.id.title_layout_no_friends);
        // ʵ��������תƴ����
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // �����Ҳഥ������
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // ����ĸ�״γ��ֵ�λ��
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 跳到详情
//                Intent it = new Intent(MembersManageActivity.this, MembersManageDetailInfoActivity.class);
//                startActivity(it);
                //选择user
                // Intent it = new Intent(MembersManageActivity.this, MembersManageDetailInfoActivity.class);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userList.get(position));
//                intent.putExtra("userId", mData.get(pos).getKey());
//                intent.putExtra("thirdValue", mData.get(pos).getValue());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
//                startActivity(it);

            }
        });

        sortListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(MembersManageOldActivity.this)
                        .setMessage("确定删除该项？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                member_id = SourceDateList.get(i).getMember_id();
                                DeleteMember();
                            }
                        }).show();
                return false;
            }
        });

        //SourceDateList = filledData(getResources().getStringArray(R.array.date));

        // ����a-z��������Դ����

        adapter = new SortGroupMemberAdapter(this, userList);
        sortListView.setAdapter(adapter);
        GetallMemberInfo();
//        ptrClassicFrameLayout.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                ptrClassicFrameLayout.autoRefresh(true);
//            }
//        }, 150);
//        //下拉刷新
//        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                GetallMemberInfo();
//            }
//        });

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        // �������������ֵ�ĸı�����������
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // ���ʱ����Ҫ��ѹЧ�� �Ͱ������ص�
                titleLayout.setVisibility(View.GONE);
                // ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * ΪListView�������
     *
     * @param
     * @return
     */
    private void filledData(List<MemberDetailInfoEntity> list) {
        List<MemberDetailInfoEntity> mSortList = new ArrayList<MemberDetailInfoEntity>();

        for (int i = 0; i < list.size(); i++) {
//            GroupMemberBean sortModel = new GroupMemberBean();
//            sortModel.setName(date[i]);
            // ����ת����ƴ��
            String pinyin = characterParser.getSelling(list.get(i).getMember_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
            if (sortString.matches("[A-Z]")) {
                list.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                list.get(i).setSortLetters("#");
            }
        }

    }

    /**
     * ����������е�ֵ���������ݲ�����ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<MemberDetailInfoEntity> filterDateList = new ArrayList<MemberDetailInfoEntity>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (MemberDetailInfoEntity sortModel : SourceDateList) {
                String name = sortModel.getMember_name();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // ����a-z��������
//        Collections.sort(filterDateList, pinyinComparator);
//        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    /**
     * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void GetallMemberInfo() {
        new Thread(GetMemberInfoThread).start();
    }

    private void DeleteMember() {
        pdu.showpd();
        new Thread(DeleteMemberThread).start();
    }

    Runnable GetMemberInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = new UserUtil().getALlUser(spu.getUidNum());
                if (jo != null) {
                    jsonArray = jo;
                    memberlistback.sendEmptyMessage(1);
                } else {
                    memberlistback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                memberlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable DeleteMemberThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                // JSONObject jo = new YuangongUtils().delete(Integer.parseInt(member_id.trim()));
//                if (jo != null) {
//                  //  memberlist = jo;
//                    memberlistback.sendEmptyMessage(2);
//                } else {
//                    memberlistback.sendEmptyMessage(0);
//                }
            } catch (Exception e) {
                memberlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
