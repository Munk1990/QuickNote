package com.munk.quicknote.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.text.View;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by kmayank on 4/5/16.
 */
public class FileWriter {
    static ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory().enable(org.codehaus.jackson.JsonParser.Feature.ALLOW_COMMENTS));
    public void printJsonOutput(ViewList list){
        try {
            String serializedval = jsonMapper.writeValueAsString(list);
            ViewList deslist = jsonMapper.readValue(serializedval, ViewList.class);
            System.out.println(jsonMapper.writeValueAsString(deslist));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ViewList getViewListFromFile(String filePath) throws IOException {
        File saveFile = new File(filePath);
        if (!saveFile.exists()) {
            return new ViewList();
        }
        System.out.println("Attempting to read file: " + filePath);
        String filecontent = new String(Files.readAllBytes(saveFile.toPath()), UTF_8);
        System.out.println("File content found to be:" + filecontent);
        try {
            return jsonMapper.readValue(filecontent, ViewList.class);
        } catch (EOFException e) {
            e.printStackTrace();
            return new ViewList();
        }
    }

    public static boolean saveViewListToFile(String filePath, ViewList viewList) throws IOException {
        File saveFile = new File(filePath);
        if (!saveFile.exists())
        {
            System.out.println("File doesn't exist, creating new file");
            saveFile.createNewFile();
        }
        jsonMapper.writeValue(new File(filePath), viewList);
        return true;
    }

    
}
