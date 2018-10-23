package com.pskj.wxx.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.mapper.StudentMapper;
import com.pskj.wxx.admin.service.StudentService;

@Service("studentJDBCTemplate")
public class StudentJDBCTemplate implements StudentService {
	
	@Resource
	private StudentMapper studentMapper;
	
	private PlatformTransactionManager transactionManager;
	/*private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }*/

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    } 

	@Override
	public Student getStudentById(int id) {
		
		return null;
	}

	@Override
	public List<Student> findStudentList() {
		return null;
	}

	@Override
	public void addStudent(Student stu) {
		TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        studentMapper.addStudent(stu);
		transactionManager.commit(status);
		transactionManager.rollback(status);
		
	}

	@Override
	public void updateStudent(Student stu) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			studentMapper.updateStudent(stu);
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

	@Override
	public void delStudent(Student stu) {
		// TODO Auto-generated method stub
		
	}

}
