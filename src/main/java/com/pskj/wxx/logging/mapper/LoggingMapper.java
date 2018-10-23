package com.pskj.wxx.logging.mapper;

import java.util.List;

import com.pskj.wxx.logging.beans.SysLog;

public interface LoggingMapper {
	
	/*
	 * 日志列表
	 */
	public List<SysLog> findLoggingList();
	
	/*
	 * 添加日志
	 */
	public void addLogging(SysLog log);

}
