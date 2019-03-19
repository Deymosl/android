package ru.startandroid.t7;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import ru.startandroid.t7.model.DBHelper;
import ru.startandroid.t7.model.ImageData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static android.support.test.InstrumentationRegistry.*;
import static org.junit.Assert.*;

import static ru.startandroid.t7.model.Utils.TEST_LOG_TAG;

@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    private DBHelper helper;
    private CompositeDisposable cd;
    private String url;
    private String descr;
    private Consumer<Boolean> aFalse, aTrue;
    private final int countN = 10;

    @Before
    public void setUp() {
        Log.d(TEST_LOG_TAG, "setUp");
        getTargetContext().deleteDatabase(DBHelper.TABLE_NAME);
        helper = new DBHelper(getTargetContext());
        cd = new CompositeDisposable();
        url = "https://i.imgur.com/Kysvo0D.png";
        descr = "4red";
        aFalse = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                assertFalse(aBoolean);
            }
        };
        aTrue = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                assertTrue(aBoolean);
            }
        };
    }

    @After
    public void tearDown() {
        Log.d(TEST_LOG_TAG, "tearDown");
        cd.clear();
        helper.close();
    }

    private void checkHasOne(boolean has, String descr, String url) throws InterruptedException {
        cd.add(helper.check("Description", "URL").subscribe(aFalse));
        TimeUnit.MILLISECONDS.sleep(10);
        cd.add(helper.check("Description", url).subscribe(aFalse));
        TimeUnit.MILLISECONDS.sleep(10);
        cd.add(helper.check(descr, "URL").subscribe(aFalse));
        TimeUnit.MILLISECONDS.sleep(10);
        cd.add(helper.check(descr, url).subscribe((has ? aTrue : aFalse)));
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @Test
    public void checkEmpty() throws InterruptedException {
        cd.add(helper.check("", "").subscribe(aFalse));
    }

    @Test
    public void checkOne() throws InterruptedException {
        checkHasOne(false, descr, url);
        helper.add(descr, url);
        checkHasOne(true, descr, url);
        TimeUnit.MILLISECONDS.sleep(100);
        helper.delete(descr, url);
        TimeUnit.MILLISECONDS.sleep(100);
        checkHasOne(false, descr, url);
    }

    @Test
    public void checkMany() throws InterruptedException {
        for (int i = 0; i < countN; i++)
            helper.add(Integer.toString(i), Integer.toString(i * i));
        for (int i = 0; i < countN; i++)
            checkHasOne(true, Integer.toString(i), Integer.toString(i * i));
        for (int i = 0; i < countN; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            helper.delete(Integer.toString(i), Integer.toString(i * i));
            TimeUnit.MILLISECONDS.sleep(100);
            for (int j = 0; j <= i; j++)
                checkHasOne(false, Integer.toString(j), Integer.toString(j * j));
            for (int j = i + 1; j < countN; j++)
                checkHasOne(true, Integer.toString(j), Integer.toString(j * j));
        }
    }

    @Test
    public void getData() throws InterruptedException {
        Log.d(TEST_LOG_TAG, "getData");
        for (int i = 0; i < countN; i++)
            helper.add(Integer.toString(i), Integer.toString(i + countN));
        cd.add(helper.getData().subscribe(new Consumer<ImageData[]>() {
            @Override
            public void accept(ImageData[] imageData) {
                for (int i = 0; i < countN; i++)
                    cd.add(helper.check(Integer.toString(i), Integer.toString(i + countN)).subscribe(aTrue));
            }
        }));
        for (int i = 0; i < countN; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            helper.delete(Integer.toString(i), Integer.toString(i + countN));
            TimeUnit.MILLISECONDS.sleep(100);
            final int ind = i;
            cd.add(helper.getData().subscribe(new Consumer<ImageData[]>() {
                @Override
                public void accept(ImageData[] imageData) throws InterruptedException {
                    for (int j = 0; j <= ind; j++)
                        cd.add(helper.check(Integer.toString(j), Integer.toString(j + countN)).subscribe(aFalse));
                    for (int j = ind + 1; j < countN; j++)
                        cd.add(helper.check(Integer.toString(j), Integer.toString(j + countN)).subscribe(aTrue));
                }
            }));
        }
    }
}