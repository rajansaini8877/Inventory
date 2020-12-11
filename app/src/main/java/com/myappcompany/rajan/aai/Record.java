package com.myappcompany.rajan.aai;

public class Record {

    private String mPart;
    private String mDate;
    private String mName;
    private String mContact;

    public Record(String date, String part, String name, String contact) {
        mPart = part;
        mDate = date;
        mName = name;
        mContact = contact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPart(String part) {
        mPart = part;
    }

    public String getName() {
        return mName;
    }

    public String getContact() {
        return mContact;
    }

    public String getDate() {
        StringBuilder sb = new StringBuilder(mDate);
        sb.insert(2, '/');
        sb.insert(5, '/');
        return sb.toString();
    }

    public String getPart() {
        return mPart;
    }
}
