package cn.app.model;

import lib.mvp.IModel;

/**
 * Bean基类 提取接口共用字段。
 * 此类的编写请按照javaBean规范. 
 * @author 景密
 *
 */
@SuppressWarnings("serial")
public class BaseBean implements IModel{
	

	private int code;//错误码
	private String msg;
	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BaseBean [code=" + code + "]";
	}
	
	
	
}