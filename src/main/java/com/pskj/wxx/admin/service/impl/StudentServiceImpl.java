package com.pskj.wxx.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pskj.wxx.admin.beans.Student;
import com.pskj.wxx.admin.mapper.StudentMapper;
import com.pskj.wxx.admin.service.StudentService;
import com.pskj.wxx.common.annotation.Logging;

@Service
/*
 * ���ԣ�rollbackOn ��������ع����쳣����������
 * 	noRollbackFor :���ᵼ������ع����쳣������
 */
public class StudentServiceImpl implements StudentService {
	
	@Resource
	private StudentMapper studentMapper;

	@Override
	@Transactional
	@Logging(modelName = "ѧ��ģ��", operation = "��ѯ����")
	public Student getStudentById(int id) {
		return studentMapper.getStudentById(id);
	}

	@Override
	//@RedisCache(type = Student.class, expire = 200000000)
	public List<Student> findStudentList() {
		return studentMapper.findStudentList();
	}

	@Override
	public void addStudent(Student stu) {
		try {
			studentMapper.addStudent(stu);
			throw new ClassNotFoundException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

	@Override
	public void updateStudent(Student stu) {
		studentMapper.updateStudent(stu);
	}

	@Override
	public void delStudent(Student stu) {
		studentMapper.delStudent(stu);
	}

}
