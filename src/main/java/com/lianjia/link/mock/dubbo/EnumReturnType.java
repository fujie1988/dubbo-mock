package com.lianjia.link.mock.dubbo;

public enum EnumReturnType {
	getEhrUserBasicInfoByEntryDate("getEhrUserBasicInfoByEntryDate","根据入职时间查人员基本信息"),
	getEhrUserByUserIds("getEhrUserByUserIds","批量获取用户信息"),
	getEhrUserByUserId("getEhrUserByUserId", "获取用户信息"), 
	getEhrUserPage("getEhrUserPage", "获取用户信息列表"), 
	getEhrOrgByOrgCode("getEhrOrgByOrgCode", "获取组织信息"), 	
	queryEventByTimeAndEvent("queryEventByTimeAndEvent", "获取所有人UC事件表"),
	getPermissionRoleByUcidAndAppId("getPermissionRoleByUcidAndAppId", "权限点获取"),
	getHasRoleInfoByUcidList("getHasRoleInfoByUcidList", "红星获取是否有角色人信息"),
	determineExistValidHouseDelegate("determineExistValidHouseDelegate","电话号码是否有委托"),
	isBookOrder("isBookOrder", "是否存在带看订单"),
	isValidCustomerPhone("isValidCustomerPhone", "电话在这个城市中是否有有效客源"),
	getPermissionByUcidAndAppId("getPermissionByUcidAndAppId", "根据用户的ucid和当前系统的appid查询该用户在当前系统有哪些权限点"),
	getPermissionByExtIdAndAppId("getPermissionByExtIdAndAppId", "根据extid和当前系统的appid获取需要的权限点"),
	getExtByPIdAndAppId("getExtByPIdAndAppId", "根据权限id和当前系统的appid查询约束"),
	countAuthExtByPIdAndAppId("countAuthExtByPIdAndAppId", "查询当前系统下某权限拥有的约束数量"),
	getExtByPIdAndAppIdAndType("getExtByPIdAndAppIdAndType", "根据权限id 当前系统appid和约束类型查询约束"),
	countAuthExtByPIdAndAppIdAndType("countAuthExtByPIdAndAppIdAndType", "根据权限id 当前系统appid和约束类型查询约束数量"),
	getRoleByUcidAndAppId("getRoleByUcidAndAppId", "根据ucid和appid查询拥有的角色"),
	getExtByPIdAndRidAppId("getExtByPIdAndRidAppId", "查询该用户某个角色拥有的全部约束"),
	getExtByPIdAndidAppIdAndType("getExtByPIdAndidAppIdAndType", "根据权限ID,角色ID,appid和类型查询约束"),
	getPermissionRolePositionByPermissionAndPosition("getPermissionRolePositionByPermissionAndPosition", "根据权限id，职位，职级，还有appId找扩展角色"),
	getUserRolePermissionByUcidAndAppId("getUserRolePermissionByUcidAndAppId", "根据角色ID，appid查询约束"),
	getVirtualRoleByUcid("getVirtualRoleByUcid", "根据用户ID获取该用户的所有虚拟角色"),
	getVirtualRoleScopeMappingByUcid("getVirtualRoleScopeMappingByUcid", "根据用户ID获取该用户的所有虚拟角色和范围的映射对象"),
	getRoleByUcidAndVirtualRole("getRoleByUcidAndVirtualRole", "据用户ID,虚拟角色ID和appId获取该用户在该系统下属于该虚拟角色的角色"),
	getDaikeUcid("getDaikeUcid", "获取被替代人的ucid"),
	getUcidByPermissionAndAppId("getUcidByPermissionAndAppId", "查询某系统下拥有某权限点的所有的人"),
	getUcidByRoleAndAppId("getUcidByRoleAndAppId", "查询某系统下拥有某角色的所有的人"),
	searchRole("searchRole", "按照一些条件查询角色列表"),
	getVirtualRoleById("getVirtualRoleById", "根据ID查询虚拟角色"),
	getAuthUserRoleByUcidAndAppId("getAuthUserRoleByUcidAndAppId", "查询某人在某系统下 人-角色 的对应关系，从而获知角色的获取来源"),
	getAuthPositionRoleByUcidAndRoleId("getAuthPositionRoleByUcidAndRoleId", ""),
	getAuthPositionRoleByUcidAndVrIdAndAppId("getAuthPositionRoleByUcidAndVrIdAndAppId", ""),
	getUserPositionDetail("getUserPositionDetail", ""),
	getUserRole("getUserRole", ""),
	
	searchUserByPermission("searchUserByPermission", ""),
	searchUserByRole("searchUserByRole", ""),
	getUserAuthExtInfo("getUserAuthExtInfo", ""),

    ;
    private String value;
	private String desc;

	private EnumReturnType(String value, String desc) {
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
