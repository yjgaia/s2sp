package lotus.ibator.struts2.scaffold.plugin;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.IntrospectedColumn;

public class PropertiesGenerator extends ConfigGenerator {

	/**
	 * package.properties파일 생성
	 */
	public String generateProperties() {
		StringBuffer document = new StringBuffer();
		document.append(dtoInstanceName + "." + listActionNameInput + ".title=" + dtoName + " List");
		document.append("\n");
		document.append(dtoInstanceName + "." + createActionNameInput + ".title=Create " + dtoName);
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + readActionNameInput + ".title=Show " + dtoName);
			document.append("\n");
			document.append(dtoInstanceName + "." + updateActionNameInput + ".title=Edit " + dtoName);
			document.append("\n");
		}

		// 검색
		document.append(dtoInstanceName + ".search.submit=Search");
		document.append("\n");

		document.append(dtoInstanceName + "." + createActionNameInput + ".submit=Create");
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + updateActionNameInput + ".submit=Update");
			document.append("\n");
		}
		document.append(dtoInstanceName + ".linkto." + listActionNameInput + "=Return to List");
		document.append("\n");
		document.append(dtoInstanceName + ".linkto." + createActionNameInput + "=New " + dtoName);
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".linkto." + readActionNameInput + "=Return to Show");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + updateActionNameInput + "=Edit");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + deleteActionNameInput + "=Delete");
			document.append("\n");
		}
		document.append(dtoInstanceName + "." + listActionNameInput + ".count=Count");
		document.append("\n");
		for (IntrospectedColumn column : columns) {
			document.append(dtoInstanceName + "." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()));
			document.append("\n");

			// 숫자 혹은 날짜일때 범위 검색 기능 추가
			if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
				"TIMESTAMP")) {
				document.append(dtoInstanceName + "." + column.getJavaProperty() + "From=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + " From");
				document.append("\n");

				document.append(dtoInstanceName + "." + column.getJavaProperty() + "To=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + " To");
				document.append("\n");
			}
		}
		for (IntrospectedColumn column : columns) {
			if (!column.isNullable()) {
				if (column.isStringColumn()) {
					document.append(dtoInstanceName + ".requiredstring." + column.getJavaProperty() + "=Please write " + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + ".");
					document.append("\n");
				} else {
					document.append(dtoInstanceName + ".required." + column.getJavaProperty() + "=Please write " + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + ".");
					document.append("\n");
				}
			}
			if (column.isStringColumn()) {
				document.append(dtoInstanceName + ".stringlength." + column.getJavaProperty() + "=Please write " + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + " no more than " + new Integer(
					column.getLength() / 2).toString() + " letters.");
				document.append("\n");
			}
		}
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".existent.primaryKey=Existent Primary Key(s).");
			document.append("\n");
		}
		return document.toString();
	}

	/**
	 * 한국어 사용자를 위한 package_ko.properties파일 생성
	 */
	public String generateKoProperties() {
		StringBuffer document = new StringBuffer();
		document.append(dtoInstanceName + "." + listActionNameInput + ".title=" + dtoName + " 목록");
		document.append("\n");
		document.append(dtoInstanceName + "." + createActionNameInput + ".title=" + dtoName + " 생성");
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + readActionNameInput + ".title=" + dtoName + " 내용");
			document.append("\n");
			document.append(dtoInstanceName + "." + updateActionNameInput + ".title=" + dtoName + " 수정");
			document.append("\n");
		}

		// 검색
		document.append(dtoInstanceName + ".search.submit=검색");
		document.append("\n");

		document.append(dtoInstanceName + "." + createActionNameInput + ".submit=생성하기");
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + updateActionNameInput + ".submit=수정하기");
			document.append("\n");
		}
		document.append(dtoInstanceName + ".linkto." + listActionNameInput + "=목록으로 돌아가기");
		document.append("\n");
		document.append(dtoInstanceName + ".linkto." + createActionNameInput + "=새 " + dtoName);
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".linkto." + readActionNameInput + "=내용으로 돌아가기");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + updateActionNameInput + "=수정");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + deleteActionNameInput + "=삭제");
			document.append("\n");
		}
		document.append(dtoInstanceName + "." + listActionNameInput + ".count=개수");
		document.append("\n");
		for (IntrospectedColumn column : columns) {
			document.append(dtoInstanceName + "." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()));
			document.append("\n");

			// 숫자 혹은 날짜일때 범위 검색 기능 추가
			if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
				"TIMESTAMP")) {
				document.append(dtoInstanceName + "." + column.getJavaProperty() + "From=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "의 시작");
				document.append("\n");

				document.append(dtoInstanceName + "." + column.getJavaProperty() + "To=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "의 끝");
				document.append("\n");
			}
		}
		for (IntrospectedColumn column : columns) {
			if (!column.isNullable()) {
				if (column.isStringColumn()) {
					document.append(dtoInstanceName + ".requiredstring." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "을/를 써 주시기 바랍니다.");
					document.append("\n");
				} else {
					document.append(dtoInstanceName + ".required." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "을/를 써 주시기 바랍니다.");
					document.append("\n");
				}
			}
			if (column.isStringColumn()) {
				document.append(dtoInstanceName + ".stringlength." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "은/는 최대 " + new Integer(
					column.getLength() / 2).toString() + "글자 입니다.");
				document.append("\n");
			}
		}
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".existent.primaryKey=이미 존재하는 Primary Key 입니다.");
			document.append("\n");
		}
		return GenerateUtil.unicode2UnicodeEsc(document.toString());
	}

}
