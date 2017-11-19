package cn.edu.gdmec.android.mobileguard.m5virusscan.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by milku on 2017/11/18.
 */

public class AntiVirusDao {
    //检查某个md5是否是病毒

    private static Context context;
    private static String dbname;
    public AntiVirusDao(Context context){
        this.context = context;
        dbname = "/data/data/"+context.getPackageName ()+"/files/antivirus.db";
    }
    //使用apk文件的md5值匹配病毒数据库
    public String checkVirus(String md5){
        String desc = null;
        //打开病毒数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase (
                dbname, null,
                SQLiteDatabase.OPEN_READONLY );
        Cursor cursor = db.rawQuery ( "select desc from datable where md5=?",
                new String[] { md5 });
        if (cursor.moveToNext ()){
            desc = cursor.getString ( 0 );
        }
        cursor.close ();
        db.close ();
        return desc;
    }
    //模块5老师
/*    //判断数据库文件是否存在
    public  boolean isDBExit() {
        File file = new File (dbname);
        return file.exists() && file.length() > 0;
    }*/
    //获取数据库版本号
   /* public  String getDBVersionNum() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname, null,*/
    //获取病毒数据版本
    public String getVirusDbVersion(){
        String dbVersion = null;
        // 打开病毒数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname, null,
                SQLiteDatabase.OPEN_READONLY);
       /* String versionnumber = "0";
        Cursor cursor = db.rawQuery("select  subcnt from version", null);*/
        Cursor cursor = db.rawQuery("select major||'.'||minor||'.'||build from version",null);

        if (cursor.moveToNext()) {
            //versionnumber = cursor.getString(0);
            dbVersion = cursor.getString(0);
        }
        cursor.close();
        db.close();
       /* return versionnumber;
    }
    //更新数据库版本号的操作
    public  void updateDBVersion(int newversion){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname, null,
                SQLiteDatabase.OPEN_READWRITE);
        String versionnumber = "0";
        ContentValues values = new ContentValues ();
        values.put("subcnt", newversion);
        db.update("version", values, null, null);
        db.close();
    }
    //更新病毒数据库的API
    public  void add(String desc,String md5){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbname, null,
                SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5", md5);
        values.put("desc", desc);
        values.put("type", 6);
        values.put("name", "Android.Hack.i22hkt.a");
        db.insert("datable", null, values);
        db.close();*/
        return dbVersion;
    }
}
