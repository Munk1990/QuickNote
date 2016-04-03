package com.munk.quicknote.loader;

import com.munk.quicknote.listener.ClipboardListener;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;
import com.munk.quicknote.ui.QuickNoteFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kmayank on 3/27/16.
 */
public class Main {
    public static ViewList viewList;
    public static void main(String [] Args){
        QuickNoteFrame mainFrame = new QuickNoteFrame();
        List<NoteItem> itemList = new ArrayList<NoteItem>();
        Thread t = new Thread(new ClipboardListener());
        t.start();
        mainFrame.setVisible(true);

        viewList = new ViewList();

    }

    public static boolean addToList(String element){
        if (!viewList.hasItemWithString(element)) {
            NoteItem item = new NoteItem(new Date(), element);
            QuickNoteFrame.listModel.add(0, item);
            viewList.add(item);
            return true;
        }
        return false;
    }

    public static boolean removeFromList (String element){
        return QuickNoteFrame.listModel.removeElement(element);
    }

    public static boolean filterListFromString(String filterString){
        QuickNoteFrame.listModel.removeAllElements();

        for(NoteItem item:viewList.getFilteredList(filterString)){
            QuickNoteFrame.listModel.add(0, item);
        }
        return true;
    }

}
