package com.lianjia.test;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lianjia.link.mock.dubbo.EnumHardMethod;
import com.lianjia.util.GsonUtil;
import com.lianjia.util.SpringContextUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationHandler;
import java.nio.charset.Charset;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.reflect.TypeToken;
import com.lianjia.house.dto.DTOHouseSearchIndexgenFull;

public class DubboServiceProxy implements InvocationHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(DubboServiceProxy.class);

	private String interfaceName;

	public DubboServiceProxy(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Object getInterfaceName(String interfaceName) {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;

		try {
			//抽离配置文件
			//InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/spring/config.properties");
			String path = System.getProperty("java.class.path");
			//int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
			//int lastIndex = path.lastIndexOf(File.separator) + 1;
			//InputStream inputStream = new BufferedInputStream(new FileInputStream(path.substring(firstIndex, lastIndex) + "/spring/config.properties"));

			InputStream inputStream = new BufferedInputStream(new FileInputStream(path.split(":")[0] + "/spring/config.properties"));
			LOGGER.info("the path is {}", path.split(":")[0] + "/spring/config.properties");

			Properties p = new Properties();
			try {
				p.load(inputStream);
			} catch (IOException e1) {
				System.out.println("cann't find config.properties!");
			}

			String isMock = p.getProperty("isMock");
			LOGGER.info("--before method " + interfaceName + '.' + method.getName() + "=====" + JSON.toJSONString(args));
			System.out.println("--before method " + interfaceName + '.' + method.getName() + "=====" + JSON.toJSONString(args));
			if ("0".equals(isMock)) {
				// result = method.invoke(proxy, args);
				result = realService(interfaceName, method.getName(), args);
			} else {
				result = dubboLocal(interfaceName, method.getName(), args);
			}
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw e;
		} finally {
			// System.out.println("--after method " + method.getName());
		}
		LOGGER.info("--after method " + interfaceName + '.' + method.getName() + "=====" + JSON.toJSONString(args) + "=========" + JSON.toJSONString(result, true));
		System.out.println("--after method " + interfaceName + '.' + method.getName() + "=====" + JSON.toJSONString(args) + "=========" + JSON.toJSONString(result, true));
		return result;
	}

	public Object realService(String interfaceName, String methodName, Object[] args) throws Exception {
		Class<?> clz = null;
		try {
			clz = Class.forName(interfaceName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String tmpStr = interfaceName.substring(interfaceName.lastIndexOf(".") + 1) + '1';
		Object obj = SpringContextUtils.getBean(tmpStr.substring(0, 1).toLowerCase() + tmpStr.substring(1));

		Class[] array = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			//java.lang.Interger拆箱
			if (args[i].getClass().getName().equals(Integer.class.getName())) {
				array[i] = int.class;
			} else {
				array[i] = args[i].getClass();
			}
		}

		Method method = clz.getDeclaredMethod(methodName, array);

		Object returnobj = method.invoke(obj, args);
		try {
			Class<?> rt = method.getReturnType();
			Type ty = method.getGenericReturnType();
			writeToLocal(returnobj, interfaceName, methodName, rt, ty);
		} catch (IOException e) {
			System.out.println("write to local error" + interfaceName + "." + methodName);
		}

		return method.invoke(obj, args);
	}

	private void writeToLocal(Object obj, String interfaceName, String methodName, Class<?> rt, Type ty) throws IOException {
		//InputStream inputStream = new BufferedInputStream(new FileInputStream("/spring/config.properties"));
		String classPath = System.getProperty("java.class.path");
		//int firstIndex = classPath.lastIndexOf(System.getProperty("path.separator")) + 1;
		//int lastIndex = classPath.lastIndexOf(File.separator) + 1;
//		InputStream inputStream = new BufferedInputStream(new FileInputStream(classPath.substring(firstIndex, lastIndex) + "/spring/config.properties"));
		InputStream inputStream = new BufferedInputStream(new FileInputStream(classPath.split(":")[0] + "/spring/config.properties"));

		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			System.out.println("can't find config.properties!");
		}
		String path = "";
		String basePath = p.getProperty("basePath");
		String FilePath = basePath + '/' + interfaceName.replace(".", "/") + '/' + methodName + ".json";
		path = p.getProperty(interfaceName + "." + methodName);

		writeFile(FilePath, JSON.toJSONString(obj, true), "UTF-8");
		System.out.println("write to local file " + FilePath + " succeed.");
	}

	public String generateFilePath(String interfaceName, String methodName, Object[] args, String basePath) {
		String path = "";
		if (methodName.equals("getExtByPIdAndidAppIdAndType")) {
			if (args[3].equals(3L)) {
				System.out.println("here");
				path = basePath + '/' + interfaceName.replace(".", "/") + '/' + methodName + ".json" + ".role";
			} else if (args[3].equals(1L)) {
				path = basePath + '/' + interfaceName.replace(".", "/") + '/' + methodName + ".json" + ".division";
			} else if (args[3].equals(5L)) {
				path = basePath + '/' + interfaceName.replace(".", "/") + '/' + methodName + ".json" + ".business";
			}
		}
		return path;
	}

	public Object dubboLocal(String interfaceName, String methodName, Object[] args) throws NoSuchMethodException, IOException {
		String classPath = System.getProperty("java.class.path");
//		int firstIndex = classPath.lastIndexOf(System.getProperty("path.separator")) + 1;
//		int lastIndex = classPath.lastIndexOf(File.separator) + 1;
//		InputStream inputStream = new BufferedInputStream(new FileInputStream(classPath.substring(firstIndex, lastIndex) + "/spring/config.properties"));
		InputStream inputStream = new BufferedInputStream(new FileInputStream(classPath.split(":")[0] + "/spring/config.properties"));
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			System.out.println("cann't find config.properties!");
		}

		String path = "";
		String content = "";
		String basePath = p.getProperty("basePath");

		String FilePath = "";
		int flag = 0;
		//手动处理的
		for (EnumHardMethod method : EnumHardMethod.values()) {
			if (methodName.equals(method.getValue())) {
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			FilePath = basePath + '/' + interfaceName.replace(".", "/") + '/' + methodName + ".json";
		} else {
			FilePath = generateFilePath(interfaceName, methodName, args, basePath);
		}
		LOGGER.info("the basePath is {}", basePath);
		LOGGER.info("the file path is {}", FilePath);

		path = p.getProperty(interfaceName + "." + methodName);
		if (!FilePath.equals("")) {
			try {
				content = readFile(FilePath, "UTF-8");
			} catch (Exception e) {
				System.out.println("we can't find " + FilePath + "!!!!");
			}
		}

		Class<?> clz = null;
		try {
			clz = Class.forName(interfaceName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Class[] array = new Class[args.length];
		//null的特殊处理
		if (methodName.equals("getUserSug")) {
			array[0] = String.class;
			array[1] = String.class;
			array[2] = String[].class;
			array[3] = String[].class;
			array[4] = int.class;
		} else if (methodName.equals("getResignationEhrUserByOrgCodeAndLeaveDate")) {
			array[0] = String.class;
			array[1] = Date.class;
			array[2] = Date.class;
		} else {
			for (int i = 0; i < args.length; i++) {
				if (args[i].getClass().getName().equals(Integer.class.getName()) && !methodName.equals("getTypeCodeByOrgAndBuildingId") && !methodName.equals("addFollowHouse") && !methodName.equals("queryHouseShowing") && !methodName.equals("getInvaildGroupBuild")) {
					array[i] = int.class;
				} else if (args[i].getClass().getName().equals(Long.class.getName()) && (methodName.equals("getAuthUserRoleByUcidAndAppId") || methodName.equals("getUserPositionDetail") ||
						methodName.equals("getVirtualRoleById") || methodName.equals("getUcidByPermissionAndAppId"))) {
					array[i] = long.class;
				} else if (args[i].getClass().getName().contains("java.util.ArrayList")) {
					array[i] = java.util.List.class;
				} else {
					array[i] = args[i].getClass();
				}
			}
		}
		Method method = null;
		try {
			method = clz.getDeclaredMethod(methodName, array);
		} catch (RuntimeException esp) {
			throw esp;
		}

		Class<?> rt = method.getReturnType();

		if ("java.lang.Integer".equals(rt.getName()) || "int".equals(rt.getName())) {
			return Integer.parseInt(content);
		} else if ("java.lang.Long".equals(rt.getName())) {
			return Long.parseLong(content);
		} else if ("java.lang.String".equals(rt.getName())) {
			return content;
		} else if ("java.lang.Boolean".equals(rt.getName()) || "boolean".equals(rt.getName())) {
			return Boolean.parseBoolean(content);
		} else if (rt.getName().startsWith("com.lianjia.")) {
			Type returnType = method.getGenericReturnType();
			if (returnType.toString().contains("com.lianjia.house.dto.DTOHouseInResourcePoolInfo")){
				return (com.lianjia.house.dto.DTOHouseInResourcePoolInfo)GsonUtil.fromJson(content,rt);
			}else if (returnType instanceof ParameterizedType) {
				Type return1Type = ((ParameterizedType) returnType).getActualTypeArguments()[0];
				if (return1Type.toString().startsWith("java.util.Map") & returnType.toString().contains("Pagination")) {
					// TODO 房客源分页类的处理
					Gson gson = new Gson();
					return gson.fromJson(content, returnType);
				} else if (returnType.toString().contains("com.lianjia.customer.dto.Pagination")) {//客源分页类
					Class<?> return1Class = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
					Type[] typeArguments = new MoeParameterizedTypeImpl[1];
					typeArguments[0] = new MoeParameterizedTypeImpl(return1Class, new MoeParameterizedTypeImpl[0], null);
					MoeParameterizedTypeImpl tripleType = new MoeParameterizedTypeImpl(rt, typeArguments, null);
					com.lianjia.customer.dto.Pagination page = JSON.parseObject(content, tripleType);
					return page;
				} else if (returnType.toString().contains("com.lianjia.uc.api.Page")) {
					Class<?> return1Class = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
					Type[] typeArguments = new MoeParameterizedTypeImpl[1];
					typeArguments[0] = new MoeParameterizedTypeImpl(return1Class, new MoeParameterizedTypeImpl[0], null);

					MoeParameterizedTypeImpl tripleType = new MoeParameterizedTypeImpl(rt, typeArguments, null);
					com.lianjia.uc.api.Page page = JSON.parseObject(content, tripleType);
					return page;
				}
			} else {
				//TODO,low
				if (returnType.toString().contains("com.lianjia.house.dto.DTOHouseSearchIndexgenFull")){
					//Gson gson = new Gson();
					//return (com.lianjia.house.dto.DTOHouseSearchIndexgenFull)gson.fromJson(content,returnType);
					return (com.lianjia.house.dto.DTOHouseSearchIndexgenFull) GsonUtil.fromJson(content,rt);
				}else {
					try {
						return JSON.parseObject(content, Class.forName(rt.getName()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} else if ("java.util.List".equals(rt.getName())) {
			Type returnType = method.getGenericReturnType();
			try {
				if (returnType instanceof ParameterizedType) {
					Type return1Type = ((ParameterizedType) returnType).getActualTypeArguments()[0];
					if (return1Type.toString().startsWith("java.util.Map")) {
						Gson gson = new Gson();
						List<Map<String, Object>> list = gson.fromJson(content, returnType);
						return list;
					} else if(return1Type.toString().contains("com.lianjia.house.dto.DTOHouseSearchIndexgenFull")){
						return  GsonUtil.fromJson(content, new TypeToken<List<DTOHouseSearchIndexgenFull>>() {});
					}else {
						Class type = (Class) (((ParameterizedType) returnType).getActualTypeArguments()[0]);
						Object obj = JSON.parseArray(content, Class.forName(type.getName()));
						return obj;
					}
				}else if("findAgentByHouseCode".equals(method.getName())){
					return JSON.parseArray(content, java.lang.Long.class);
				}else{
					return JSON.parseArray(content, new Type[]{returnType});
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if ("java.util.Map".equals(rt.getName())) {
			// Map<Object, List<Object>>
			Type returnType = method.getGenericReturnType();
			Type return1Type = ((ParameterizedType) returnType).getActualTypeArguments()[1];
			if (interfaceName.equals("com.lianjia.customer.api.service.CustomerDasFacade")) {
				//Gson gson = new Gson();
				//Map<String, Object> map = new HashMap<String, Object>();
				return (Map<String, Object>) JSON.parse(content);
			} else if (return1Type.toString().startsWith("java.util.List")) {
				Gson gson = new Gson();
				return (Map<Object, List<Object>>) gson.fromJson(content, returnType);
			} else {
				Gson gson = new Gson();
				return (Map<Object, Object>) gson.fromJson(content, returnType);
			}
		}
		return null;
	}

	public String readFile(String filepath, String Codingstyle) throws IOException {
		String content = "";
		String read;

		InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath), Charset.forName(Codingstyle));
		BufferedReader br = new BufferedReader(isr);
		while ((read = br.readLine()) != null) {
			content = content + read;
		}
		return content;
	}

	public void writeFile(String filepath, String content, String Codingstyle) throws IOException {
		File file = new File(filepath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(filepath), Charset.forName(Codingstyle));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedWriter br = new BufferedWriter(osw);
		String str_temp = content.replace(",", ",\n").replace("},{", "},\n{").replace("[{", "[\n{").replace("}]", "}\n]");
		br.write(str_temp);
		br.flush();

	}

	public static void main(String args[]) throws Exception {

		//String content = "{\"d1AreaHigh\": 110, \"d1Ids\": 861519, \"d1BusinessArea\": \"18335629\", \"d1AreaLow\": 100, \"d1PriceHigh\": 500, \"d1PriceLow\": 400, \"customerId\": \"32227862\", \"d1RoomHigh\": 3, \"d1RoomLow\": 2}";
		//String content = "{\n" +
		//		"    \"1000000020109428\": 1\n" +
		//		"}";
		//String content = "[{\"newestPriceVo\": {\"trend\": 0, \"unitPrice\": 20.2, \"housedelCode\": \"101100541674\", \"newestPrice\": 2222}, \"houseVisitCount\": 0, \"houseInResourcePoolInfo\": {\"isInResourcePool\": 0, \"housedelCode\": \"101100541674\", \"reason\": null, \"teamId\": null, \"create_time\": null, \"createTime\": null, \"inResPool\": false}, \"housedelCode\": \"101100541674\", \"houseBasic\": {\"buildSize\": 110, \"titleImage\": null, \"houseId\": 146, \"orientation\": \"100500000001\", \"cookroomAmount\": 1, \"bedroomAmount\": 2, \"cityId\": 110000, \"parlorAmount\": 1, \"toiletAmount\": 1}, \"maintainGroupOrgcodeArr\": \"A13D81,A13D37\", \"lastDate\": null, \"houseSaleRentStatus\": {\"housedelCodeList\": [\"101100541673\", \"101100541674\"], \"saleRentStatus\": true}, \"dutyGroupOrgcodeArr\": \"A11D90\", \"areaGroupOrgcodeArr\": \"A13D81,A13D37\", \"houseDelBasic\": {\"ownerMobilePhone2\": null, \"isInResourcePool\": 0, \"focusStatus\": null, \"delType\": 2, \"overdueWarnStatus\": 0, \"housedelCode\": 101100541674, \"pauseStatus\": null, \"linkerOtherMobilePhone\": null, \"fastBrokerUcid\": null, \"serialStatus\": null, \"hurryStatus\": null, \"isForeClosure\": 0, \"buyLimit\": null, \"delStatus\": 1, \"holderName\": \"\\u90ed\\u6c38\\u56fe\", \"updatedUcid\": 1000000020021115, \"ownerHomePhone\": null, \"isUnique\": null, \"keyBrokerUcid\": null, \"ownerMobilePhone1\": \"94B4BD4F1DF4900B6FAB5EA239427246\", \"rentDeadline\": null, \"holderUcid\": 1000000020021115, \"creatorUcid\": 1000000020021115, \"standardHouseId\": 1115032467764, \"delSourceSup\": \"001\", \"houseStatus\": null, \"buildSize\": 110, \"approBrokerUcid\": null, \"houseId\": 146, \"paymentMode\": null, \"delSourceSub\": \"001001\", \"delGrade\": \"B\", \"createdTime\": \"2017-02-23 17:14:15\", \"loginClient\": \"0\"}, \"houseTaxFreeInfo\": {\"businessTaxFree\": false, \"housedelCode\": \"101100541674\", \"individualIncomeTaxFree\": false}}]";
		String content = "{\"isInResourcePool\": 0,\"housedelCode\": \"103100000001\",\"reason\": \"reason\",\"teamId\": \"N11163\",\"create_time\": 148894567\", createTime\": 1488945673,\"inResPool\": false}";


		Class<?> clz = Class.forName("com.lianjia.house.api.HouseDelIndexgenFacade");
		Method[] method = clz.getMethods();

		for (int i = 0; i < method.length; ++i) {
			 Class<?> rt = method[i].getReturnType();
			 Type returnType = method[i].getGenericReturnType();
			// if ("getEhrUserPage" == method[i].getName()){
			//if ("queryCountBrokerShowing".equals(method[i].getName())) {"
/*			if ("getEhrUserBasicInfoByPage".equals(method[i].getName())) {
				Type returnType = method[i].getGenericReturnType();
				Class<?> rt = method[i].getReturnType();
				if (returnType.toString().contains("com.lianjia.uc.api.Page")) {
					Class<?> return1Class = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
					System.out.println(rt.getName());
					Type[] typeArguments = new MoeParameterizedTypeImpl[1];
					typeArguments[0] = new MoeParameterizedTypeImpl(return1Class, new MoeParameterizedTypeImpl[0], null);

					MoeParameterizedTypeImpl tripleType = new MoeParameterizedTypeImpl(rt, typeArguments, null);
					com.lianjia.uc.api.Page page = JSON.parseObject(content, tripleType);
					//System.out.println(tripleType);
					System.out.println(page.getList().toString());
				}
			}*/
			if ("getHouseInResourcePoolInfo".equals(method[i].getName()) ){
			//if ("getHouseSearchFullIndexInfoByDelCode".equals(method[i].getName()) ){
//				System.out.println(rt.getName());
//				Type returnType = method[i].getGenericReturnType();
				//Gson gson = new Gson();
				//System.out.println((com.lianjia.house.dto.DTOHouseSearchIndexgenFull)gson.fromJson(content,returnType));
				//Class<?> return1Class = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
				//Type[] typeArguments = new MoeParameterizedTypeImpl[1];
				//typeArguments[0] = new MoeParameterizedTypeImpl(return1Class, new MoeParameterizedTypeImpl[0], null);

				//MoeParameterizedTypeImpl tripleType = new MoeParameterizedTypeImpl(rt, typeArguments, null);

				//System.out.println(tripleType.getRawType());
				//System.out.println(GsonUtil.fromJson(content, new TypeToken<List<DTOHouseSearchIndexgenFull>>() {}));
				//System.out.println(JSON.parseObject(content, returnType));
				System.out.println(GsonUtil.fromJson(content,rt));
			}
		}
	}
}
