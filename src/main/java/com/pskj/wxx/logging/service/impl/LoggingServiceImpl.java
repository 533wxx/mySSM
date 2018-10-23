package com.pskj.wxx.logging.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pskj.wxx.logging.beans.SysLog;
import com.pskj.wxx.logging.mapper.LoggingMapper;
import com.pskj.wxx.logging.service.LoggingService;

@Service
public class LoggingServiceImpl implements LoggingService {
	
	@Resource
	private LoggingMapper loggingMapper;

	@Override
	@Transactional
	public List<SysLog> findLoggingList() {
		return loggingMapper.findLoggingList();
	}

	@Override
	@Transactional
	public void addLogging(SysLog log) {
		loggingMapper.addLogging(log);
	}
	
	

}
