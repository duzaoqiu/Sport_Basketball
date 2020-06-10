package com.android.library;

import android.content.Context;

import com.android.library.tools.db.Book;
import com.android.library.tools.db.MyObjectBox;
import com.android.library.tools.db.UserData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        // assertEquals("com.android.library.test", appContext.getPackageName());
        BoxStore build = MyObjectBox.builder().androidContext(this).build();
        Box<UserData> userDataBox = build.boxFor(UserData.class);
        UserData userData = new UserData();
        userData.setAge(23);
        userData.setId(123333);
        userData.setName("张三");
        List<Book> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book b = new Book();
            b.setAuthor("罗曼" + i);
            b.setId(1111 + i);
            b.setName("名人传" + i);
            list.add(b);
        }
        userDataBox.put(userData);

        Query<UserData> query = userDataBox.query().build();
        List<UserData> findList = query.find();
        for (int i = 0; i < findList.size(); i++) {
            UserData temp = findList.get(i);
            System.out.println(temp.getName());
        }
    }
}
