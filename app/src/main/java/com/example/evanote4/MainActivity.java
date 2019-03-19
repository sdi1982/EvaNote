package com.example.evanote4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    MyTask mt;
    static public TextView tvInfo;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    static int pagenum = 1;
    static int ind = 0;

    static boolean btn_lock = false;

    String URL="";
//    String URL="http://en.wikipedia.org/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.note_text);
        tvInfo.setText("Please wait...");

        sp = getSharedPreferences("page", Context.MODE_PRIVATE);
        editor = sp.edit();

        pagenum = sp.getInt("PAGENUM",1);
        ind = sp.getInt("INDEX",0);

        mt = new MyTask();
        URL="http://m.rutc.tv/vod_list.php?pagenum="+pagenum+"&category=RA090101";
        mt.execute(URL);

        Button button1 = findViewById(R.id.btn_left);
        button1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public synchronized void  onClick(View view)
            {
                if(btn_lock == true)
                    return;
                btn_lock = true;

                editor.putInt("PAGENUM",pagenum);
                editor.putInt("INDEX",ind);
                editor.commit();

                if(ind > 0)
                {
                    ind -= 1;

                    String title = MyTask.menu.get(ind)+"\n";
                    String sub = MyTask.text_list.get(ind)+"\n";
                    int no = (pagenum-1)*10+ind+1;
                    tvInfo.setText(title+sub+no);
                    btn_lock = false;
                }
                else
                {
                    if(pagenum > 1)
                    {
                        pagenum -= 1;

                        tvInfo.setText("Please wait...");
                        MyTask.menu.clear();
                        MyTask.text_list.clear();
                        MySubTask.ref_count = 0;

                        ind = 9;
                        for(int i = 0; i < 10; i++)
                        {
                            MyTask.st[i] = new MySubTask();
                        }
                        mt = new MyTask();
                        URL="http://m.rutc.tv/vod_list.php?pagenum="+pagenum+"&category=RA090101";
                        mt.execute(URL);

//                        String title = MainActivity.menu.get(ind);
//                        String sub = MainActivity.text_list.get(ind);
//                        tvInfo.setText(title+sub);
                    }
                }
            }
        });

            Button button2 = findViewById(R.id.btn_right);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public synchronized void onClick(View view)
            {
                if(btn_lock == true)
                    return;
                btn_lock = true;

                editor.putInt("PAGENUM",pagenum);
                editor.putInt("INDEX",ind);
                editor.commit();

                if(ind < 9)
                {
                    ind += 1;

                    String title = MyTask.menu.get(ind)+"\n";
                    String sub = MyTask.text_list.get(ind)+"\n";
                    int no = (pagenum-1)*10+ind+1;
                    tvInfo.setText(title+sub+no);
                    btn_lock = false;
                }
                else
                {
                    pagenum += 1;

                    tvInfo.setText("Please wait...");
                    MyTask.menu.clear();
                    MyTask.text_list.clear();
                    MySubTask.ref_count = 0;

                    ind = 0;
                    for(int i = 0; i < 10; i++)
                    {
                        MyTask.st[i] = new MySubTask();
                    }

                    mt = new MyTask();
                    URL="http://m.rutc.tv/vod_list.php?pagenum="+pagenum+"&category=RA090101";
                    mt.execute(URL);

//                    String title = MainActivity.menu.get(ind);
//                    String sub = MainActivity.text_list.get(ind);
//                    tvInfo.setText(title+sub);
                }
            }
        });
    }
}
