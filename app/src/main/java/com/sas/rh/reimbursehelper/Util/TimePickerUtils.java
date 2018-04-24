package com.sas.rh.reimbursehelper.Util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.warmtel.expandtab.KeyValueBean;

import java.util.Calendar;
import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by liqing on 17/11/14.
 */

public class TimePickerUtils {

    private static TimePickerUtils mInstance;

    public static TimePickerUtils getInstance() {
        if (mInstance == null) {
            mInstance = new TimePickerUtils();
        }
        return mInstance;
    }

    //日期选择
    public void onYearMonthDayPicker(Context context, final View view) {
        Calendar calendar = Calendar.getInstance();
        final String result = null;
        Loger.i("calendar= " + calendar.get(Calendar.YEAR));
        final DatePicker picker = new DatePicker((Activity) context);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(calendar.get(Calendar.YEAR) - 2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setRangeEnd(calendar.get(Calendar.YEAR) + 2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                if (view instanceof TextView) {
                    Loger.e("view instanceof TextView---");
                    ((TextView) view).setText(year + "-" + month + "-" + day);
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(year + "-" + month + "-" + day);
                }

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    // 结束日期
    public void onEndDatePicker(Context context, final View view, final String startTime) {
        Calendar calendar = Calendar.getInstance();
        final String result = null;

        Loger.i("calendar= " + calendar.get(Calendar.YEAR));
        final DatePicker picker = new DatePicker((Activity) context);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(calendar.get(Calendar.YEAR) - 2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setRangeEnd(calendar.get(Calendar.YEAR) + 2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String endTime = "";
                if (view instanceof TextView) {
                    Loger.e("view instanceof TextView---");
                    ((TextView) view).setText(year + "-" + month + "-" + day);
                    endTime = ((TextView) view).getText().toString().trim();
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(year + "-" + month + "-" + day);
                    endTime = ((EditText) view).getText().toString().trim();
                }
                Loger.e("startTime=" + startTime + "endtime" + endTime);
                checkDate(startTime, endTime);

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int i, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int i, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());

            }

            @Override
            public void onDayWheeled(int i, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
//            @Override
//            public void onYearWheeled(int index, String year) {
//                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onMonthWheeled(int index, String month) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onDayWheeled(int index, String day) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
//            }
        });
        picker.show();
    }


    private void checkDate(String startTime, String endTime) {

        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            if (TimeUtils.timeCompare(startTime, endTime, "yyyy-MM-dd")) {
                // SimpleToast.ToastMessage("结束日期不合法！");
                return;
            }
        }
    }

    public View onListDataPicker(Activity context, final List<String> allStatus, final View view) {
        final int[] pos = {0};
        final SinglePicker<String> picker = new SinglePicker<String>(context, allStatus);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(false);
        picker.setItemWidth(200);
        picker.setTopPadding(15);
        picker.setTextSize(25);
        picker.setSelectedIndex(0);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int i, String s) {
                pos[0] = i;
                Loger.e("i," + i);
            }
        });

        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                Loger.e("viewName=" + (view instanceof TextView));

                if (view instanceof TextView) {
                    Loger.e("view instanceof TextView---");
                    ((TextView) view).setText(allStatus.get(i));
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(allStatus.get(i));
                }

                view.setTag(i);
            }
        });
        picker.setWeightEnable(true);

        if (view.getTag() == null) {
            view.setTag(0);
        }

        picker.show();
        Loger.e("view.selectPOs()" + view.getTag());
        return view;
    }


    public void onListPicker(Activity context, final List<KeyValueBean> allStatus, final View view) {
        final SinglePicker<KeyValueBean> picker = new SinglePicker<KeyValueBean>(context, allStatus);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setSelectedIndex(0);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int i, String s) {

            }
        });

        picker.setOnItemPickListener(new OnItemPickListener<KeyValueBean>() {
            @Override
            public void onItemPicked(int i, KeyValueBean s) {
                Loger.e("viewName=" + (view instanceof TextView));
                if (view instanceof TextView) {
                    Loger.e("view instanceof TextView---" + allStatus.get(i).getValue());

                    ((TextView) view).setText(allStatus.get(i).getValue());
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(allStatus.get(i).getValue());
                }

            }
        });
        picker.setWeightEnable(true);

        picker.show();
    }

    /**
     * 证照上传的接口
     * @param context
     * @param allStatus
     * @param sourceData
     * @param adapter
     */
//    public void  onListDataPicker(Activity context, final List<String> allStatus,final CompanyCerificateModifyBean sourceData,final RecyclerView.Adapter adapter) {
//        final SinglePicker<String> picker = new SinglePicker<String>(context, allStatus);
//        picker.setCanLoop(false);
//        picker.setWheelModeEnable(true);
//        picker.setTopPadding(15);
//        picker.setSelectedIndex(0);
//        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
//            @Override
//            public void onWheeled(int i, String s) {
//
//            }
//        });
//
//        picker.setOnItemPickListener(new OnItemPickListener<String>() {
//            @Override
//            public void onItemPicked(int i, String s) {
//                sourceData.setCertificationName(allStatus.get(i));
//                sourceData.setCetificationType(i+"");
//                adapter.notifyDataSetChanged();
//            }
//        });
//        picker.setWeightEnable(true);
//
//        picker.show();
//    }
//
//    //证照上传时间
//    public void onYearMonthDayPickerInUploda(Context context, final CompanyCerificateModifyBean sourceData, final RecyclerView.Adapter adapter, final int type) {
//        Calendar calendar = Calendar.getInstance();
//        final String result = null;
//        Loger.i("calendar= " + calendar.get(Calendar.YEAR));
//        final DatePicker picker = new DatePicker((Activity) context);
//        picker.setCanLoop(false);
//        picker.setWheelModeEnable(true);
//        picker.setTopPadding(15);
//        picker.setRangeStart(calendar.get(Calendar.YEAR)-2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
//        picker.setRangeEnd(calendar.get(Calendar.YEAR)+2, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
//        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
//        picker.setWeightEnable(true);
//        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
//            @Override
//            public void onDatePicked(String year, String month, String day) {
//                //开始时间
//                if(type == 0){
//                    sourceData.setCetificationDate(year+"-"+month+"-"+day);
//                }else{
//                    sourceData.setCetificationExpiration(year+"-"+month+"-"+day);
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//        });
//        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
//            @Override
//            public void onYearWheeled(int index, String year) {
//                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onMonthWheeled(int index, String month) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
//            }
//
//            @Override
//            public void onDayWheeled(int index, String day) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
//            }
//        });
//        picker.show();
//    }

}
