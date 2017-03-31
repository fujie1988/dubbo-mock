package com.lianjia.link.mock.dubbo;

public enum EnumHardMethod {
	getExtByPIdAndidAppIdAndType("getExtByPIdAndidAppIdAndType","根据权限ID，角色ID，appid和类型查询约束")

    ;
    private String value;
	private String desc;

	private EnumHardMethod(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
