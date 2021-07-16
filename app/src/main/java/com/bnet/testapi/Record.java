package com.bnet.testapi;

public class Record {

    private String mId;
    private String mBody;
    private String mDa;
    private String mDm;

    public Record(String id, String body, String da, String dm){
        mId = id;
        mBody = body;
        mDa = da;
        mDm = dm;
    }

    public String getDm() {
        return mDm;
    }

    public void setDm(String mDm) {
        this.mDm = mDm;
    }

    public String getDa() {
        return mDa;
    }

    public void setDa(String mDa) {
        this.mDa = mDa;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
