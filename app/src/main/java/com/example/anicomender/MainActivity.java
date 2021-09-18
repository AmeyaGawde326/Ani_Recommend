package com.example.anicomender;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {
    //Declaring the back end
    AsyncHttpClient client;
    ProgressBar progressBar;
    Workbook workbook;
    List<String> titles,genre,imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Storing the url in a string
        String url = "https://github.com/AmeyaGawde326/Ani_Recommend/blob/main/AnimeList.xls?raw=true";

        //Declaring variables to the buttons and text
        TextView titleView = findViewById(R.id.titleView);
        TextView descView = findViewById(R.id.descView);
        Button Recommended = findViewById(R.id.buttonR);
        ImageView Cover = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        //arraylist to store the data
        titles = new ArrayList<>();
        genre = new ArrayList<>();
        imgUrl = new ArrayList<>();

        //using Asynchttp client for loading the data from url
        client = new AsyncHttpClient();
        progressBar.setVisibility(View.VISIBLE);
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Download of Data Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Database Downloaded", Toast.LENGTH_SHORT).show();
                    //Getting the data from excel and store in natively in a excel worksheet
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setGCDisabled(true);
                    if(file != null){
                        try {
                            workbook = workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(0);
                            //using for loop to store it in a array list we created at the start
                            for (int i = 0; i < sheet.getRows(); i++) {
                                Cell[] row = sheet.getRow(i);
                                titles.add(row[0].getContents());
                                genre.add(row[row.length-1].getContents());
                                imgUrl.add(row[1].getContents());
                                System.out.println(row.length);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }


                    }
            }
        });


        Recommended.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Generating a random number which is less than no of items in array list than using that random number as index to call a random entry
                Random random = new Random();
                Sheet sheet_no = workbook.getSheet(0);
                int index = random.nextInt(sheet_no.getRows());
                //getting the values of the no of row it generated
                String Title, Genres, ImageUrl;
                Title = titles.get(index);
                Genres = genre.get(index);
                ImageUrl =imgUrl.get(index);
                titleView.setText(Title);
                descView.setText(Genres);
                //Using Picasso to download the image
                Picasso.get().load(ImageUrl).into(Cover);

            }
        });

    }


}