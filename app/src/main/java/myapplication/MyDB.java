package myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wengjunyu on 2016/1/20.
 */
public class MyDB extends SQLiteOpenHelper{
    private static String DB_NAME = "My_DB.db"; //数据库名
    private static int DB_VERSION = 2; //版本号
    private SQLiteDatabase db; //数据库操作对象
    public MyDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//创建数据库后，对数据库的操作
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //每次成功打开数据库后首先被执行
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更改数据库版本的操作
    }
    //执行SQLite数据库连接
    public SQLiteDatabase openConnection (){
        if (!db.isOpen())
        {
            //以读写的方式获取SQLiteDatabase
            db = getWritableDatabase();
        }
        return db;
    }
    //关闭SQLite数据库连接操作
    public void closeConnection (){
        try {
            if(db!=null&&db.isOpen())
                db.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //创建表
    public boolean createTable(String createTableSql){
        try {
            openConnection();
            db.execSQL(createTableSql);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    //添加表
    public boolean save(String tableName, ContentValues values)
    {
        try {
            openConnection();
            db.insert(tableName, null, values);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    //更新操作
    public boolean update(String table,ContentValues values,String whereClause,String []whereArgs){
        try {
            openConnection();
            db.update(table,values,whereClause,whereArgs);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    //删除
    public boolean delete(String table, String deleteSql,String obj[])
    {
        try {
            openConnection();
            db.delete(table, deleteSql, obj);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    //查询
    public Cursor find(String findSql,String obj[])
    {
        try {
            openConnection();
            Cursor cursor = db.rawQuery(findSql,obj);
            return cursor;
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    //判断表是否存在
    public boolean isTableExits(String tablename){
        try {
            openConnection();
            String str = "select count(*) xcount from " + tablename;
            db.rawQuery(str,null).close();
        }catch (Exception ex)
        {
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
}
