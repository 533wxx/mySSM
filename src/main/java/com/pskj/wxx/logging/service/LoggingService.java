package com.pskj.wxx.logging.service;

import java.util.List;

import com.pskj.wxx.logging.beans.SysLog;

public interface LoggingService {
	
	/*
	 * 日志列表
	 */
	public List<SysLog> findLoggingList();
	
	/*
	 * 添加日志
	 */
	public void addLogging(SysLog log);

}
