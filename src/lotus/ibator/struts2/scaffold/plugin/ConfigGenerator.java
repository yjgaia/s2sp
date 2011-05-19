package lotus.ibator.struts2.scaffold.plugin;

import java.util.Map;

import org.apache.ibatis.ibator.api.GeneratedXmlFile;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.Document;
import org.apache.ibatis.ibator.api.dom.xml.TextElement;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;

public class ConfigGenerator extends ValidationsGenerator {

	protected String listViewFileName;
	protected String createInputViewFileName;
	protected String readViewFileName;
	protected String updateInputViewFileName;

	/**
	 * Struts2 설정 파일을 생성하는 함수
	 */
	public GeneratedXmlFile generateStruts2Config() {
		listViewFileName = listActionNameInput + ".jsp";
		createInputViewFileName = createActionNameInput + "-input.jsp";
		readViewFileName = readActionNameInput + ".jsp";
		updateInputViewFileName = updateActionNameInput + "-input.jsp";

		Document document = new Document(
			"-//Apache Software Foundation//DTD Struts Configuration 2.0//EN",
			"http://struts.apache.org/dtds/struts-2.0.dtd");

		XmlElement root = new XmlElement("struts");
		document.setRootElement(root);

		// package
		XmlElement packageEl = new XmlElement("package");
		packageEl.addAttribute(new Attribute("name", dtoInstanceName));
		packageEl.addAttribute(new Attribute("namespace", namespace));
		packageEl.addAttribute(new Attribute("extends", "struts-default"));
		root.addElement(packageEl);

		// list action
		XmlElement listAction = new XmlElement("action");
		listAction.addAttribute(new Attribute("name", listActionNameInput));
		listAction.addAttribute(new Attribute(
			"class",
			actionPackage + "." + listActionName));
		packageEl.addElement(listAction);

		// list action input result
		XmlElement listActionInputResult = new XmlElement("result");
		listActionInputResult.addAttribute(new Attribute("name", "input"));
		listActionInputResult.addElement(new TextElement(
			namespace + "/" + listViewFileName));
		listAction.addElement(listActionInputResult);

		// list action success result
		XmlElement listActionSuccessResult = new XmlElement("result");
		listActionSuccessResult.addElement(new TextElement(
			namespace + "/" + listViewFileName));
		listAction.addElement(listActionSuccessResult);

		// create action
		XmlElement createAction = new XmlElement("action");
		createAction.addAttribute(new Attribute(
			"name",
			createActionNameInput + "-*"));
		createAction.addAttribute(new Attribute("method", "{1}"));
		createAction.addAttribute(new Attribute(
			"class",
			actionPackage + "." + createActionName));
		packageEl.addElement(createAction);

		// create action input result
		XmlElement createActionInputResult = new XmlElement("result");
		createActionInputResult.addAttribute(new Attribute("name", "input"));
		createActionInputResult.addElement(new TextElement(
			namespace + "/" + createInputViewFileName));
		createAction.addElement(createActionInputResult);

		// create action success result
		XmlElement createActionSuccessResult = new XmlElement("result");
		createActionSuccessResult.addAttribute(new Attribute(
			"type",
			"redirectAction"));
		XmlElement createActionSuccessResultRedirect = new XmlElement("param");
		createActionSuccessResultRedirect.addAttribute(new Attribute(
			"name",
			"actionName"));

		if (primaryKeys.size() > 0) {
			createActionSuccessResultRedirect.addElement(new TextElement(
				readActionNameInput));
		} else {
			createActionSuccessResultRedirect.addElement(new TextElement(
				listActionNameInput));
		}
		createActionSuccessResult.addElement(createActionSuccessResultRedirect);

		if (primaryKeys.size() > 0) {
			for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
				XmlElement param = new XmlElement("param");
				param.addAttribute(new Attribute(
					"name",
					primaryKeyColumn.getJavaProperty()));
				param.addElement(new TextElement(
					"${" + primaryKeyColumn.getJavaProperty() + "}"));
				createActionSuccessResult.addElement(param);
			}
		}

		/* addAllFieldsIntoConfig */
		addAllFieldsIntoConfig(createActionSuccessResult);

		createAction.addElement(createActionSuccessResult);

		if (primaryKeys.size() > 0) {
			// read action
			XmlElement readAction = new XmlElement("action");
			readAction.addAttribute(new Attribute("name", readActionNameInput));
			readAction.addAttribute(new Attribute(
				"class",
				actionPackage + "." + readActionName));
			packageEl.addElement(readAction);

			// read action success result
			XmlElement readActionSuccessResult = new XmlElement("result");
			readActionSuccessResult.addElement(new TextElement(
				namespace + "/" + readViewFileName));
			readAction.addElement(readActionSuccessResult);

			XmlElement updateInputAction = new XmlElement("action");
			updateInputAction.addAttribute(new Attribute(
				"name",
				updateActionNameInput + "-input"));
			updateInputAction.addAttribute(new Attribute(
				"class",
				actionPackage + "." + readActionName));
			packageEl.addElement(updateInputAction);

			XmlElement updateInputActionResult = new XmlElement("result");
			updateInputActionResult.addElement(new TextElement(
				namespace + "/" + updateInputViewFileName));
			updateInputAction.addElement(updateInputActionResult);

			// update action
			XmlElement updateAction = new XmlElement("action");
			updateAction.addAttribute(new Attribute(
				"name",
				updateActionNameInput));
			updateAction.addAttribute(new Attribute(
				"class",
				actionPackage + "." + updateActionName));
			packageEl.addElement(updateAction);

			// update action input result
			XmlElement updateInputActionResult2 = new XmlElement("result");
			updateInputActionResult2.addAttribute(new Attribute("name", "input"));
			updateInputActionResult2.addElement(new TextElement(
				namespace + "/" + updateInputViewFileName));
			updateAction.addElement(updateInputActionResult2);

			// update action success result
			XmlElement updateActionSuccessResult = new XmlElement("result");
			updateActionSuccessResult.addAttribute(new Attribute(
				"type",
				"redirectAction"));
			XmlElement updateActionSuccessResultRedirect = new XmlElement(
				"param");
			updateActionSuccessResultRedirect.addAttribute(new Attribute(
				"name",
				"actionName"));
			updateActionSuccessResultRedirect.addElement(new TextElement(
				readActionNameInput));
			updateActionSuccessResult.addElement(updateActionSuccessResultRedirect);

			for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
				XmlElement updateActionSuccessResultId = new XmlElement("param");
				updateActionSuccessResultId.addAttribute(new Attribute(
					"name",
					primaryKeyColumn.getJavaProperty()));
				updateActionSuccessResultId.addElement(new TextElement(
					"${" + primaryKeyColumn.getJavaProperty() + "}"));
				updateActionSuccessResult.addElement(updateActionSuccessResultId);
			}

			/* addAllFieldsIntoConfig */
			addAllFieldsIntoConfig(updateActionSuccessResult);

			updateAction.addElement(updateActionSuccessResult);

			// delete action
			XmlElement deleteAction = new XmlElement("action");
			deleteAction.addAttribute(new Attribute(
				"name",
				deleteActionNameInput));
			deleteAction.addAttribute(new Attribute(
				"class",
				actionPackage + "." + deleteActionName));
			packageEl.addElement(deleteAction);

			// delete action success result
			XmlElement deleteActionSuccessResult = new XmlElement("result");
			deleteActionSuccessResult.addAttribute(new Attribute(
				"type",
				"redirectAction"));
			XmlElement deleteActionSuccessResultRedirect = new XmlElement(
				"param");
			deleteActionSuccessResultRedirect.addAttribute(new Attribute(
				"name",
				"actionName"));
			deleteActionSuccessResultRedirect.addElement(new TextElement(
				listActionNameInput));
			deleteActionSuccessResult.addElement(deleteActionSuccessResultRedirect);

			/* addAllFieldsIntoConfig */
			addAllFieldsIntoConfig(deleteActionSuccessResult);

			deleteAction.addElement(deleteActionSuccessResult);
		}
		return new GeneratedXmlFile(
			document,
			dtoInstanceName + "-struts.xml",
			actionPackage,
			targetProject,
			false);
	}

	/**
	 * 정렬 필드를 Config 파일에 추가
	 */
	private void addSortFieldsIntoConfig(XmlElement target) {
		XmlElement param = new XmlElement("param");
		param.addAttribute(new Attribute(
			"name",
			listSortInfoInstanceName + ".sort"));
		param.addElement(new TextElement(
			"${" + listSortInfoInstanceName + ".sort}"));
		target.addElement(param);

		param = new XmlElement("param");
		param.addAttribute(new Attribute(
			"name",
			listSortInfoInstanceName + ".order"));
		param.addElement(new TextElement(
			"${" + listSortInfoInstanceName + ".order}"));
		target.addElement(param);
	}

	/**
	 * 검색 필드를 Config 파일에 추가
	 */
	private void addSearchFieldsIntoConfig(XmlElement target) {
		for (IntrospectedColumn column : columns) {
			if (!column.isBLOBColumn() && useSearch.contains(column.getJavaProperty())) {
				String columnName = column.getJavaProperty();
				XmlElement param = new XmlElement("param");
				param.addAttribute(new Attribute(
					"name",
					listSearchInfoInstanceName + "." + columnName));
				param.addElement(new TextElement(
					"${" + listSearchInfoInstanceName + "." + columnName + "}"));
				target.addElement(param);

				// 숫자 혹은 날짜일때 범위 검색 기능 추가
				if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
					"TIMESTAMP")) {
					columnName = column.getJavaProperty();
					param = new XmlElement("param");
					param.addAttribute(new Attribute(
						"name",
						listSearchInfoInstanceName + "." + columnName + "From"));
					param.addElement(new TextElement(
						"${" + listSearchInfoInstanceName + "." + columnName + "From}"));
					target.addElement(param);

					columnName = column.getJavaProperty();
					param = new XmlElement("param");
					param.addAttribute(new Attribute(
						"name",
						listSearchInfoInstanceName + "." + columnName + "To"));
					param.addElement(new TextElement(
						"${" + listSearchInfoInstanceName + "." + columnName + "To}"));
					target.addElement(param);
				}
			}
		}
	}

	/**
	 * 기타 필드를 Config 파일에 추가
	 */
	private void addInfoFieldsIntoConfig(XmlElement target) {
		for (String key : addInfo.keySet()) {
			String columnName = key;
			XmlElement param = new XmlElement("param");
			param.addAttribute(new Attribute(
				"name",
				listInfoInstanceName + "." + columnName));
			param.addElement(new TextElement(
				"${" + listInfoInstanceName + "." + columnName + "}"));
			target.addElement(param);
		}
	}

	/**
	 * 기타2 필드를 Config 파일에 추가
	 */
	private void addInfo2FieldsIntoConfig(XmlElement target) {
		for (String key : addInfo2.keySet()) {
			Map<String, FullyQualifiedJavaType> addInfo = addInfo2.get(key);
			for (String key2 : addInfo.keySet()) {
				String columnName = key2;
				XmlElement param = new XmlElement("param");
				param.addAttribute(new Attribute(
					"name",
					listInfoInstanceName + "." + columnName));
				param.addElement(new TextElement(
					"${" + listInfoInstanceName + "." + columnName + "}"));
				target.addElement(param);
			}
		}
	}

	private void addAllFieldsIntoConfig(XmlElement target) {

		if (useSort != null) { // 정렬
			addSortFieldsIntoConfig(target);
		}

		if (useSearch != null) {// 검색
			addSearchFieldsIntoConfig(target);
		}

		if (addInfo != null) { // 기타 정보
			addInfoFieldsIntoConfig(target);
		}

		if (addInfo2 != null) { // 기타 정보2 사용시
			addInfo2FieldsIntoConfig(target);
		}
	}

}
