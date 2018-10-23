package com.pskj.wxx.logging.beans;

import java.util.Date;

public class SysLog {

	private int id;
	private String operation;
	private String handler;
	private Date createDate;
	private String modelName;

	public SysLog() {
		super();
	}

	public SysLog(int id, String operation, String handler, Date createDate, String modelName) {
		super();
		this.id = id;
		this.operation = operation;
		this.handler = handler;
		this.createDate = createDate;
		this.modelName = modelName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
