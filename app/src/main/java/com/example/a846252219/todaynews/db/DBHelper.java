package com.example.a846252219.todaynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.a846252219.todaynews.bean.NewTabBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by 846252219 on 2018/6/27.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper{


    public DBHelper(Context context) {
        super(context, "todaynews.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NewTabBean.NewsBean.class);
            //Dao对象即可身上有怎删改查的方法

        } catch (SQLException e) {
e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public Dao getDao(Class clazz)  {
        try {
            Dao dao = super.getDao(clazz);
            return dao;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
