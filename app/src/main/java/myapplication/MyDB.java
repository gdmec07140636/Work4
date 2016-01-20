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
    private static String DB_NAME = "My_DB.db"; //���ݿ���
    private static int DB_VERSION = 2; //�汾��
    private SQLiteDatabase db; //���ݿ��������
    public MyDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//�������ݿ�󣬶����ݿ�Ĳ���
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //ÿ�γɹ������ݿ�����ȱ�ִ��
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //�������ݿ�汾�Ĳ���
    }
    //ִ��SQLite���ݿ�����
    public SQLiteDatabase openConnection (){
        if (!db.isOpen())
        {
            //�Զ�д�ķ�ʽ��ȡSQLiteDatabase
            db = getWritableDatabase();
        }
        return db;
    }
    //�ر�SQLite���ݿ����Ӳ���
    public void closeConnection (){
        try {
            if(db!=null&&db.isOpen())
                db.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //������
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
    //��ӱ�
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
    //���²���
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
    //ɾ��
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
    //��ѯ
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
    //�жϱ��Ƿ����
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
