package com.munk.quicknote.models;

import com.sun.tools.javap.TypeAnnotationWriter;

import java.util.Date;

/**
 * Created by kmayank on 3/27/16.
 */
public class NoteItem implements Comparable<NoteItem> {
    private Date creationDate;
    private String noteContent;

    public NoteItem(Date creationDate, String content){
        this.creationDate=creationDate;
        this.noteContent=content;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setCreationDate(Date date){
        this.creationDate=date;
    }
    public Date getCreationDate(){
        return creationDate;
    }



    @Override
    public int compareTo(NoteItem o) {
        return creationDate.compareTo(o.getCreationDate());
    }

    public boolean contains(String text){
        return noteContent.toLowerCase().contains(text.toLowerCase());
    }

    public String getOneLinePreview(){
        return noteContent.replace("[\n\r]","");
    }

    @Override
    public String toString() {
        return noteContent;
    }
}
