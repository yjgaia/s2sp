package lotus.ibator.struts2.scaffold.plugin;

import java.util.Map;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;

/**
 * Info
 */
public class InfoGenerator extends BasePreset {

	protected String listSortInfoName;
	protected String listSortInfoInstanceName;
	protected FullyQualifiedJavaType listSortInfoType;

	protected String listSearchInfoName;
	protected String listSearchInfoInstanceName;
	protected FullyQualifiedJavaType listSearchInfoType;

	protected String listInfoName;
	protected String listInfoInstanceName;
	protected FullyQualifiedJavaType listInfoType;

	/**
	 * List Sort Info 생성
	 */
	public GeneratedJavaFile generateListSortInfo() {
		listSortInfoName = dtoName + "ListSortInfo";
		listSortInfoInstanceName = GenerateUtil.toLowerCaseFirstLetter(listSortInfoName);
		listSortInfoType = GenerateUtil.generateType(
			modelPackage,
			listSortInfoName);

		TopLevelClass listSortInfoClass = GenerateUtil.generateClass(listSortInfoType);

		// useSort
		GenerateUtil.settingProperty(
			listSortInfoClass,
			"sort",
			stringType,
			true,
			true);
		GenerateUtil.settingProperty(
			listSortInfoClass,
			"order",
			stringType,
			true,
			true);

		return new GeneratedJavaFile(listSortInfoClass, targetProject);
	}

	/**
	 * List Search Info 생성
	 */
	public GeneratedJavaFile generateListSearchInfo() {
		listSearchInfoName = dtoName + "ListSearchInfo";
		listSearchInfoInstanceName = GenerateUtil.toLowerCaseFirstLetter(listSearchInfoName);
		listSearchInfoType = GenerateUtil.generateType(
			modelPackage,
			listSearchInfoName);

		TopLevelClass listSearchInfoClass = GenerateUtil.generateClass(listSearchInfoType);
		listSearchInfoClass.setSuperClass(dtoType);

		// 숫자 혹은 날짜일때 범위 검색 기능 추가
		for (IntrospectedColumn column : columns) {
			if (!column.isBLOBColumn() && useSearch.contains(column.getJavaProperty()) && (column.getJdbcTypeName().equals(
				"DECIMAL") || column.getJdbcTypeName().equals("TIMESTAMP"))) {
				GenerateUtil.settingProperty(
					listSearchInfoClass,
					column.getJavaProperty() + "From",
					column.getFullyQualifiedJavaType());

				GenerateUtil.settingProperty(
					listSearchInfoClass,
					column.getJavaProperty() + "To",
					column.getFullyQualifiedJavaType());
			}
		}

		return new GeneratedJavaFile(listSearchInfoClass, targetProject);
	}

	/**
	 * Info 생성
	 */
	public GeneratedJavaFile generateListInfo() {
		listInfoName = dtoName + "ListInfo";
		listInfoInstanceName = GenerateUtil.toLowerCaseFirstLetter(listInfoName);
		listInfoType = GenerateUtil.generateType(modelPackage, listInfoName);

		TopLevelClass listInfoClass = GenerateUtil.generateClass(listInfoType);

		// addInfo
		if (addInfo != null) {
			for (String key : addInfo.keySet()) {
				GenerateUtil.settingProperty(
					listInfoClass,
					key,
					addInfo.get(key));
			}
		}

		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				Map<String, FullyQualifiedJavaType> addInfo = addInfo2.get(key);
				for (String key2 : addInfo.keySet()) {
					GenerateUtil.settingProperty(
						listInfoClass,
						key2,
						addInfo.get(key2));
				}
			}
		}

		return new GeneratedJavaFile(listInfoClass, targetProject);
	}
}
