package com.example.ipfs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import io.textile.ipfslite.Peer;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    String createRepo(Boolean reset) throws Exception {
        Context ctx = getApplicationContext();

        final File filesDir = ctx.getFilesDir();
        final String path = new File(filesDir, "ipfslite").getAbsolutePath();
        // Wipe repo
        File repo = new File(path);
        if (repo.exists() && reset == true) {
            FileUtils.deleteDirectory(repo);
        }
        System.out.println("Path is :"+path);
        return path;
    }

    public void SavePhotoTask(byte [] jpeg){
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Life Lapse");
        imagesFolder.mkdirs();

        final File photo= new File(imagesFolder, "desktopWallpaper.jpg");
        try
        {
            FileOutputStream fos=new FileOutputStream(photo.getPath());
            fos.write(jpeg);
            fos.close();
        }
        catch(Exception e)
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Ipfs testing");
        Boolean debug = false;
        Peer litePeer ;
        try {
            litePeer = new Peer(createRepo(true), debug, true);
            litePeer.start();
            byte[] data = litePeer.getFileSync("bafybeic35nent64fowmiohupnwnkfm2uxh6vpnyjlt3selcodjipfrokgi"); //hello world
            //byte[] data = litePeer.getFileSync("QmSgvgwxZGaBLqkGyWemEDqikCqU52XxsYLKtdy3vGZ8uq");
            System.out.println("=============================");
            System.out.println(new String(data));

            byte[] image = litePeer.getFileSync("QmSgvgwxZGaBLqkGyWemEDqikCqU52XxsYLKtdy3vGZ8uq");
            System.out.println("++++++++++++++++++++++++++++++");
            //System.out.println(new String(image));
            SavePhotoTask(image);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}