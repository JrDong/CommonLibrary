package com.djr.commonlibrary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.djr.commonlibrary.db.dbModel.DateBaseModelBase;
import com.djr.commonlibrary.step.StepModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 数据库帮助类
 */
public class DateBaseHelper extends SQLiteOpenHelper {

    private static DateBaseHelper instance;

    public SQLiteDatabase getwDB() {
        return wDB;
    }

    private SQLiteDatabase wDB;
    private SQLiteDatabase rDB;


    private Map<Class<? extends DateBaseModelBase>, DateBaseModelBase> modes;

    public DateBaseHelper(Context context) {
        super(context, DBConfig.dbName, null, DBConfig.dbVersion);
        initDataBase();
    }

    public static DateBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DateBaseHelper(context.getApplicationContext());
        }
        return instance;
    }


    /**
     * 数据表创建
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        wDB = db;
        rDB = db;
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2 && newVersion>=2){
            Log.d("StepDetector","执行upgrade   "+System.currentTimeMillis());
            StepModel stepModel = new StepModel();
            db.execSQL(stepModel.createTableSql());
        }
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (instance != null) {
            try {
                wDB.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

    private void initDataBase() {
        initModeType();
        wDB = getWritableDatabase();
        rDB = getReadableDatabase();

    }

    private void initModeType() {
        modes = new HashMap<Class<? extends DateBaseModelBase>, DateBaseModelBase>();
        modes.put(StepModel.class, new StepModel());
    }

    private void createTable() {
        Set<Class<? extends DateBaseModelBase>> modeSet = modes.keySet();
        Iterator<Class<? extends DateBaseModelBase>> iterator = modeSet.iterator();
        Class<? extends DateBaseModelBase> modeType = null;
        DateBaseModelBase mode = null;
        while (iterator.hasNext()) {
            modeType = iterator.next();
            if (modeType == null) {
                continue;
            }
            mode = modes.get(modeType);
            if (mode == null) {
                continue;
            }
            execSQL(mode.createTableSql());
        }
    }

    private void execuSQL(String sql, String[] bindArgs) {
        if (wDB != null) {
            wDB.execSQL(sql, bindArgs);
        }
    }

    private void execSQL(String sql) {
        if (wDB != null) {
            wDB.execSQL(sql);
        }
    }

    public void execSQL(String sql, Object[] bindArgs) {
        if (wDB != null) {
            wDB.execSQL(sql, bindArgs);
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        if (rDB != null) {
            return cursor = rDB.rawQuery(sql, selectionArgs);
        }
        return cursor;
    }


}
