package com.pskj.wxx;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.service.StudentService;
import com.pskj.wxx.logging.beans.SysLog;
import com.pskj.wxx.logging.service.LoggingService;

public class Test1 {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		//���ʽ����
		ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-mybatis.xml");//��·���µ��ļ�
		//ApplicationContext cxt = new FileSystemXmlApplicationContext();//��ȡWEB-INF�е��ļ�
		StudentService studentService = (StudentService) cxt.getBean("studentServiceImpl");
		Student stu = studentService.getStudentById(1);
		System.out.println(stu);
		
		LoggingService loggingService = (LoggingService) cxt.getBean(LoggingService.class);
		List<SysLog >log = loggingService.findLoggingList();
		System.out.println(log);
	}

}
