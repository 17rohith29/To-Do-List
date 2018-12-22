package com.example.home.todolist;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ListActivity extends Activity {

    ArrayList<String> allInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_list);
        allInfo = new ArrayList<>();

        // creating the file if it does not exist
        try {
            PrintStream out = new PrintStream(openFileOutput("info.txt", MODE_APPEND));
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }

        // getting all data from file and storing it in allInfo
        try {
            Scanner scan = new Scanner(openFileInput("info.txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                allInfo.add(line);
            }

            scan.close();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }

        display();

        // removing items when long clicked
        ListView list = (ListView) findViewById(R.id.items);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                allInfo.remove(i);
                // put all info back onto the file
                try {
                    PrintStream out = new PrintStream(openFileOutput("info.txt", MODE_PRIVATE));

                    for(String s : allInfo) {
                        out.println(s);
                    }

                    out.close();
                } catch (FileNotFoundException e) {
                    Log.e("Error: ", "Wrong File");
                }

                display();

                return true;
            }
        });
    }

    public void addNewItem (View view) {
        // adds item to the file
        try {
            PrintStream out = new PrintStream(openFileOutput("info.txt", MODE_APPEND));
            // getting the string
            String toAdd = ((EditText) findViewById(R.id.add)).getText().toString();

            // only add if there is a relevent message
            if (toAdd.equals(""))
                Toast.makeText(this, "Please Enter a valid item", Toast.LENGTH_SHORT).show();
            else if (allInfo.contains(toAdd))
                Toast.makeText(this, "Already Available", Toast.LENGTH_SHORT).show();
            else {
                out.append(toAdd + "\n");
                allInfo.add(toAdd);
            }

            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }

        // calls display after the new item has been added
        display();

        // remove edittext
        ((EditText) findViewById(R.id.add)).setText("");
    }

    public void display() {
        // adding the arrayList to the ListView
        ArrayList<String> info = new ArrayList<>(); // new ArrayList with Numbering
        int pos = 1;

        for(String s : allInfo)
            info.add(pos++ + ". " + s);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, info);

        ListView list = (ListView) findViewById(R.id.items);
        list.setAdapter(adapter);
    }

    public void eraseAllItems (View view) {
        try {
            PrintStream out = new PrintStream(openFileOutput("info.txt", MODE_PRIVATE));
            allInfo.clear();
            out.close();

            display();
        } catch (FileNotFoundException e) {
            Log.e("Error: ", "Wrong File");
        }
    }

}
