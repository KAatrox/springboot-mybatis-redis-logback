package com.example.controller;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.lettuce.RedisCli;
import com.example.mapper.StudentMapper;
import com.example.model.Student;



@RestController
public class StudentController {
	
	@Autowired
	StudentMapper studentMapper;
	
	Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	@GetMapping("/student/sel/{id}")
	public Student getOneStudent(@PathVariable int id){
		Student s = new Student();
		s = studentMapper.findOneStudent(id);
		logger.debug("--------till-----------");		
		return s;
	}
	
	@GetMapping("/student/all")
	public List<Student> getAllStudent(){
		List<Student> list  = null;
		list = studentMapper.findAllStudent();
		logger.debug("--------till-----------");
		return list;
		
	}
	
	@GetMapping("/Students")
	public List<Student> getAllStudents() throws InterruptedException, ExecutionException{
		List<Student> Students = null;
		Students = readFromRedis();
		if(Students.size()==0){
			logger.debug("--------read from db-----------");
			Students =  studentMapper.findAllStudent();
			writeToRedis(Students);
		}
		return Students;
	}
	
	private List<Student> readFromRedis() throws InterruptedException, ExecutionException {
		RedisAsyncCommands<String, String> asyncCommands = RedisCli.connection.async();
		final List<Student> Students = new ArrayList();
		RedisFuture<List<String>> futureKeys = asyncCommands.keys("Student*");
		List<String> keys = futureKeys.get();
		if(keys.size()==0) return Students;
		
		for(String key: keys){
			RedisFuture<Map<String, String>> futureMap = asyncCommands.hgetall(key);
			Map<String, String> map = futureMap.get();		
			Student Student = new Student();
			Student.setSid( Integer.valueOf(map.get("id")) );
			Student.setSname( map.get("name") );
			Student.setSex( map.get("sex") );
			Students.add(Student);
		}
		logger.debug("----------read from redis-------------------");
		return Students;
	}
	
	private void writeToRedis(List<Student> Students) {
		RedisAsyncCommands<String, String> asyncCommands = RedisCli.connection.async();		
		for(Student auth: Students){
			Map<String, String> map = new HashMap();
			map.put("id", String.valueOf(auth.getSid()));
			map.put("name", auth.getSname());
			map.put("sex", auth.getSex());
			asyncCommands.hmset("Student:"+auth.getSid(), map);
		}
		
		
	}


	@DeleteMapping("/student/del/{id}")
	public int getDelStudent(@PathVariable int id){
		int i = 0;
		i=studentMapper.delStudent(id);
		logger.debug("--------till-----------");
		return i;
		
	}
	
	@PostMapping(value="/student/add", consumes="application/json")
	public int getDelStudent(@RequestBody Student student){
		return studentMapper.addStudent(student);
		
	}
	

}
