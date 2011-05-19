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
	 * 타입 생성
	 */
	static public FullyQualifiedJavaType generateType(
			String packageStr,
			String name) {
		// 패키지명 + 이름
		return new FullyQualifiedJavaType(packageStr + "." + name);
	}

	/**
	 * 클래스 생성
	 */
	static public TopLevelClass generateClass(FullyQualifiedJavaType type) {
		// 기본으로 public
		return generateClass(type, "public");
	}

	/**
	 * 클래스 생성
	 */
	static public TopLevelClass generateClass(
			FullyQualifiedJavaType type,
			String visibility) {
		TopLevelClass javaClass = new TopLevelClass(type);
		javaClass.setVisibility(getVisibility(visibility));
		return javaClass;
	}

	/**
	 * 문자열로부터 Visibility 설정을 반환
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
	 * Property 생성
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type) {
		// Getter Setter 메소드를 만들어줍니다.
		settingProperty(target, name, type, true, true);
	}

	/**
	 * Property 생성
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter) {
		target.addImportedType(type);
		target.addField(generateField(name, type)); // 필드 생성
		if (isGenerateGetter) { // Getter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property 생성 (With New)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			boolean isNew) {
		target.addImportedType(type);
		target.addField(generateField(name, type, isNew)); // 필드 생성 (new)
		if (isGenerateGetter) { // Getter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property 생성 (With New, Width Visibility)
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
		// 필드 생성 (new)
		target.addField(generateField(name, visibility, type, isNew));
		if (isGenerateGetter) { // Getter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property 생성 (With Initialization)
	 */
	static public void settingProperty(
			TopLevelClass target,
			String name,
			FullyQualifiedJavaType type,
			boolean isGenerateGetter,
			boolean isGenerateSetter,
			String initialization) {
		target.addImportedType(type);
		// 필드 생성 (초기값 있음)
		target.addField(generateField(name, type, initialization));
		if (isGenerateGetter) { // Getter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * Property 생성 (With Initialization, Width Visibility)
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
		// 필드 생성 (초기값 있음)
		target.addField(generateField(name, visibility, type, initialization));
		if (isGenerateGetter) { // Getter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getGetterMethodName(
				name,
				type), type);
			method.addBodyLine("return " + name + ";");
			target.addMethod(method);
		}
		if (isGenerateSetter) { // Setter 메소드 생성
			Method method = generateMethod(JavaBeansUtil.getSetterMethodName(name));
			method.addParameter(new Parameter(type, name));
			method.addBodyLine("this." + name + " = " + name + ";");
			target.addMethod(method);
		}
	}

	/**
	 * 필드 생성
	 */
	static public Field generateField(String name, FullyQualifiedJavaType type) {
		// 초기값이 없는 필드 생성
		return generateField(name, type, null);
	}

	/**
	 * 필드 생성 (기본적으로 private)
	 */
	static public Field generateField(
			String name,
			FullyQualifiedJavaType type,
			String initialization) {
		// 기본으로 private
		return generateField(name, "private", type, initialization);
	}

	/**
	 * 필드 생성 (초기값 있음)
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
	 * 필드 생성 (모든 정보 포함)
	 */
	static public Field generateField(
			String name,
			String visibility,
			boolean isStatic,
			boolean isFinal,
			FullyQualifiedJavaType type,
			String initialization) {
		Field field = new Field();
		field.setName(name); // 이름 설정
		field.setVisibility(getVisibility(visibility)); // Visibility 설정
		field.setStatic(isStatic); // static 설정
		field.setFinal(isFinal); // final 설정
		field.setType(type); // 타입 설정
		field.setInitializationString(initialization); // 초기값 선언
		return field;
	}

	/**
	 * 필드 생성 (기본적으로 private, With New)
	 */
	static public Field generateField(
			String name,
			FullyQualifiedJavaType type,
			boolean isNew) {
		// 기본으로 private, new
		return generateField(name, "private", type, isNew);
	}

	/**
	 * 필드 생성 (With New)
	 */
	static public Field generateField(
			String name,
			String visibility,
			FullyQualifiedJavaType type,
			boolean isNew) {
		// 기본으로 private, new
		return generateField(name, visibility, false, false, type, isNew);
	}

	/**
	 * 필드 생성 (With New)
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
			// new 생성자 붙혀줌
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
	 * 메소드 생성
	 */
	static public Method generateMethod(String name) {
		// 메소드 생성 (void)
		return generateMethod(name, null);
	}

	/**
	 * 메소드 생성 (기본적으로 public)
	 */
	static public Method generateMethod(String name, FullyQualifiedJavaType type) {
		// 기본으로 public
		return generateMethod(name, "public", type);
	}

	/**
	 * 메소드 생성
	 */
	static public Method generateMethod(
			String name,
			String visibility,
			FullyQualifiedJavaType type) {
		// static이 아니고 final이 아닌 메소드 생성
		return generateMethod(name, visibility, false, false, type);
	}

	/**
	 * 메소드 생성 (모든 정보 포함)
	 */
	static public Method generateMethod(
			String name,
			String visibility,
			boolean isStatic,
			boolean isFinal,
			FullyQualifiedJavaType type) {
		Method method = new Method();
		method.setName(name); // 이름 설정
		method.setVisibility(getVisibility(visibility)); // Visibility 설정
		method.setStatic(isStatic); // static 설정
		method.setFinal(isFinal); // final 설정
		method.setReturnType(type); // 타입 설정
		return method;
	}

	/**
	 * 첫 글자를 대문자로
	 */
	static public String toUpperCaseFirstLetter(String str) {
		StringBuilder sb = new StringBuilder();

		sb.append(str);
		if (Character.isLowerCase(sb.charAt(0))) {
			// 대문자로
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 * 첫 글자를 소문자로
	 */
	static public String toLowerCaseFirstLetter(String str) {
		StringBuilder sb = new StringBuilder();

		sb.append(str);
		if (Character.isUpperCase(sb.charAt(0))) {
			// 소문자로
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 * 유니코드 인코딩
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
