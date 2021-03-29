package com.example.ipfs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

//    public boolean isExternalStorageWritable(){
//        String state= Environment.getExternalStorageState();
//        if(Environment.MEDIA_MOUNTED.equals(state)){
//            return true;
//        }
//        return false;
//    }

//    public void SavePhotoTask(byte [] jpeg){
//        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "testipfs");
//        File photo= new File(imagesFolder, "desktopWallpaper.jpg");
//        System.out.println("Saving file in "+photo.getAbsolutePath());
//        if (!photo.exists()) {
//            try {
//                photo.createNewFile();
//            } catch (IOException e) {
//                System.out.println("Failed to create photo");
//                e.printStackTrace();
//            }
//        }
//        try
//        {
//            FileOutputStream fos=new FileOutputStream(photo.getPath());
//            fos.write(jpeg);
//            fos.close();
//        }
//        catch(Exception e)
//        {
//            System.out.println("------ error saving file" + e);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Ipfs testing");
        Boolean debug = false;
        Peer litePeer ;
        try {

            //start peer
            litePeer = new Peer(createRepo(true), debug, true);
            litePeer.start();


             //send data
            String message = "Hello World from android demo";
            String cid = litePeer.addFileSync(message.getBytes());
            System.out.println("Data uploaded, CID:" + cid);


            //send file
            Drawable drawable= getResources().getDrawable(R.drawable.ima);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] buffer= out.toByteArray();
            System.out.println("opened file, lenght" + new String(buffer).length());
            String cidImage = litePeer.addFileSync(buffer);
            System.out.println("Image uploaded, CID: " + cidImage);



            //get hello world file
            byte[] data = litePeer.getFileSync("bafybeic35nent64fowmiohupnwnkfm2uxh6vpnyjlt3selcodjipfrokgi"); //hello world
            System.out.println("Fetched data:" + new String(data));



            // get and save this pinned image https://ipfs.io/ipfs/QmSgvgwxZGaBLqkGyWemEDqikCqU52XxsYLKtdy3vGZ8uq
            byte[] image = litePeer.getFileSync("QmSgvgwxZGaBLqkGyWemEDqikCqU52XxsYLKtdy3vGZ8uq");
            System.out.println("Fetched image as byte array, length:" + image.length);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            ImageView im = (ImageView) findViewById(R.id.imageView);
            im.setImageBitmap(Bitmap.createScaledBitmap(bmp, 4000, 3000, false));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}