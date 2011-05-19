package lotus.ibator.struts2.scaffold.plugin;

import java.util.Map;

import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;

public class InfoViewsGenerator extends PropertiesGenerator {

	protected String sortShiddenViewFileName = "sort-shidden.jspf";
	protected String searchShiddenViewFileName = "search-shidden.jspf";
	protected String infoShiddenViewFileName = "info-shidden.jspf";

	protected String sortSparamViewFileName = "sort-sparam.jspf";
	protected String searchSparamViewFileName = "search-sparam.jspf";
	protected String infoSparamViewFileName = "info-sparam.jspf";

	protected String allShiddenViewFileName = "all-shidden.jspf";
	protected String forSortShiddenViewFileName = "for-sort-shidden.jspf";
	protected String forSearchShiddenViewFileName = "for-search-shidden.jspf";
	protected String forInfoShiddenViewFileName = "for-info-shidden.jspf";

	protected String allSparamViewFileName = "all-sparam.jspf";
	protected String forSortSparamViewFileName = "for-sort-sparam.jspf";
	protected String forSearchSparamViewFileName = "for-search-sparam.jspf";
	protected String forInfoSparamViewFileName = "for-info-sparam.jspf";

	/**
	 * SortShiddenView
	 */
	public String generateSortShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		AddListSortInfoFieldShidden(document, "sort");
		document.append("\n");
		AddListSortInfoFieldShidden(document, "order");
		document.append("\n");
		return document.toString();
	}

	/**
	 * SearchShiddenView
	 */
	public String generateSearchShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (IntrospectedColumn column : columns) {
			if (useSearch.contains(column.getJavaProperty())) {
				AddListSearchInfoFieldShidden(
					document,
					column.getJavaProperty());
				document.append("\n");

				if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
					"TIMESTAMP")) {
					AddListSearchInfoFieldShidden(
						document,
						column.getJavaProperty() + "From");
					document.append("\n");

					AddListSearchInfoFieldShidden(
						document,
						column.getJavaProperty() + "To");
					document.append("\n");
				}
			}
		}
		return document.toString();
	}

	/**
	 * InfoShiddenView
	 */
	public String generateInfoShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (String key : addInfo.keySet()) {
			AddListInfoFieldShidden(document, key);
			document.append("\n");
		}
		return document.toString();
	}

	/**
	 * Info2ShiddenView
	 */
	public String generateInfo2ShiddenView(
			Map<String, FullyQualifiedJavaType> addInfo) {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (String key : addInfo.keySet()) {
			AddListInfoFieldShidden(document, key);
			document.append("\n");
		}
		return document.toString();
	}

	private void AddListSortInfoFieldShidden(StringBuffer document, String name) {
		XmlElement shidden = new XmlElement("s:hidden");
		shidden.addAttribute(new Attribute(
			"name",
			listSortInfoInstanceName + "." + name));
		document.append(shidden.getFormattedContent(0));
	}

	private void AddListSearchInfoFieldShidden(
			StringBuffer document,
			String name) {
		XmlElement shidden = new XmlElement("s:hidden");
		shidden.addAttribute(new Attribute(
			"name",
			listSearchInfoInstanceName + "." + name));
		document.append(shidden.getFormattedContent(0));
	}

	private void AddListInfoFieldShidden(StringBuffer document, String name) {
		XmlElement shidden = new XmlElement("s:hidden");
		shidden.addAttribute(new Attribute(
			"name",
			listInfoInstanceName + "." + name));
		document.append(shidden.getFormattedContent(0));
	}

	/**
	 * SortSparamView
	 */
	public String generateSortSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		AddListSortInfoFieldSparam(document, "sort");
		document.append("\n");
		AddListSortInfoFieldSparam(document, "order");
		document.append("\n");
		return document.toString();
	}

	/**
	 * SearchSparamView
	 */
	public String generateSearchSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (IntrospectedColumn column : columns) {
			if (useSearch.contains(column.getJavaProperty())) {
				AddListSearchInfoFieldSparam(document, column.getJavaProperty());
				document.append("\n");

				if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
					"TIMESTAMP")) {
					AddListSearchInfoFieldSparam(
						document,
						column.getJavaProperty() + "From");
					document.append("\n");

					AddListSearchInfoFieldSparam(
						document,
						column.getJavaProperty() + "To");
					document.append("\n");
				}
			}
		}
		return document.toString();
	}

	/**
	 * InfoSparamView
	 */
	public String generateInfoSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (String key : addInfo.keySet()) {
			AddListInfoFieldSparam(document, key);
			document.append("\n");
		}
		return document.toString();
	}

	/**
	 * Info2SparamView
	 */
	public String generateInfo2SparamView(
			Map<String, FullyQualifiedJavaType> addInfo) {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());
		for (String key : addInfo.keySet()) {
			AddListInfoFieldSparam(document, key);
			document.append("\n");
		}
		return document.toString();
	}

	private void AddListSortInfoFieldSparam(StringBuffer document, String name) {
		XmlElement sparam = new XmlElement("s:param");
		sparam.addAttribute(new Attribute(
			"name",
			listSortInfoInstanceName + "." + name));
		sparam.addAttribute(new Attribute(
			"value",
			listSortInfoInstanceName + "." + name));
		document.append(sparam.getFormattedContent(0));
	}

	private void AddListSearchInfoFieldSparam(StringBuffer document, String name) {
		XmlElement sparam = new XmlElement("s:param");
		sparam.addAttribute(new Attribute(
			"name",
			listSearchInfoInstanceName + "." + name));
		sparam.addAttribute(new Attribute(
			"value",
			listSearchInfoInstanceName + "." + name));
		document.append(sparam.getFormattedContent(0));
	}

	private void AddListInfoFieldSparam(StringBuffer document, String name) {
		XmlElement sparam = new XmlElement("s:param");
		sparam.addAttribute(new Attribute(
			"name",
			listInfoInstanceName + "." + name));
		sparam.addAttribute(new Attribute(
			"value",
			listInfoInstanceName + "." + name));
		document.append(sparam.getFormattedContent(0));
	}

	protected String getJspInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("<%@ page language=\"java\" contentType=\"text/html; charset=" + charset + "\" pageEncoding=\"" + charset + "\"%>");
		sb.append("\n");
		sb.append("<%@ taglib prefix=\"s\" uri=\"/struts-tags\"%>");
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * AllShiddenView
	 */
	public String generateAllShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortShiddenViewFileName);
		}
		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchShiddenViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoShiddenViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-shidden.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * AllSparamView
	 */
	public String generateAllSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortSparamViewFileName);
		}
		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchSparamViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoSparamViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-sparam.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * ForSortShiddenView
	 */
	public String generateForSortShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchShiddenViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoShiddenViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-shidden.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * ForSortSparamView
	 */
	public String generateForSortSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchSparamViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoSparamViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-sparam.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * ForSearchShiddenView
	 */
	public String generateForSearchShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortShiddenViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoShiddenViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-shidden.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * ForSearchSparamView
	 */
	public String generateForSearchSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortSparamViewFileName);
		}
		if (addInfo != null) {
			addOtherInfoFileInclude(document, infoSparamViewFileName);
		}
		if (addInfo2 != null) {
			for (String key : addInfo2.keySet()) {
				addOtherInfoFileInclude(document, key + "-sparam.jsp");
			}
		}

		return document.toString();
	}

	/**
	 * ForInfoShiddenView
	 */
	public String generateForInfoShiddenView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortShiddenViewFileName);
		}
		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchShiddenViewFileName);
		}

		return document.toString();
	}

	/**
	 * ForInfoSparamView
	 */
	public String generateForInfoSparamView() {
		StringBuffer document = new StringBuffer();
		document.append(getJspInfo());

		if (useSort != null) {
			addOtherInfoFileInclude(document, sortSparamViewFileName);
		}
		if (useSearch != null) {
			addOtherInfoFileInclude(document, searchSparamViewFileName);
		}

		return document.toString();
	}

	private void addOtherInfoFileInclude(StringBuffer document, String fileName) {
		document.append("<%@ include file=\"" + fileName + "\" %>\n");
	}

}
