package com.adel.dictionary;



import com.orm.SugarRecord;

public class DBDictionary extends SugarRecord {
    private String word;
    private String date;




    public DBDictionary(String word, String date){
        this.word = word;
        this.date = date;

    }

    public String getword() {
        return word;
    }

    public void setword(String word) {
        this.word = word;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }


}

