package com.pskj.wxx.logging.service;

import java.util.List;

import com.pskj.wxx.logging.beans.SysLog;

public interface LoggingService {
	
	/*
	 * ��־�б�
	 */
	public List<SysLog> findLoggingList();
	
	/*
	 * �����־
	 */
	public void addLogging(SysLog log);

}
