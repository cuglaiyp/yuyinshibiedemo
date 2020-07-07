package org.example;

import org.example.service.YuYin;
import org.example.view.MyFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class Main {
    public static Future<?> task;
    public static ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        //String s = new String("  asd, sda\n 。  ad。sds  asda\n sda           adsd。ad");
        //String text = s.replaceAll("[ *\n*\r*]", "");
        //StringBuilder sb = new StringBuilder(text);
        //for (int i = 0; i < sb.length(); i++){
        //    if(sb.charAt(i) == '。'){
        //        sb.insert(i+1, '\n');
        //    }
        //}
        //System.out.println(sb.toString());

        task = pool.submit(() -> new ArrayList<>(Arrays.asList(YuYin.INSTANCE.listHotWords())));
        new MyFrame();

		
    }

}









