package lotus.ibator.struts2.scaffold.plugin.util;

import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;

public class GenerateUtil {

	/**
	 * Ÿ�� ����
	 */
	static public FullyQualifiedJavaType generateType(
			String packageStr,
			String name) {
		// ��Ű���� + �̸�
		return new FullyQualifiedJavaType(packageStr + "." + name);
	}

	/**
	 * Ŭ���� ����
	 */
	static public TopLevelClass generateClass(FullyQualifiedJavaType type) {
		// �⺻���� public
		return generateClass(type, "public");
	}

	/**
	 * Ŭ���� ����
	 */
	static public TopLevelClass generateClass(
			FullyQualifiedJavaType type,
			String visibility) {
		TopLevelClass javaClass = new TopLevelClass(type);
		javaClass.setVisibility(getVisibility(visibility));
		return javaClass;
	}

	/**
	 * ���ڿ��κ��� Visibility ������ ��ȯ
	 */
	static private JavaVisibility getVisibility(String visibility) {
		if (visibility.equals("public")) {
			return JavaVisibility.PUBLIC;
		} else if (visibility.equals("private")) {
			return JavaVisibility.PRIVATE;
		} else if (visibility.equals("protected")) {
			return JavaVisibility.PROTECTED;
		}
		return null;
	}

	/**
	 * Property ����
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type) {
		// Getter Setter �޼ҵ带 ������ݴϴ�.
		settingProperty(target, name, type, true, true);
	}

	/**
	 * Property ����
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter) {
		target.addImportedType(type);
		target.addField(generateField(name, type)); // �ʵ� ����
		if (isGenerateGetter) { // Getter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property ���� (With New)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			boolean isNew) {
		target.addImportedType(type);
		target.addField(generateField(name, type, isNew)); // �ʵ� ���� (new)
		if (isGenerateGetter) { // Getter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property ���� (With New, Width Visibility)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			String visibility,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			boolean isNew) {
		target.addImportedType(type);
		// �ʵ� ���� (new)
		target.addField(generateField(name, visibility, type, isNew));
		if (isGenerateGetter) { // Getter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property ���� (With Initialization)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			String initialization) {
		target.addImportedType(type);
		// �ʵ� ���� (�ʱⰪ ����)
		target.addField(generateField(name, type, initialization));
		if (isGenerateGetter) { // Getter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property ���� (With Initialization, Width Visibility)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			String visibility,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			String initialization) {
		target.addImportedType(type);
		// �ʵ� ���� (�ʱⰪ ����)
		target.addField(generateField(name, visibility, type, initialization));
		if (isGenerateGetter) { // Getter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter �޼ҵ� ����
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * �ʵ� ����
	 */
	static public Field generateField(String name, FullyQualifiedJavaType type) {
		// �ʱⰪ�� ���� �ʵ� ����
		return generateField(name, type, null);
	}

	/**
	 * �ʵ� ���� (�⺻������ private)
	 */
	static public Field generateField(
			String name,
			FullyQualifiedJavaType type,
			String initialization) {
		// �⺻���� private
		return generateField(name, "private", type, initialization);
	}

	/**
	 * �ʵ� ���� (�ʱⰪ ����)
	 */
	static public Field generateField(
			String name,
			String visibility,
			FullyQualifiedJavaType type,
			String initialization) {
		return generateField(
			name,
			visibility,
			false,
			false,
			type,
			initialization);
	}

	/**
	 * �ʵ� ���� (��� ���� ����)
	 */
	static public Field generateField(
			String name,
			String visibility,
			boolean isStatic,
			boolean isFinal,
			FullyQualifiedJavaType type,
			String initialization) {
		Field field = new Field();
		field.setName(name); // �̸� ����
		field.setVisibility(getVisibility(visibility)); // Visibility ����
		field.setStatic(isStatic); // static ����
		field.setFinal(isFinal); // final ����
		field.setType(type); // Ÿ�� ����
		field.setInitializationString(initialization); // �ʱⰪ ����
		return field;
	}

	/**
	 * �ʵ� ���� (�⺻������ private, With New)
	 */
	static public Field generateField(
			String name,
			FullyQualifiedJavaType type,
			boolean isNew) {
		// �⺻���� private, new
		return generateField(name, "private", type, isNew);
	}

	/**
	 * �ʵ� ���� (With New)
	 */
	static public Field generateField(
			String name,
			String visibility,
			FullyQualifiedJavaType type,
			boolean isNew) {
		// �⺻���� private, new
		return generateField(name, visibility, false, false, type, isNew);
	}

	/**
	 * �ʵ� ���� (With New)
	 */
	static public Field generateField(
			String name,
			String visibility,
			boolean isStatic,
			boolean isFinal,
			FullyQualifiedJavaType type,
			boolean isNew) {
		String initialization = null;
		if (isNew) {
			// new ������ ������
			initialization = "new " + type.getShortName() + "()";
		}
		return generateField(
			name,
			visibility,
			isStatic,
			isFinal,
			type,
			initialization);
	}

	/**
	 * �޼ҵ� ����
	 */
	static public Method generateMethod(String name) {
		// �޼ҵ� ���� (void)
		return generateMethod(name, null);
	}

	/**
	 * �޼ҵ� ���� (�⺻������ public)
	 */
	static public Method generateMethod(String name, FullyQualifiedJavaType type) {
		// �⺻���� public
		return generateMethod(name, "public", type);
	}

	/**
	 * �޼ҵ� ����
	 */
	static public Method generateMethod(
			String name,
			String visibility,
			FullyQualifiedJavaType type) {
		// static�� �ƴϰ� final�� �ƴ� �޼ҵ� ����
		return generateMethod(name, visibility, false, false, type);
	}

	/**
	 * �޼ҵ� ���� (��� ���� ����)
	 */
	static public Method generateMethod(
			String name,
			String visibility,
			boolean isStatic,
			boolean isFinal,
			FullyQualifiedJavaType type) {
		Method method = new Method();
		method.setName(name); // �̸� ����
		method.setVisibility(getVisibility(visibility)); // Visibility ����
		method.setStatic(isStatic); // static ����
		method.setFinal(isFinal); // final ����
		method.setReturnType(type); // Ÿ�� ����
		return method;
	}

	/**
	 * ù ���ڸ� �빮�ڷ�
	 */
	static public String toUpperCaseFirstLetter(String str) {
		StringBuilder sb = new StringBuilder();

		sb.append(str);
		if (Character.isLowerCase(sb.charAt(0))) {
			// �빮�ڷ�
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 * ù ���ڸ� �ҹ��ڷ�
	 */
	static public String toLowerCaseFirstLetter(String str) {
		StringBuilder sb = new StringBuilder();

		sb.append(str);
		if (Character.isUpperCase(sb.charAt(0))) {
			// �ҹ��ڷ�
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 * �����ڵ� ���ڵ�
	 */
	public static String unicode2UnicodeEsc(String uniStr) {
		StringBuffer ret = new StringBuffer();
		if (uniStr == null)
			return null;
		int maxLoop = uniStr.length();
		for (int i = 0; i < maxLoop; i++) {
			char character = uniStr.charAt(i);
			if (character <= '\177') {
				ret.append(character);
				continue;
			}
			ret.append("\\u");
			String hexStr = Integer.toHexString(character);
			int zeroCount = 4 - hexStr.length();
			for (int j = 0; j < zeroCount; j++)
				ret.append('0');

			ret.append(hexStr);
		}

		return ret.toString();
	}

}
