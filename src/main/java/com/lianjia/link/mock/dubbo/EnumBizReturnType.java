package com.lianjia.link.mock.dubbo;

import com.lianjia.link.mock.dubbo.EnumReturnType;

public enum EnumBizReturnType {
	JSON(1,"对象",new EnumReturnType[]{	EnumReturnType.getEhrOrgByOrgCode,
										EnumReturnType.getEhrUserByUserId,
										EnumReturnType.getUserRolePermissionByUcidAndAppId,
										EnumReturnType.getVirtualRoleById}),
										
	LIST(2, "对象List", new EnumReturnType[]{	EnumReturnType.getEhrUserByUserIds, 
											EnumReturnType.getPermissionRoleByUcidAndAppId,
											EnumReturnType.getHasRoleInfoByUcidList,
											EnumReturnType.getPermissionByUcidAndAppId,
											EnumReturnType.getPermissionByExtIdAndAppId,
											EnumReturnType.getExtByPIdAndAppId,
											EnumReturnType.getExtByPIdAndAppIdAndType,
											EnumReturnType.getRoleByUcidAndAppId,
											EnumReturnType.getExtByPIdAndRidAppId,
											EnumReturnType.getExtByPIdAndidAppIdAndType,
											EnumReturnType.getPermissionRolePositionByPermissionAndPosition,
											EnumReturnType.getVirtualRoleByUcid,
											EnumReturnType.getVirtualRoleScopeMappingByUcid,
											EnumReturnType.getRoleByUcidAndVirtualRole,
											EnumReturnType.getUcidByPermissionAndAppId,
											EnumReturnType.getUcidByRoleAndAppId,
											EnumReturnType.searchRole,
											EnumReturnType.getAuthUserRoleByUcidAndAppId,
											EnumReturnType.getAuthPositionRoleByUcidAndRoleId,
											EnumReturnType.getAuthPositionRoleByUcidAndVrIdAndAppId,
											EnumReturnType.getUserPositionDetail,
											EnumReturnType.searchUserByPermission,
											EnumReturnType.searchUserByRole,
											EnumReturnType.getUserAuthExtInfo											
											} ),
													
	JSON_PAGE(3, "对象分页", new EnumReturnType[]{	EnumReturnType.getEhrUserPage,
												EnumReturnType.getEhrUserBasicInfoByEntryDate}),
													
	BOOLEAN(4, "布尔值",new EnumReturnType[]{	EnumReturnType.determineExistValidHouseDelegate,
											EnumReturnType.isValidCustomerPhone}),
	
	MAP(5, "字典", new EnumReturnType[]{ EnumReturnType.queryEventByTimeAndEvent,
										EnumReturnType.getUserRole} ), 
	
	INTEGER(6, "整型", new EnumReturnType[]{EnumReturnType.isBookOrder,
											EnumReturnType.countAuthExtByPIdAndAppId,
											EnumReturnType.countAuthExtByPIdAndAppIdAndType} )	,
	
	STRING(7, "字符串", new EnumReturnType[]{} ),
	
	LONG(8, "长整型", new EnumReturnType[]{EnumReturnType.getDaikeUcid} )
	
	;
	
    private int value;
	private String desc;
	private EnumReturnType[] types;

	private EnumBizReturnType(int value, String desc,EnumReturnType[] types) {
		this.value = value;
		this.desc = desc;
		this.types = types;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public EnumReturnType[] getTypes() {
		return types;
	}

	public void setTypes(EnumReturnType[] statuses) {
		this.types = types;
	}
	
}
