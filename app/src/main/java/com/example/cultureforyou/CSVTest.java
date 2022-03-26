package com.example.cultureforyou;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.Nullable;

public class CSVTest extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csvtest);

        /*
        new Thread(() -> {
            // 네트워크 동작 -> 인터넷에서 받아오는 코드
            try {
                URL stockURL = new URL("https://drive.google.com/file/d/1ojxLDmtKm6Xbp6sX6z2A7_D9nAvkUdDP/view?usp=sharing");
                BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
                CSVReader reader = new CSVReader(in);
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    // nextLine[] is an array of values from the line
                    Log.d("CSVTEST_2", nextLine[0]);
                    //System.out.println(nextLine[0] + nextLine[1] + "etc...");
                }
            } catch (IOException e) {
                e.printStackTrace();}
        }).start();

         */

// Java code to illustrate reading a
// CSV file line by line


    }



}


