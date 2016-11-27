package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-08.
 * CARDMEMBER 테이블 각각의  column을 관리하는 객체를 만들기 위한 파일
 */
public class Cardmember {
    public int _id;
    public String p_name;
    public String c_name;
    public String phone;
    public String email;
    public String fax;
    public String position;
    public String op_name;
    public String ophone;

    public int get_id() {
        return _id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getC_name() {
        return c_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getPosition() {
        return position;
    }

    public String getOp_name() {
        return op_name;
    }

    public String getOphone() {
        return ophone;
    }

    public Cardmember(int _id, String p_name, String c_name, String phone, String email, String fax, String position, String op_name, String ophone) {
        this._id = _id;
        this.p_name = p_name;
        this.c_name = c_name;
        this.phone = phone;
        this.email = email;
        this.fax = fax;
        this.position = position;
        this.op_name = op_name;
        this.ophone = ophone;
    }

}
