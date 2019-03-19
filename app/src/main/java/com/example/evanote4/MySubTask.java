package com.example.evanote4;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ListIterator;

class MySubTask extends AsyncTask<String, Void, Elements>
{
    Document doc;
    Elements panel;
    String what1 = "failed";
    public static int ref_count = 0;

    // SubTask
    protected Elements doInBackground(String... params)
    {
        // TimeUnit.SECONDS.sleep(2);
        String url = params[0];
        try {
            doc = Jsoup.connect(url).get();
            panel = doc.select("div.row > div.large-12 > div.panel > p");
            what1 = panel.text();
            MyTask.text_list.add(what1 + "\n\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ref_count++;

        return panel;
    }
    // SubTask
    protected void onPostExecute(Elements result) {
        super.onPostExecute(result);

//        MainActivity.text_list.get(MainActivity.ind);
//        MainActivity.tvInfo.setText(MainActivity.menu.get(0));

//        text += result + "\n";
//        MainActivity.menu += text + "\n";
    }
}