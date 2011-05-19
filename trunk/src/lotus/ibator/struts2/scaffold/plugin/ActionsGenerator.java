package lotus.ibator.struts2.scaffold.plugin;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;

public class ActionsGenerator extends ServiceGenerator {

	protected String superActionName;
	protected FullyQualifiedJavaType superActionType;

	/**
	 * SuperAction
	 */
	public GeneratedJavaFile generateSuperAction() {
		superActionName = "Super" + dtoName + "Action";
		superActionType = GenerateUtil.generateType(
			actionPackage,
			superActionName);
		FullyQualifiedJavaType actionSupportType = new FullyQualifiedJavaType(
			"com.opensymphony.xwork2.ActionSupport");

		TopLevelClass superActionClass = GenerateUtil.generateClass(superActionType);
		superActionClass.addImportedType(actionSupportType);
		superActionClass.setSuperClass(actionSupportType);
		superActionClass.addImportedType(serviceType);
		superActionClass.addField(GenerateUtil.generateField(
			serviceInstanceName,
			"protected",
			serviceType,
			serviceName + ".getInstance()"));

		if (useSort != null) {
			GenerateUtil.settingProperty(
				superActionClass,
				listSortInfoInstanceName,
				"protected",
				listSortInfoType,
				true,
				true,
				true);
		}
		if (useSearch != null) {
			GenerateUtil.settingProperty(
				superActionClass,
				listSearchInfoInstanceName,
				"protected",
				listSearchInfoType,
				true,
				true,
				true);
		}
		if (addInfo != null || addInfo2 != null) {
			GenerateUtil.settingProperty(
				superActionClass,
				listInfoInstanceName,
				"protected",
				listInfoType,
				true,
				true,
				true);
		}

		return new GeneratedJavaFile(superActionClass, targetProject);
	}

	protected String listActionName;
	protected FullyQualifiedJavaType listActionType;

	protected String dtoListInstanceName;
	protected String dtoCountInstanceName;

	/**
	 * ListAction
	 */
	public GeneratedJavaFile generateListAction() {
		listActionName = dtoName + GenerateUtil.toUpperCaseFirstLetter(listActionNameInput) + "Action";
		listActionType = GenerateUtil.generateType(
			actionPackage,
			listActionName);
		TopLevelClass listActionClass = GenerateUtil.generateClass(listActionType);
		listActionClass.setSuperClass(superActionType);
		// List
		dtoListInstanceName = dtoInstanceName + "List";
		GenerateUtil.settingProperty(
			listActionClass,
			dtoListInstanceName,
			listType,
			true,
			false);
		// Count
		dtoCountInstanceName = dtoInstanceName + "Count";
		GenerateUtil.settingProperty(
			listActionClass,
			dtoCountInstanceName,
			intType,
			true,
			false);
		// Example
		listActionClass.addImportedType(exampleType);
		listActionClass.addField(GenerateUtil.generateField(
			exampleInstanceName,
			exampleType,
			true));
		// Execute
		Method execute = GenerateUtil.generateMethod("execute", stringType);
		execute.addAnnotation("@Override");
		execute.addException(exceptionType);

		// 검색
		if (useSearch != null) {
			execute.addBodyLine(exampleName + ".Criteria criteria = " + exampleInstanceName + ".createCriteria();");
			for (IntrospectedColumn column : columns) {
				if (!column.isBLOBColumn() && useSearch.contains(column.getJavaProperty())) {
					String propertyName = GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty());
					if (column.isJdbcCharacterColumn()) {
						execute.addBodyLine("if (" + listSearchInfoInstanceName + ".get" + propertyName + "() != null && !" + listSearchInfoInstanceName + ".get" + propertyName + "().equals(\"\")) {");
						execute.addBodyLine("criteria.and" + propertyName + "Like(\"%\" + " + listSearchInfoInstanceName + ".get" + propertyName + "() + \"%\");");
					} else {
						execute.addBodyLine("if (" + listSearchInfoInstanceName + ".get" + propertyName + "() != null) {");
						execute.addBodyLine("criteria.and" + propertyName + "EqualTo(" + listSearchInfoInstanceName + ".get" + propertyName + "());");
					}
					execute.addBodyLine("}");

					// 숫자 혹은 날짜일때 범위 검색 기능 추가
					if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
						"TIMESTAMP")) {
						execute.addBodyLine("if (" + listSearchInfoInstanceName + ".get" + propertyName + "From() != null) {");
						execute.addBodyLine("criteria.and" + propertyName + "GreaterThanOrEqualTo(" + listSearchInfoInstanceName + ".get" + propertyName + "From());");
						execute.addBodyLine("}");

						execute.addBodyLine("if (" + listSearchInfoInstanceName + ".get" + propertyName + "To() != null) {");
						execute.addBodyLine("criteria.and" + propertyName + "LessThanOrEqualTo(" + listSearchInfoInstanceName + ".get" + propertyName + "To());");
						execute.addBodyLine("}");
					}
				}
			}
		}

		execute.addBodyLine(dtoCountInstanceName + " = " + serviceInstanceName + ".getCount(" + exampleInstanceName + ");");

		// 정렬
		if (useSort != null) {
			String firstSort;
			if (primaryKeys.size() > 0) {
				firstSort = primaryKeys.get(0).getActualColumnName().toLowerCase();
			} else {
				firstSort = columns.get(0).getActualColumnName().toLowerCase();
			}
			execute.addBodyLine("if (" + listSortInfoInstanceName + ".getSort() == null || " + listSortInfoInstanceName + ".getSort().equals(\"\")) {");
			execute.addBodyLine(listSortInfoInstanceName + ".setSort(\"" + firstSort + "\");");
			execute.addBodyLine("}");
			execute.addBodyLine("if (" + listSortInfoInstanceName + ".getOrder() == null || " + listSortInfoInstanceName + ".getOrder().equals(\"\")) {");
			execute.addBodyLine(listSortInfoInstanceName + ".setOrder(\"desc\");");
			execute.addBodyLine("}");

			execute.addBodyLine(exampleInstanceName + ".setOrderByClause(" + listSortInfoInstanceName + ".getSort() + \" \" + " + listSortInfoInstanceName + ".getOrder());");
		}

		execute.addBodyLine(dtoListInstanceName + " = " + serviceInstanceName + "." + listMethodNameInput + "(" + exampleInstanceName + ");");
		execute.addBodyLine("return SUCCESS;");
		listActionClass.addMethod(execute);
		return new GeneratedJavaFile(listActionClass, targetProject);
	}

	protected String createActionName;
	protected FullyQualifiedJavaType createActionType;

	/**
	 * CreateAction
	 */
	public GeneratedJavaFile generateCreateAction() {
		createActionName = dtoName + GenerateUtil.toUpperCaseFirstLetter(createActionNameInput) + "Action";
		createActionType = GenerateUtil.generateType(
			actionPackage,
			createActionName);
		TopLevelClass createActionClass = GenerateUtil.generateClass(createActionType);
		createActionClass.setSuperClass(superActionType);
		createActionClass.addImportedType(modelDrivenType);
		createActionClass.addSuperInterface(modelDrivenType);
		// DTO
		createActionClass.addImportedType(dtoType);
		createActionClass.addField(GenerateUtil.generateField(
			dtoInstanceName,
			dtoType,
			true));
		// GetModel
		Method method = GenerateUtil.generateMethod("getModel", dtoType);
		method.addAnnotation("@Override");
		method.addBodyLine("return " + dtoInstanceName + ";");
		createActionClass.addMethod(method);
		// Validate
		if (primaryKeys.size() > 0) {
			Method validate = GenerateUtil.generateMethod("validate");
			validate.addAnnotation("@Override");
			validate.addBodyLine("try {");

			String paramKeyName;
			if (primaryKeys.size() == 1) {
				paramKeyName = dtoInstanceName + ".get" + GenerateUtil.toUpperCaseFirstLetter(primaryKeys.get(
					0).getJavaProperty() + "()");
			} else {
				paramKeyName = dtoInstanceName;
			}
			validate.addBodyLine("if (getFieldErrors().get(\"" + primaryKeys.get(
				0).getJavaProperty() + "\") == null && " + serviceInstanceName + ".isExistentPrimaryKey(" + paramKeyName + ")) {");

			String msg = null;
			if (isCreateProperties) {
				msg = "getText(\"" + dtoInstanceName + ".existent.primaryKey\")";
			} else {
				// properties 파일을 만들지 않을 경우
				msg = "\"" + dtoInstanceName + ".existent.primaryKey\"";
			}

			validate.addBodyLine("addFieldError(\"" + primaryKeys.get(0).getJavaProperty() + "\", " + msg + ");");
			validate.addBodyLine("}");

			validate.addBodyLine("} catch (Exception e) {");
			validate.addBodyLine("e.printStackTrace();");
			validate.addBodyLine("}");
			createActionClass.addMethod(validate);
		}
		// Execute
		Method execute = GenerateUtil.generateMethod("execute", stringType);
		execute.addAnnotation("@Override");
		execute.addException(exceptionType);
		execute.addBodyLine(serviceInstanceName + "." + createMethodNameInput + "(" + dtoInstanceName + ");");
		execute.addBodyLine("return SUCCESS;");
		createActionClass.addMethod(execute);
		return new GeneratedJavaFile(createActionClass, targetProject);
	}

	protected String readActionName;
	protected FullyQualifiedJavaType readActionType;

	/**
	 * ReadAction
	 */
	public GeneratedJavaFile generateReadAction() {
		readActionName = dtoName + GenerateUtil.toUpperCaseFirstLetter(readActionNameInput) + "Action";
		readActionType = GenerateUtil.generateType(
			actionPackage,
			readActionName);
		TopLevelClass readActionClass = GenerateUtil.generateClass(readActionType);
		readActionClass.setSuperClass(superActionType);
		// Key
		readActionClass.addImportedType(keyType);
		String keyInstanceName = this.keyInstanceName;
		if (keyInstanceName == null) {
			keyInstanceName = dtoInstanceName + "Key";
		}
		if (primaryKeys.size() == 1) {
			GenerateUtil.settingProperty(
				readActionClass,
				keyInstanceName,
				keyType,
				false,
				true);
		} else {
			readActionClass.addField(GenerateUtil.generateField(
				keyInstanceName,
				keyType,
				true));
			readActionClass.addImportedType(modelDrivenType);
			readActionClass.addSuperInterface(modelDrivenType);
			// GetModel
			Method method = GenerateUtil.generateMethod("getModel", keyType);
			method.addAnnotation("@Override");
			method.addBodyLine("return " + keyInstanceName + ";");
			readActionClass.addMethod(method);
		}
		// DTO
		readActionClass.addImportedType(dtoType);
		GenerateUtil.settingProperty(
			readActionClass,
			dtoInstanceName,
			dtoType,
			true,
			false);
		// Execute
		Method execute = GenerateUtil.generateMethod("execute", stringType);
		execute.addAnnotation("@Override");
		execute.addException(exceptionType);
		execute.addBodyLine(dtoInstanceName + " = " + serviceInstanceName + "." + readMethodNameInput + "(" + keyInstanceName + ");");
		execute.addBodyLine("return SUCCESS;");
		readActionClass.addMethod(execute);
		return new GeneratedJavaFile(readActionClass, targetProject);
	}

	protected String updateActionName;
	protected FullyQualifiedJavaType updateActionType;

	/**
	 * UpdateAction
	 */
	public GeneratedJavaFile generateUpdateAction() {
		updateActionName = dtoName + GenerateUtil.toUpperCaseFirstLetter(updateActionNameInput) + "Action";
		updateActionType = GenerateUtil.generateType(
			actionPackage,
			updateActionName);
		TopLevelClass updateActionClass = GenerateUtil.generateClass(updateActionType);
		updateActionClass.setSuperClass(superActionType);
		updateActionClass.addImportedType(modelDrivenType);
		updateActionClass.addSuperInterface(modelDrivenType);
		// DTO
		updateActionClass.addImportedType(dtoType);
		GenerateUtil.settingProperty(
			updateActionClass,
			dtoInstanceName,
			dtoType,
			true,
			false,
			true);
		// GetModel
		Method method = GenerateUtil.generateMethod("getModel", dtoType);
		method.addAnnotation("@Override");
		method.addBodyLine("return " + dtoInstanceName + ";");
		updateActionClass.addMethod(method);
		// Execute
		Method execute = GenerateUtil.generateMethod("execute", stringType);
		execute.addAnnotation("@Override");
		execute.addException(exceptionType);
		execute.addBodyLine(serviceInstanceName + "." + updateMethodNameInput + "(" + dtoInstanceName + ");");
		execute.addBodyLine("return SUCCESS;");
		updateActionClass.addMethod(execute);
		return new GeneratedJavaFile(updateActionClass, targetProject);
	}

	protected String deleteActionName;
	protected FullyQualifiedJavaType deleteActionType;

	/**
	 * DeleteAction
	 */
	public GeneratedJavaFile generateDeleteAction() {
		deleteActionName = dtoName + GenerateUtil.toUpperCaseFirstLetter(deleteActionNameInput) + "Action";
		deleteActionType = GenerateUtil.generateType(
			actionPackage,
			deleteActionName);
		TopLevelClass deleteActionClass = GenerateUtil.generateClass(deleteActionType);
		deleteActionClass.setSuperClass(superActionType);
		// Key
		deleteActionClass.addImportedType(keyType);
		String keyInstanceName = this.keyInstanceName;
		if (keyInstanceName == null) {
			keyInstanceName = dtoInstanceName + "Key";
		}
		if (primaryKeys.size() == 1) {
			GenerateUtil.settingProperty(
				deleteActionClass,
				keyInstanceName,
				keyType,
				false,
				true);
		} else {
			deleteActionClass.addField(GenerateUtil.generateField(
				keyInstanceName,
				keyType,
				true));
			deleteActionClass.addImportedType(modelDrivenType);
			deleteActionClass.addSuperInterface(modelDrivenType);
			// GetModel
			Method method = GenerateUtil.generateMethod("getModel", keyType);
			method.addAnnotation("@Override");
			method.addBodyLine("return " + keyInstanceName + ";");
			deleteActionClass.addMethod(method);
		}
		// Execute
		Method execute = GenerateUtil.generateMethod("execute", stringType);
		execute.addAnnotation("@Override");
		execute.addException(exceptionType);
		execute.addBodyLine(serviceInstanceName + "." + deleteMethodNameInput + "(" + keyInstanceName + ");");
		execute.addBodyLine("return SUCCESS;");
		deleteActionClass.addMethod(execute);
		return new GeneratedJavaFile(deleteActionClass, targetProject);
	}

}
