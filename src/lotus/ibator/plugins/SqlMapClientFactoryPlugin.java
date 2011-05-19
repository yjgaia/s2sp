package lotus.ibator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * SqlMapClientFactory를 생성하는 플러그인
 * 
 * @author Lotus
 * 
 */
public class SqlMapClientFactoryPlugin extends IbatorPluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		boolean valid = true;

		// sqlMapConfigPath은 필수 입력
		if (!StringUtility.stringHasValue(properties.getProperty("sqlMapConfigPath"))) {
			warnings.add("SqlMapClientFactoryPlugin requires the sqlMapConfigPath property ");
			valid = false;
		}

		if (!StringUtility.stringHasValue(properties.getProperty("targetProject"))) {
			warnings.add(Messages.getString("Warning.25"));
			valid = false;
		}

		if (!StringUtility.stringHasValue(properties.getProperty("targetPackage"))) {
			warnings.add(Messages.getString("Warning.26"));
			valid = false;
		}

		return valid;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {

		String sqlMapClientFactoryName = "SqlMapClientFactory";
		FullyQualifiedJavaType sqlMapClientFactoryType = new FullyQualifiedJavaType(
			properties.getProperty("targetPackage") + "." + sqlMapClientFactoryName);

		// 클래스 생성
		TopLevelClass sqlMapClientFactorySource = new TopLevelClass(
			sqlMapClientFactoryType);

		// public
		sqlMapClientFactorySource.setVisibility(JavaVisibility.PUBLIC);

		// import
		sqlMapClientFactorySource.addImportedType(new FullyQualifiedJavaType(
			"java.io.Reader"));
		sqlMapClientFactorySource.addImportedType(new FullyQualifiedJavaType(
			"com.ibatis.common.resources.Resources"));
		sqlMapClientFactorySource.addImportedType(new FullyQualifiedJavaType(
			"com.ibatis.sqlmap.client.SqlMapClient"));
		sqlMapClientFactorySource.addImportedType(new FullyQualifiedJavaType(
			"com.ibatis.sqlmap.client.SqlMapClientBuilder"));

		Field field;
		Method method;

		// resource
		field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(new FullyQualifiedJavaType("String"));
		field.setName("resource");
		field.setInitializationString("\"" + properties.getProperty("sqlMapConfigPath") + "\"");
		sqlMapClientFactorySource.addField(field);

		// client
		field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(new FullyQualifiedJavaType(
			"com.ibatis.sqlmap.client.SqlMapClient"));
		field.setName("client");
		sqlMapClientFactorySource.addField(field);

		// instance
		field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setStatic(true);
		field.setType(sqlMapClientFactoryType);
		field.setName("instance");
		field.setInitializationString("new " + sqlMapClientFactoryName + "()");
		sqlMapClientFactorySource.addField(field);

		// getInstance
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setStatic(true);
		method.setReturnType(sqlMapClientFactoryType);
		method.setName("getInstance");
		method.addBodyLine("return instance;");
		sqlMapClientFactorySource.addMethod(method);

		// 생성자
		method = new Method();
		method.setVisibility(JavaVisibility.PRIVATE);
		method.setConstructor(true);
		method.setName(sqlMapClientFactoryName);
		method.addBodyLine("try {");
		method.addBodyLine("Reader reader = Resources.getResourceAsReader(resource);");
		method.addBodyLine("client = SqlMapClientBuilder.buildSqlMapClient(reader);");
		method.addBodyLine("reader.close();");
		method.addBodyLine("} catch (Exception e) {");
		method.addBodyLine("e.printStackTrace();");
		method.addBodyLine("}");
		sqlMapClientFactorySource.addMethod(method);

		// getClient
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType(
			"com.ibatis.sqlmap.client.SqlMapClient"));
		method.setName("getClient");
		method.addBodyLine("return client;");
		sqlMapClientFactorySource.addMethod(method);

		GeneratedJavaFile gjf = new GeneratedJavaFile(
			sqlMapClientFactorySource,
			properties.getProperty("targetProject"));

		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		answer.add(gjf);

		return answer;
	}

}
