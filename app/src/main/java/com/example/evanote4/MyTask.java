package com.example.evanote4;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

class MyTask extends AsyncTask<String, Void, Elements>
{
    Document doc;
    Elements words;
    String what1 = "failed";

    static public MySubTask[] st = new MySubTask[10];

    static public ArrayList<String> menu = new ArrayList<String>();
    static public ArrayList<String> text_list = new ArrayList<String>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        for(int i = 0; i < 10; i++)
        {
            st[i] = new MySubTask();
        }

    }
    // MyTask
    protected synchronized Elements doInBackground(String... params) {
        // TimeUnit.SECONDS.sleep(2);
        String url = params[0];
        try {
            doc = Jsoup.connect(url).get();
            words = doc.select("td > a[href]");
            what1 = words.text();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return words;
    }

    // MyTask
    protected void onPostExecute(Elements result) {
        super.onPostExecute(result);
        ListIterator<Element> postIt = result.listIterator();

        for (int i=0; i<10; i++)
        {
            if(postIt.hasNext())
            {
                menu.add(postIt.next().text() + "\n");

                String content = words.get(i).attr("abs:href");

                st[i].execute(content);
            }
        }

        try {
            while (true)
            {
                Thread.sleep(100);
                if(MySubTask.ref_count == 10)
                    break;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        String title = MyTask.menu.get(0)+"\n";
        String sub = MyTask.text_list.get(0)+"\n";
        int no = (MainActivity.pagenum-1)*10+MainActivity.ind+1;
        MainActivity.tvInfo.setText(title+sub+no);
        MainActivity.btn_lock = false;
    }
}