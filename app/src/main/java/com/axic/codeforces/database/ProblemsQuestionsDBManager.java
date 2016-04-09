package com.axic.codeforces.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by 59786 on 2016/4/8.
 */
public class ProblemsQuestionsDBManager {

    public ProblemsQuestionsDbHelper helper;
    private SQLiteDatabase db;
    private String dbName = "problemsQuestion.db";

    public ProblemsQuestionsDBManager(Context context, int version) {
        helper = new ProblemsQuestionsDbHelper(context, dbName, 1);
        db = helper.getWritableDatabase();
    }

    public void addQuestions(String contestId, String questions) {
        db.beginTransaction();//开始事务
        try {

            db.execSQL("INSERT into problemsQuestion (id,questions)" +
                            " values(?,?)",
                    new String[]{contestId, questions});
            Log.d("addPQuestionsToDb", "true");
            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
    }

    public String getQustions(String contestId) {
        String questions = null;
        db.beginTransaction();//开始事务
        try {
            Cursor c = db.rawQuery("SELECT * FROM problemsQuestion " +
                    "WHERE id = ? ", new String[]{contestId});
//        int tag = c.getCount();


//        if (tag != 0) {
//            while (c.moveToNext()) {
            c.moveToNext();
            Log.d("cCount", c.getCount() + c.getString(c.getColumnIndex("questions")));

            questions = c.getString(c.getColumnIndex("questions"));
            Log.d("getQuestionsFromDB", questions + "null");
//            }
            c.close();

            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
        return questions;
    }

    public boolean hasQuestions(String contestId) {


        db.beginTransaction();//开始事务
        try {
            Cursor c = db.rawQuery("SELECT * FROM problemsQuestion " +
                    "WHERE id = ? ", new String[]{contestId});
            if (c.getCount() == 0) {
                return false;
            }
        } finally {
            db.endTransaction();//完成事务
        }
        return true;
    }

    //程序退出时需要调用close()释放数据库资源

    public void closeDB() {
        db.close();
    }

}
