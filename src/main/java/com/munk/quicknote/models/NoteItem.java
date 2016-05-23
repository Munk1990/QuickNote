package com.munk.quicknote.models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created by kmayank on 3/27/16.
 */
public class NoteItem implements Comparable<NoteItem> {
    @JsonProperty
    private Date creationDate;
    @JsonProperty
    private String noteContent;
    @JsonProperty
    private String noteType;

    public NoteItem(){}
    public NoteItem(Date creationDate, String content, String noteType){
        this.creationDate=creationDate;
        this.noteContent=content;
        this.noteType=noteType;
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

    public String getNoteType(){
        return noteType;
    }
    public void setNoteType(String noteType){
        this.noteType=noteType;
    }



    @Override
    public int compareTo(NoteItem o) {
        return creationDate.compareTo(o.getCreationDate());
    }

    public boolean contains(String text){
        return noteContent.toLowerCase().contains(text.toLowerCase());
    }

    @JsonIgnore
    public String getOneLinePreview(){
        return noteContent.replace("[\n\r]","");
    }

    @Override
    public String toString() {
        return noteContent;
    }
}
