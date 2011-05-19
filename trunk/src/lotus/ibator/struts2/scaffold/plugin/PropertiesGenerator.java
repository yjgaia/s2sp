package lotus.ibator.struts2.scaffold.plugin;

import lotus.ibator.struts2.scaffold.plugin.util.GenerateUtil;

import org.apache.ibatis.ibator.api.IntrospectedColumn;

public class PropertiesGenerator extends ConfigGenerator {

	/**
	 * package.properties���� ����
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

		// �˻�
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

			// ���� Ȥ�� ��¥�϶� ���� �˻� ��� �߰�
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
	 * �ѱ��� ����ڸ� ���� package_ko.properties���� ����
	 */
	public String generateKoProperties() {
		StringBuffer document = new StringBuffer();
		document.append(dtoInstanceName + "." + listActionNameInput + ".title=" + dtoName + " ���");
		document.append("\n");
		document.append(dtoInstanceName + "." + createActionNameInput + ".title=" + dtoName + " ����");
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + readActionNameInput + ".title=" + dtoName + " ����");
			document.append("\n");
			document.append(dtoInstanceName + "." + updateActionNameInput + ".title=" + dtoName + " ����");
			document.append("\n");
		}

		// �˻�
		document.append(dtoInstanceName + ".search.submit=�˻�");
		document.append("\n");

		document.append(dtoInstanceName + "." + createActionNameInput + ".submit=�����ϱ�");
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + "." + updateActionNameInput + ".submit=�����ϱ�");
			document.append("\n");
		}
		document.append(dtoInstanceName + ".linkto." + listActionNameInput + "=������� ���ư���");
		document.append("\n");
		document.append(dtoInstanceName + ".linkto." + createActionNameInput + "=�� " + dtoName);
		document.append("\n");
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".linkto." + readActionNameInput + "=�������� ���ư���");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + updateActionNameInput + "=����");
			document.append("\n");
			document.append(dtoInstanceName + ".linkto." + deleteActionNameInput + "=����");
			document.append("\n");
		}
		document.append(dtoInstanceName + "." + listActionNameInput + ".count=����");
		document.append("\n");
		for (IntrospectedColumn column : columns) {
			document.append(dtoInstanceName + "." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()));
			document.append("\n");

			// ���� Ȥ�� ��¥�϶� ���� �˻� ��� �߰�
			if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
				"TIMESTAMP")) {
				document.append(dtoInstanceName + "." + column.getJavaProperty() + "From=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "�� ����");
				document.append("\n");

				document.append(dtoInstanceName + "." + column.getJavaProperty() + "To=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "�� ��");
				document.append("\n");
			}
		}
		for (IntrospectedColumn column : columns) {
			if (!column.isNullable()) {
				if (column.isStringColumn()) {
					document.append(dtoInstanceName + ".requiredstring." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "��/�� �� �ֽñ� �ٶ��ϴ�.");
					document.append("\n");
				} else {
					document.append(dtoInstanceName + ".required." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "��/�� �� �ֽñ� �ٶ��ϴ�.");
					document.append("\n");
				}
			}
			if (column.isStringColumn()) {
				document.append(dtoInstanceName + ".stringlength." + column.getJavaProperty() + "=" + GenerateUtil.toUpperCaseFirstLetter(column.getJavaProperty()) + "��/�� �ִ� " + new Integer(
					column.getLength() / 2).toString() + "���� �Դϴ�.");
				document.append("\n");
			}
		}
		if (primaryKeys.size() > 0) {
			document.append(dtoInstanceName + ".existent.primaryKey=�̹� �����ϴ� Primary Key �Դϴ�.");
			document.append("\n");
		}
		return GenerateUtil.unicode2UnicodeEsc(document.toString());
	}

}
