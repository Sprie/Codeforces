package com.axic.codeforces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 59786 on 2016/4/8.
 */
public class ProblemsQuestionsDBManager {

    public ProblemsQuestionsDbHelper helper;
    private SQLiteDatabase db;
    private String dbName = "problemsQuestion.db";

    public ProblemsQuestionsDBManager(Context context, int version){
        helper = new ProblemsQuestionsDbHelper(context,dbName,1);
        db = helper.getWritableDatabase();
    }




    //程序退出时需要调用close()释放数据库资源
    public void closeDB() {
        db.close();
    }

}
