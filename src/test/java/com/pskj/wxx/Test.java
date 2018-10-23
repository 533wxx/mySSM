package com.pskj.wxx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.service.StudentService;

public class Test {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		//声明式事务
		ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-mybatis.xml");//类路径下的文件
		//ApplicationContext cxt = new FileSystemXmlApplicationContext();//读取WEB-INF中的文件
		StudentService studentService = (StudentService) cxt.getBean("studentServiceImpl");
		Student stu = new Student("w",1321,56.6);
		stu.setId(1);
		studentService.updateStudent(stu);
	}

}
