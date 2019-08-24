package ru.startandroid.t7.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static ru.startandroid.t7.model.Utils.LOG_TAG;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "vkPhotosTable";
    private static final String DESCRIPTION_1 = "description";
    private static final String URL_2 = "URL";
    private static final int DESCRIPTION_1_id = 1;
    private static final int URL_2_id = 2;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (id integer primary key autoincrement, " +
                DESCRIPTION_1 + " text, " +
                URL_2 + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String getDescription(Cursor cr) {
        String ans = cr.getString(DESCRIPTION_1_id);
        return ans;
    }

    private String getURL(Cursor cr) {
        String ans = cr.getString(URL_2_id);
        return ans;
    }

    private Cursor getAllData() {
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME, null);
        return cr;
    }

    private int getId(String descr, String url) {
        Cursor cr = getAllData();
        int ans = -1;
        while (cr.moveToNext()) {
            if (getDescription(cr).equals(descr) && getURL(cr).equals(url)) {
                ans = cr.getShort(0);
                break;
            }
        }
        cr.close();
        return ans;
    }

    public Observable<Boolean> check(final String descr, final String url) {
        Callable<Boolean> f = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                boolean ans = getId(descr, url) != -1;
                return ans;
            }
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ImageData[]> getData() {
        Callable<ImageData[]> f = new Callable<ImageData[]>() {
            @Override
            public ImageData[] call() {
                Cursor cr = getAllData();
                ImageData[] ans = new ImageData[cr.getCount()];
                int ind = 0;
                while (cr.moveToNext()) {
                    ans[ind] = new ImageData(ind, getURL(cr), getDescription(cr));
                    ind++;
                }
                cr.close();
                return ans;
            }
        };
        return Observable.fromCallable(f).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void delete(final String descr, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ind = getId(descr, url);
                if (ind != -1) {
                    db.execSQL("delete from " + TABLE_NAME + " where id = " + Integer.toString(ind) + ";");
                }
            }
        }).run();
    }

    public void add(final String descr, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getId(descr, url) == -1) {
                    ContentValues cv = new ContentValues();
                    cv.put(DESCRIPTION_1, descr);
                    cv.put(URL_2, url);
                    long ans = db.insert(TABLE_NAME, null, cv);
                }
            }
        }).run();

    }
}
