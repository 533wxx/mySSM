package com.pskj.wxx.admin.service;

import java.util.List;

import com.pskj.wxx.admin.beans.Student;

public interface StudentService {

	public Student getStudentById(int id);

	public List<Student> findStudentList();

	public void addStudent(Student stu);

	public void updateStudent(Student stu);

	public void delStudent(Student stu);

}
