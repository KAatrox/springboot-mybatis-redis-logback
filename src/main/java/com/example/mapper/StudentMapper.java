package com.example.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.model.*;
public interface StudentMapper {
//	按学号查找学生
	Student findOneStudent(int id);
//	查找全部学生
	List<Student> findAllStudent();
//	添加学生信息
	int addStudent(Student student);
//	按学号删除学生信息
	int delStudent(int sid);
	
}
