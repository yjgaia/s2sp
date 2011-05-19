package lotus.ibator.struts2.scaffold.plugin;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;

public class BasePreset extends PropertyPreset {

	protected FullyQualifiedJavaType stringType = new FullyQualifiedJavaType("String");
	protected FullyQualifiedJavaType intType = new FullyQualifiedJavaType("int");
	protected FullyQualifiedJavaType booleanType = new FullyQualifiedJavaType("boolean");
	protected FullyQualifiedJavaType listType = new FullyQualifiedJavaType("java.util.List");
	protected FullyQualifiedJavaType exceptionType = new FullyQualifiedJavaType("Exception");
	protected FullyQualifiedJavaType modelDrivenType = new FullyQualifiedJavaType("com.opensymphony.xwork2.ModelDriven");
	protected FullyQualifiedJavaType preparableType = new FullyQualifiedJavaType("com.opensymphony.xwork2.Preparable");

}
