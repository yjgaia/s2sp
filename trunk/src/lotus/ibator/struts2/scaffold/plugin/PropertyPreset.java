package lotus.ibator.struts2.scaffold.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

public class PropertyPreset extends IbatorPluginAdapter {

	protected String sqlMapClientFactoryPackage; // sqlMapClientFactory�� ��Ű��
	protected String actionPackage; // �׼� ��Ű��
	protected String servicePackage; // ���� ��Ű��
	protected String charset = "UTF-8"; // ĳ���ͼ�
	protected Set<String> useSort; // ����
	protected Set<String> useSearch; // �˻�
	protected Map<String, FullyQualifiedJavaType> addInfo; // �������� ����
	protected String targetProject; // ��� ������Ʈ

	protected String namespace; // ���ӽ����̽�

	protected String modelPackage; // �� ��Ű��
	protected String dtoName;
	protected String daoName;
	protected String dtoInstanceName;
	protected String daoInstanceName;
	protected String exampleName;
	protected String exampleInstanceName;
	protected String keyInstanceName;
	protected List<IntrospectedColumn> primaryKeys;
	protected List<IntrospectedColumn> columns;

	protected boolean hasBLOBColumn = false;

	protected String daoPackage;

	protected String sqlMapClientFactoryName = "SqlMapClientFactory";

	protected FullyQualifiedJavaType dtoType;
	protected FullyQualifiedJavaType daoType;
	protected FullyQualifiedJavaType sqlMapClientFactoryType;
	protected FullyQualifiedJavaType exampleType;
	protected FullyQualifiedJavaType keyType;

	protected String srcPath; // �ҽ� ���� ����
	protected String webContentPath; // WebContent ���� ����

	protected boolean isUpdateProcees = false; // ���������ΰ�?
	protected boolean isCreateProperties = true; // properties ������ ������ΰ�?

	// �������� ���� (�׷���)
	protected Map<String, Map<String, FullyQualifiedJavaType>> addInfo2;

	// ���� �׼� �̸�
	protected String listActionNameInput = "list";
	protected String createActionNameInput = "create";
	protected String readActionNameInput = "read";
	protected String updateActionNameInput = "update";
	protected String deleteActionNameInput = "delete";

	// ���� ���� ���� �޼ҵ� �̸�
	protected String listMethodNameInput;
	protected String createMethodNameInput;
	protected String readMethodNameInput;
	protected String updateMethodNameInput;
	protected String deleteMethodNameInput;

	@Override
	public boolean validate(List<String> warnings) {
		boolean valid = true;

		// sqlMapClientFactoryPackage�� �ʼ� �Է�
		if (!StringUtility.stringHasValue(properties.getProperty("sqlMapClientFactoryPackage"))) {
			warnings.add("Struts2ScaffoldPlugin requires the sqlMapClientFactoryPackage property ");
			valid = false;
		}

		// servicePackage�� �ʼ� �Է�
		if (!StringUtility.stringHasValue(properties.getProperty("servicePackage"))) {
			warnings.add("Struts2ScaffoldPlugin requires the servicePackage property ");
			valid = false;
		}

		if (!StringUtility.stringHasValue(properties.getProperty("targetProject"))) {
			warnings.add(Messages.getString("Warning.25"));
			valid = false;
		}

		return valid;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(
			TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		setPropertyPreset(topLevelClass.getType(), introspectedTable);
		return true;
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(
			TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		setPropertyPreset(topLevelClass.getType(), introspectedTable);
		return true;
	}

	@Override
	public boolean modelRecordWithBLOBsClassGenerated(
			TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		setPropertyPreset(topLevelClass.getType(), introspectedTable);
		return true;
	}

	private void setPropertyPreset(
			FullyQualifiedJavaType topLevelClassType,
			IntrospectedTable introspectedTable) {

		columns = introspectedTable.getAllColumns();
		for (IntrospectedColumn column : columns) {
			if (column.isBLOBColumn()) {
				hasBLOBColumn = true;
				break;
			}
		}

		sqlMapClientFactoryPackage = properties.getProperty("sqlMapClientFactoryPackage");
		actionPackage = properties.getProperty("actionPackage");
		servicePackage = properties.getProperty("servicePackage");
		if (properties.getProperty("charset") != null) {
			charset = properties.getProperty("charset");
		}

		// useSort
		if (properties.getProperty("useSort") == null) {
			useSort = new HashSet<String>();
			for (IntrospectedColumn column : columns) {
				useSort.add(column.getJavaProperty());
			}
		} else if (!properties.getProperty("useSort").trim().equals("")) {
			useSort = getStringSet(properties.getProperty("useSort"));
		}

		// useSearch
		if (properties.getProperty("useSearch") == null) {
			useSearch = new HashSet<String>();
			for (IntrospectedColumn column : columns) {
				useSearch.add(column.getJavaProperty());
			}
		} else if (!properties.getProperty("useSearch").trim().equals("")) {
			useSearch = getStringSet(properties.getProperty("useSearch"));
		}

		// addInfo
		if (properties.getProperty("addInfo") != null) {
			addInfo = getObjectSet(properties.getProperty("addInfo"));
		}

		targetProject = properties.getProperty("targetProject");

		modelPackage = topLevelClassType.getPackageName();
		dtoName = topLevelClassType.getShortName();
		daoName = dtoName + "DAO";
		dtoInstanceName = GenerateUtil.toLowerCaseFirstLetter(dtoName);
		daoInstanceName = GenerateUtil.toLowerCaseFirstLetter(daoName);
		exampleName = dtoName + "Example";
		exampleInstanceName = GenerateUtil.toLowerCaseFirstLetter(exampleName);

		namespace = properties.getProperty("namespace");
		if (namespace == null) {
			namespace = "/" + dtoInstanceName;
		}

		primaryKeys = introspectedTable.getPrimaryKeyColumns();
		if (primaryKeys.size() == 1) {
			keyInstanceName = primaryKeys.get(0).getJavaProperty();
			keyType = primaryKeys.get(0).getFullyQualifiedJavaType();
		} else {
			keyInstanceName = null;
			keyType = GenerateUtil.generateType(modelPackage, dtoName + "Key");
		}

		srcPath = properties.getProperty("srcPath");
		webContentPath = properties.getProperty("webContentPath");

		if (properties.getProperty("addInfo2") != null && !properties.getProperty(
			"addInfo2").trim().equals(""))
			addInfo2 = getObjectGroup(properties.getProperty("addInfo2"));

		if (properties.getProperty("isUpdateProcees") != null)
			isUpdateProcees = Boolean.parseBoolean(properties.getProperty("isUpdateProcees"));

		if (properties.getProperty("isCreateProperties") != null)
			isCreateProperties = Boolean.parseBoolean(properties.getProperty("isCreateProperties"));

		// Action Name ����
		if (properties.getProperty("listActionName") != null)
			listActionNameInput = properties.getProperty("listActionName");
		if (properties.getProperty("createActionName") != null)
			createActionNameInput = properties.getProperty("createActionName");
		if (properties.getProperty("readActionName") != null)
			readActionNameInput = properties.getProperty("readActionName");
		if (properties.getProperty("updateActionName") != null)
			updateActionNameInput = properties.getProperty("updateActionName");
		if (properties.getProperty("deleteActionName") != null)
			deleteActionNameInput = properties.getProperty("deleteActionName");

		// Service Method Name ����
		if (properties.getProperty("listMethodName") != null)
			listMethodNameInput = properties.getProperty("listMethodName");
		else
			listMethodNameInput = listActionNameInput;

		if (properties.getProperty("createMethodName") != null)
			createMethodNameInput = properties.getProperty("createMethodName");
		else
			createMethodNameInput = createActionNameInput;

		if (properties.getProperty("readMethodName") != null)
			readMethodNameInput = properties.getProperty("readMethodName");
		else
			readMethodNameInput = readActionNameInput;

		if (properties.getProperty("updateMethodName") != null)
			updateMethodNameInput = properties.getProperty("updateMethodName");
		else
			updateMethodNameInput = updateActionNameInput;

		if (properties.getProperty("deleteMethodName") != null)
			deleteMethodNameInput = properties.getProperty("deleteMethodName");
		else
			deleteMethodNameInput = deleteActionNameInput;
	}

	/**
	 * ���ڿ��� ���ڿ� ������ (useSort, useSearch���� ���)
	 */
	private Set<String> getStringSet(String str) {
		Set<String> set = new HashSet<String>();
		if (!str.trim().equals("")) {
			String[] strArr = str.split(",");
			for (int i = 0; i < strArr.length; i++) {
				set.add(strArr[i].trim());
			}
		}
		return set;
	}

	/**
	 * ���ڿ��� ��ü ������ (addInfo���� ���)
	 */
	private Map<String, FullyQualifiedJavaType> getObjectSet(String str) {
		Map<String, FullyQualifiedJavaType> map = new HashMap<String, FullyQualifiedJavaType>();
		if (!str.trim().equals("")) {
			String[] strArr = str.split(",");
			for (int i = 0; i < strArr.length; i++) {
				String[] strArr2 = strArr[i].split(":");
				if (strArr2 == null) {
					map.put(str.trim(), new FullyQualifiedJavaType("String"));
				} else {
					map.put(strArr2[0].trim(), new FullyQualifiedJavaType(
						strArr2[1].trim()));
				}
			}
		}
		return map;
	}

	/**
	 * ���ڿ��� ��ü ������ (addInfo2���� ���)
	 */
	private Map<String, Map<String, FullyQualifiedJavaType>> getObjectGroup(
			String str) {
		Map<String, Map<String, FullyQualifiedJavaType>> map = new HashMap<String, Map<String, FullyQualifiedJavaType>>();
		while (str.indexOf("=") != -1) {
			String name = str.substring(0, str.indexOf("="));
			str = str.substring(str.indexOf("=") + 1);
			String content = str.substring(0, str.indexOf(";"));
			str = str.substring(str.indexOf(";") + 1);
			map.put(name.trim(), getObjectSet(content));
		}
		return map;
	}

	@Override
	public boolean daoImplementationGenerated(
			TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		daoPackage = topLevelClass.getType().getPackageName();
		setTypes();
		return true;
	}

	/**
	 * ���� Ÿ�Ե� ����
	 */
	private void setTypes() {
		dtoType = GenerateUtil.generateType(modelPackage, dtoName);
		daoType = GenerateUtil.generateType(daoPackage, daoName);
		sqlMapClientFactoryType = GenerateUtil.generateType(
			sqlMapClientFactoryPackage,
			sqlMapClientFactoryName);
		exampleType = GenerateUtil.generateType(modelPackage, exampleName);
	}

}
