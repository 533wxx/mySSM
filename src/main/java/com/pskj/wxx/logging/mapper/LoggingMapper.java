package com.pskj.wxx.logging.mapper;

import java.util.List;

import com.pskj.wxx.logging.beans.SysLog;

public interface LoggingMapper {
	
	/*
	 * ��־�б�
	 */
	public List<SysLog> findLoggingList();
	
	/*
	 * �����־
	 */
	public void addLogging(SysLog log);

}
