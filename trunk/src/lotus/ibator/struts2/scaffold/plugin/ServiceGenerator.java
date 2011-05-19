package lotus.ibator.struts2.scaffold.plugin;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;

public class ServiceGenerator extends InfoGenerator {

	protected String serviceName;
	protected String serviceInstanceName;
	protected FullyQualifiedJavaType serviceType;

	/**
	 * 서비스 생성
	 */
	public GeneratedJavaFile generateService() {
		// 이름 초기화
		serviceName = dtoName + "Service";
		serviceInstanceName = GenerateUtil.toLowerCaseFirstLetter(serviceName);
		serviceType = GenerateUtil.generateType(servicePackage, serviceName);

		// 클래스 생성
		TopLevelClass serviceClass = GenerateUtil.generateClass(serviceType);
		serviceClass.addField(GenerateUtil.generateField(
			"instance",
			"private",
			true,
			false,
			serviceType,
			true));
		FullyQualifiedJavaType sqlMapClientType = new FullyQualifiedJavaType(
			"com.ibatis.sqlmap.client.SqlMapClient");

		// sqlMapClient
		serviceClass.addImportedType(sqlMapClientType);
		serviceClass.addImportedType(sqlMapClientFactoryType);
		serviceClass.addField(GenerateUtil.generateField(
			"sqlMapClient",
			sqlMapClientType,
			sqlMapClientFactoryName + ".getInstance().getClient()"));

		// dao
		serviceClass.addImportedType(daoType);
		serviceClass.addImportedType(GenerateUtil.generateType(
			daoPackage,
			daoName + "Impl"));
		serviceClass.addField(GenerateUtil.generateField(
			daoInstanceName,
			daoType,
			"new " + daoName + "Impl(sqlMapClient)"));

		// getInstance
		Method getInstance = GenerateUtil.generateMethod(
			"getInstance",
			"public",
			true,
			false,
			serviceType);
		getInstance.addBodyLine("return instance;");
		serviceClass.addMethod(getInstance);

		// constructor
		Method constructor = GenerateUtil.generateMethod(
			serviceName,
			"private",
			null);
		constructor.setConstructor(true);
		constructor.addBodyLine("");
		serviceClass.addMethod(constructor);

		// getCount
		Method getCount = GenerateUtil.generateMethod("getCount", intType);
		serviceClass.addImportedType(exampleType);
		getCount.addParameter(new Parameter(exampleType, exampleInstanceName));
		getCount.addException(exceptionType);
		getCount.addBodyLine("return " + daoInstanceName + ".countByExample(" + exampleInstanceName + ");");
		serviceClass.addMethod(getCount);

		// List
		serviceClass.addImportedType(listType);
		Method list = GenerateUtil.generateMethod(listMethodNameInput, listType);
		serviceClass.addImportedType(exampleType);
		list.addParameter(new Parameter(exampleType, exampleInstanceName));
		list.addException(exceptionType);
		if (hasBLOBColumn && primaryKeys.size() > 0) {
			list.addBodyLine("return " + daoInstanceName + ".selectByExampleWithoutBLOBs(" + exampleInstanceName + ");");
		} else {
			list.addBodyLine("return " + daoInstanceName + ".selectByExample(" + exampleInstanceName + ");");
		}
		serviceClass.addMethod(list);

		// Create
		Method create = GenerateUtil.generateMethod(createMethodNameInput);
		serviceClass.addImportedType(dtoType);
		create.addParameter(new Parameter(dtoType, dtoInstanceName));
		create.addException(exceptionType);
		create.addBodyLine(daoInstanceName + ".insert(" + dtoInstanceName + ");");
		serviceClass.addMethod(create);

		// Primary Key가 있을때
		if (primaryKeys.size() > 0) {
			String keyInstanceName = this.keyInstanceName;
			if (keyInstanceName == null) {
				keyInstanceName = "key";
			}

			// Read
			Method read = GenerateUtil.generateMethod(
				readMethodNameInput,
				dtoType);
			read.addException(exceptionType);
			serviceClass.addImportedType(keyType);
			read.addParameter(new Parameter(keyType, keyInstanceName));
			read.addBodyLine("return " + daoInstanceName + ".selectByPrimaryKey(" + keyInstanceName + ");");
			serviceClass.addMethod(read);

			// Update
			Method update = GenerateUtil.generateMethod(updateMethodNameInput);
			serviceClass.addImportedType(dtoType);
			update.addParameter(new Parameter(dtoType, dtoInstanceName));
			update.addException(exceptionType);
			update.addBodyLine(daoInstanceName + ".updateByPrimaryKeySelective(" + dtoInstanceName + ");");
			serviceClass.addMethod(update);

			// Delete
			Method delete = GenerateUtil.generateMethod(deleteMethodNameInput);
			delete.addException(exceptionType);
			serviceClass.addImportedType(keyType);
			delete.addParameter(new Parameter(keyType, keyInstanceName));
			delete.addBodyLine(daoInstanceName + ".deleteByPrimaryKey(" + keyInstanceName + ");");
			serviceClass.addMethod(delete);

			// IsExistentPrimaryKey
			Method isExistentPrimaryKey = GenerateUtil.generateMethod(
				"isExistentPrimaryKey",
				booleanType);
			isExistentPrimaryKey.addException(exceptionType);
			serviceClass.addImportedType(keyType);
			isExistentPrimaryKey.addParameter(new Parameter(
				keyType,
				keyInstanceName));
			isExistentPrimaryKey.addBodyLine("return " + daoInstanceName + ".selectByPrimaryKey(" + keyInstanceName + ") != null;");
			serviceClass.addMethod(isExistentPrimaryKey);
		}

		return new GeneratedJavaFile(serviceClass, targetProject);
	}
}
