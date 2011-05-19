package lotus.ibator.struts2.scaffold.plugin;

import org.apache.ibatis.ibator.api.GeneratedXmlFile;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.Document;
import org.apache.ibatis.ibator.api.dom.xml.TextElement;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;

public class ValidationsGenerator extends ActionsGenerator {

	/**
	 * Create Validation 파일을 생성하는 함수
	 */
	public GeneratedXmlFile generateCreateValidation() {
		return new GeneratedXmlFile(
			generateCuValidation(),
			createActionName + "-validation.xml",
			actionPackage,
			targetProject,
			false);
	}

	/**
	 * Update Validation 파일을 생성하는 함수
	 */
	public GeneratedXmlFile generateUpdateValidation() {
		return new GeneratedXmlFile(
			generateCuValidation(),
			updateActionName + "-validation.xml",
			actionPackage,
			targetProject,
			false);
	}

	private Document generateCuValidation() {
		Document document = new Document(
			"-//OpenSymphony Group//XWork Validator 1.0.2//EN",
			"http://www.opensymphony.com/xwork/xwork-validator-1.0.2.dtd");

		String name = "validators";
		XmlElement root = new XmlElement(name);
		document.setRootElement(root);

		XmlElement validator;
		XmlElement param;
		XmlElement message;

		for (IntrospectedColumn column : columns) {
			if (!column.isNullable()) {
				if (column.isStringColumn()) {
					// validator
					validator = new XmlElement("validator");
					validator.addAttribute(new Attribute(
						"type",
						"requiredstring"));

					param = new XmlElement("param");
					param.addAttribute(new Attribute("name", "fieldName"));
					param.addElement(new TextElement(column.getJavaProperty()));
					validator.addElement(param);

					message = new XmlElement("message");

					if (isCreateProperties) {
						message.addAttribute(new Attribute(
							"key",
							dtoInstanceName + ".requiredstring." + column.getJavaProperty()));
					} else {
						message.addElement(new TextElement(
							dtoInstanceName + ".requiredstring." + column.getJavaProperty()));
					}

					validator.addElement(message);

					root.addElement(validator);
				} else {
					// validator
					validator = new XmlElement("validator");
					validator.addAttribute(new Attribute("type", "required"));

					param = new XmlElement("param");
					param.addAttribute(new Attribute("name", "fieldName"));
					param.addElement(new TextElement(column.getJavaProperty()));
					validator.addElement(param);

					message = new XmlElement("message");

					if (isCreateProperties) {
						message.addAttribute(new Attribute(
							"key",
							dtoInstanceName + ".required." + column.getJavaProperty()));
					} else {
						message.addElement(new TextElement(
							dtoInstanceName + ".required." + column.getJavaProperty()));
					}

					validator.addElement(message);

					root.addElement(validator);
				}
			}

			if (column.isStringColumn()) {
				// validator
				validator = new XmlElement("validator");
				validator.addAttribute(new Attribute("type", "stringlength"));

				param = new XmlElement("param");
				param.addAttribute(new Attribute("name", "fieldName"));
				param.addElement(new TextElement(column.getJavaProperty()));
				validator.addElement(param);

				param = new XmlElement("param");
				param.addAttribute(new Attribute("name", "maxLength"));
				param.addElement(new TextElement(new Integer(
					column.getLength() / 2).toString()));
				validator.addElement(param);

				message = new XmlElement("message");

				if (isCreateProperties) {
					message.addAttribute(new Attribute(
						"key",
						dtoInstanceName + ".stringlength." + column.getJavaProperty()));
				} else {
					message.addElement(new TextElement(
						dtoInstanceName + ".stringlength." + column.getJavaProperty()));
				}

				validator.addElement(message);

				root.addElement(validator);
			}
		}
		return document;
	}

}
