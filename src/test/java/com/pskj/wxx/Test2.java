package com.pskj.wxx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.service.StudentService;

public class Test2 {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		//����ʽ����
		ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-mybatis.xml");//��·���µ��ļ�
		//ApplicationContext cxt = new FileSystemXmlApplicationContext();//��ȡWEB-INF�е��ļ�
		StudentService studentService = (StudentService) cxt.getBean("studentServiceImpl");
		List<Student> stuList = studentService.findStudentList();
		for (Student student : stuList) {
			System.out.println(student);
		}
		
	}

}
