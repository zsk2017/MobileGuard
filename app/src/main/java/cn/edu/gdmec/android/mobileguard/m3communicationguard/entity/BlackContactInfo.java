package cn.edu.gdmec.android.mobileguard.m3communicationguard.entity;

/**
 * Created by liuyuan on 2017/11/5.
 */

public class BlackContactInfo {
    public String phoneNumber;
    public String contactName;
    public String contactType;
    public int mode;
    public String type;
    public  String getModeString(int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话、短信拦截";
        }
         return  "";
    }
    public String getContactType(){
        return contactType;
    }
}
