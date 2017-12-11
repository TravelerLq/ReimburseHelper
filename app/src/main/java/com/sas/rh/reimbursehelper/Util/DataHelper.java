package com.sas.rh.reimbursehelper.Util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.sas.rh.reimbursehelper.Dao.BaoxiaoItem;

public class DataHelper extends SQLiteOpenHelper {

    public DataHelper(Context context, String name, CursorFactory factory,
                             int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE if not exists BaoxiaoItem_Table(billid varchar ,sum varchar ,xflxsp varchar ,fplxsp varchar ,datepicker varchar ,remarket varchar ," +
                "p1 varchar ,p2 varchar ,p3 varchar ,p4 varchar ,p5 varchar ,p6 varchar ,p7 varchar ,p8 varchar ,p9 varchar)";
        db.execSQL(sql);
    }

    //
    public static boolean addBaoxiaoItem(DataHelper sqlhelper, BaoxiaoItem bitem){

        try {
            //获取数据库对象
            SQLiteDatabase db = sqlhelper.getReadableDatabase();
            db.execSQL("insert into BaoxiaoItem_Table(billid ,sum ,xflxsp ,fplxsp ,datepicker ,remarket ," +
                            "p1 ,p2 ,p3 ,p4 ,p5 ,p6 ,p7 ,p8 ,p9) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{bitem.getBillid(), bitem.getSum(), bitem.getXflxsp(), bitem.getFplxsp(), bitem.getDatepicker(), bitem.getRemarket(),
                            bitem.getP1(), bitem.getP2(), bitem.getP3(), bitem.getP4(), bitem.getP5(), bitem.getP6(), bitem.getP7(), bitem.getP8(), bitem.getP9()});
            //ursor.close();//关闭结果集
            db.close();//关闭数据库对象
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<BaoxiaoItem> getAllBaoxiaoItem(DataHelper sqlhelper){
        List<BaoxiaoItem> list = new ArrayList<BaoxiaoItem>();
        SQLiteDatabase db=sqlhelper.getReadableDatabase();
        Cursor cursor = db.query("BaoxiaoItem_Table", null, null, null, null, null, "billid asc");
        int billidIndex = cursor.getColumnIndex("billid");
        int sumIndex=cursor.getColumnIndex("sum");
        int xflxspIndex=cursor.getColumnIndex("xflxsp");
        int fplxspIndex=cursor.getColumnIndex("fplxsp");
        int datepickerIndex=cursor.getColumnIndex("datepicker");
        int remarketIndex=cursor.getColumnIndex("remarket");
        int p1Index=cursor.getColumnIndex("p1");
        int p2Index=cursor.getColumnIndex("p2");
        int p3Index=cursor.getColumnIndex("p3");
        int p4Index=cursor.getColumnIndex("p4");
        int p5Index=cursor.getColumnIndex("p5");
        int p6Index=cursor.getColumnIndex("p6");
        int p7Index=cursor.getColumnIndex("p7");
        int p8Index=cursor.getColumnIndex("p8");
        int p9Index=cursor.getColumnIndex("p9");
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            BaoxiaoItem bi =new BaoxiaoItem();
            bi.setBillid(cursor.getString(billidIndex));
            bi.setSum(cursor.getString(sumIndex));
            bi.setXflxsp(cursor.getString(xflxspIndex));
            bi.setFplxsp(cursor.getString(fplxspIndex));
            bi.setDatepicker(cursor.getString(datepickerIndex));
            bi.setRemarket(cursor.getString(remarketIndex));
            bi.setP1(cursor.getString(p1Index));
            bi.setP2(cursor.getString(p2Index));
            bi.setP3(cursor.getString(p3Index));
            bi.setP4(cursor.getString(p4Index));
            bi.setP5(cursor.getString(p5Index));
            bi.setP6(cursor.getString(p6Index));
            bi.setP7(cursor.getString(p7Index));
            bi.setP8(cursor.getString(p8Index));
            bi.setP9(cursor.getString(p9Index));
            list.add(bi);

        }
        cursor.close();//关闭结果集
        db.close();//关闭数据库对象
        return list;
    }

    public static BaoxiaoItem getABaoxiaoItem(DataHelper sqlhelper,String billid){
        SQLiteDatabase db=sqlhelper.getReadableDatabase();
        Cursor cursor = db.query("BaoxiaoItem_Table", null, null, null, null, null, "billid asc");
        int billidIndex = cursor.getColumnIndex("billid");
        int sumIndex=cursor.getColumnIndex("sum");
        int xflxspIndex=cursor.getColumnIndex("xflxsp");
        int fplxspIndex=cursor.getColumnIndex("fplxsp");
        int datepickerIndex=cursor.getColumnIndex("datepicker");
        int remarketIndex=cursor.getColumnIndex("remarket");
        int p1Index=cursor.getColumnIndex("p1");
        int p2Index=cursor.getColumnIndex("p2");
        int p3Index=cursor.getColumnIndex("p3");
        int p4Index=cursor.getColumnIndex("p4");
        int p5Index=cursor.getColumnIndex("p5");
        int p6Index=cursor.getColumnIndex("p6");
        int p7Index=cursor.getColumnIndex("p7");
        int p8Index=cursor.getColumnIndex("p8");
        int p9Index=cursor.getColumnIndex("p9");
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            BaoxiaoItem bi =new BaoxiaoItem();
            if(cursor.getString(billidIndex).trim().equals(billid.trim())){
                bi.setBillid(cursor.getString(billidIndex));
                bi.setSum(cursor.getString(sumIndex));
                bi.setXflxsp(cursor.getString(xflxspIndex));
                bi.setFplxsp(cursor.getString(fplxspIndex));
                bi.setDatepicker(cursor.getString(datepickerIndex));
                bi.setRemarket(cursor.getString(remarketIndex));
                bi.setP1(cursor.getString(p1Index));
                bi.setP2(cursor.getString(p2Index));
                bi.setP3(cursor.getString(p3Index));
                bi.setP4(cursor.getString(p4Index));
                bi.setP5(cursor.getString(p5Index));
                bi.setP6(cursor.getString(p6Index));
                bi.setP7(cursor.getString(p7Index));
                bi.setP8(cursor.getString(p8Index));
                bi.setP9(cursor.getString(p9Index));
                cursor.close();//关闭结果集
                db.close();//关闭数据库对象
                return bi;
            }


        }
        cursor.close();//关闭结果集
        db.close();//关闭数据库对象
        return null;
    }

	public static boolean alterABill(DataHelper sqlhelper,BaoxiaoItem bi){

//        String sql = "CREATE TABLE if not exists BaoxiaoItem_Table(billid varchar ,sum varchar ,xflxsp varchar ,fplxsp varchar ,datepicker varchar ,remarket varchar ," +
//                "p1 varchar ,p2 varchar ,p3 varchar ,p4 varchar ,p5 varchar ,p6 varchar ,p7 varchar ,p8 varchar ,p9 varchar)";
        try {
		String sql="update BaoxiaoItem_Table set sum = '"+ bi.getSum()+"',xflxsp ='"+bi.getXflxsp()+"',fplxsp ='"+bi.getFplxsp()+"',datepicker ='"+bi.getDatepicker()
                +"',remarket ='"+bi.getRemarket()+"',p1 ='"+bi.getP1()+"',p2 ='"+bi.getP2()+"',p3 ='"+bi.getP3()+"',p4 ='"+bi.getP4()+"',p5 ='"+bi.getP5()+"',p6 ='"
                +bi.getP6()+"',p7 ='"+bi.getP7()+"',p8 ='"+bi.getP8()+"',p9 ='"+bi.getP9()+"' where billid ='" +bi.getBillid()+"'";
		SQLiteDatabase db=sqlhelper.getWritableDatabase();
		db.execSQL(sql);
        db.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

	}

    public static boolean deleteone(DataHelper sqlhelper,String idlist){
        try {
            SQLiteDatabase db=sqlhelper.getWritableDatabase();
    //	    for(int i =0;i<idlist.size();i++){
            String sql="delete from BaoxiaoItem_Table where billid ='"+idlist+"'";
            db.execSQL(sql);
    //        }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
	}
//
//	public static void alternotetitle(appDatabaseHelper sqlhelper,usernoteEntity newune,String oldtitle){
//
//		String sql="update notes_db set note_title = '"+newune.getTitle()+"' where note_title ='" +oldtitle+"' AND name ='"+newune.getName()+"'";
//		String sql2="update notes_db set note_text = '"+newune.getNotetext()+"' where note_title ='" +oldtitle+"' AND name ='"+newune.getName()+"'";
//		SQLiteDatabase db=sqlhelper.getWritableDatabase();
//		db.execSQL(sql2);
//		db.execSQL(sql);
//
//	}
//
//	public static void deleteone(appDatabaseHelper sqlhelper,usernoteEntity une){
//		String sql="delete from notes_db where name='"+une.getName()+"' AND note_title ='"+une.getTitle()+"'";
//		SQLiteDatabase db=sqlhelper.getWritableDatabase();
//		db.execSQL(sql);
//	}
//
//	public static void overrideall(appDatabaseHelper sqlhelper,List<usernoteEntity> unes,String authorname){
//		String deletesql="delete from notes_db where name='"+authorname+"'";
//		SQLiteDatabase db=sqlhelper.getWritableDatabase();
//		db.execSQL(deletesql);
//		if(unes.size()==0)
//			return;
//		for(usernoteEntity une:unes){
//			db.execSQL("insert into notes_db(name,note_title,note_text) values('"+une.getName()+"','"+une.getTitle()+"','"+une.getNotetext()+"')");
//		}
//
//
//	}
//

}

