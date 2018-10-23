package com.pskj.wxx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.service.StudentService;

public class Test2 {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		//声明式事务
		ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-mybatis.xml");//类路径下的文件
		//ApplicationContext cxt = new FileSystemXmlApplicationContext();//读取WEB-INF中的文件
		StudentService studentService = (StudentService) cxt.getBean("studentServiceImpl");
		List<Student> stuList = studentService.findStudentList();
		for (Student student : stuList) {
			System.out.println(student);
		}
		
	}

}
