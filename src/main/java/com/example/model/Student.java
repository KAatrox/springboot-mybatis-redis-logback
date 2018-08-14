package com.example.model;

public class Student {
private int sid;
private String sname;
private String sex;
public int getSid() {
	return sid;
}
public void setSid(int sid) {
	this.sid = sid;
}
public String getSname() {
	return sname;
}
public void setSname(String sname) {
	this.sname = sname;
}
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
@Override
public String toString() {
	return "Student [sid=" + sid + ", sname=" + sname + ", sex=" + sex + "]";
}


}
