package com.munk.quicknote.persistance;

import com.munk.quicknote.models.NoteItem;
import com.oracle.javafx.jmx.json.JSONWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmayank on 4/5/16.
 */
public class FileWriter {
    DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String filePath = "/home/kmayank/QuickNote.qnt";
    public List<NoteItem> readFile(){
        BufferedReader br;
        List<NoteItem> itemList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            while(line!=null){
                String [] itemArray = line.split("~~");
                itemList.add(new NoteItem(df2.parse(itemArray[0]),itemArray[1]));
                line=br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
    
}
