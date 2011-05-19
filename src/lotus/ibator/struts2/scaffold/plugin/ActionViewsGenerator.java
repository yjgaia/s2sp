package lotus.ibator.struts2.scaffold.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.Document;
import org.apache.ibatis.ibator.api.dom.xml.TextElement;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;

public class ActionViewsGenerator extends InfoViewsGenerator {

	/**
	 * ListView
	 */
	public String generateListView() {
		Document document = new Document(
			"-//W3C//DTD HTML 4.01 Transitional//EN",
			"http://www.w3.org/TR/html4/loose.dtd");
		XmlElement root = new XmlElement("html");

		// body
		XmlElement body = new XmlElement("body");
		addHeadAndBodyAndTitle(root, body, listActionNameInput, true);
		document.setRootElement(root);

		// count
		XmlElement countText = new XmlElement("s:text");
		countText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + "." + listActionNameInput + ".count"));
		body.addElement(countText);
		XmlElement count = new XmlElement("s:property");
		count.addAttribute(new Attribute("value", dtoInstanceName + "Count"));
		body.addElement(count);

		// table
		XmlElement table = new XmlElement("table");
		table.addAttribute(new Attribute("border", "1"));
		body.addElement(table);

		XmlElement tr = new XmlElement("tr");
		for (IntrospectedColumn column : columns) {
			if (!column.isBLOBColumn() || primaryKeys.size() == 0) {
				XmlElement th = new XmlElement("th");
				XmlElement fieldTitleText = new XmlElement("s:text");
				fieldTitleText.addAttribute(new Attribute(
					"name",
					dtoInstanceName + "." + column.getJavaProperty()));

				if (useSort != null && useSort.contains(column.getJavaProperty())) { // 정렬
					String columnName = column.getActualColumnName().toLowerCase();

					XmlElement surl = new XmlElement("s:url");
					surl.addAttribute(new Attribute("namespace", namespace));
					surl.addAttribute(new Attribute(
						"action",
						listActionNameInput));
					XmlElement sparam = new XmlElement("s:param");
					sparam.addAttribute(new Attribute(
						"name",
						listSortInfoInstanceName + ".sort"));
					sparam.addAttribute(new Attribute(
						"value",
						"%{'" + columnName + "'}"));
					surl.addElement(sparam);
					sparam = new XmlElement("s:param");
					sparam.addAttribute(new Attribute(
						"name",
						listSortInfoInstanceName + ".order"));
					sparam.addAttribute(new Attribute(
						"value",
						"%{(" + listSortInfoInstanceName + ".sort == '" + columnName + "' && " + listSortInfoInstanceName + ".order == 'asc') ? 'desc' : 'asc'}"));
					surl.addElement(sparam);

					// 정보 삽입
					addJspIncludePageTag(surl, forSortSparamViewFileName);

					XmlElement linkToList = new XmlElement("a");
					linkToList.addAttribute(new Attribute(
						"href",
						surl.getFormattedContent(0)));
					linkToList.addElement(fieldTitleText);
					th.addElement(linkToList);
				} else {
					th.addElement(fieldTitleText);
				}

				tr.addElement(th);
			}
		}
		table.addElement(tr);
		XmlElement siterator = new XmlElement("s:iterator");
		siterator.addAttribute(new Attribute("value", dtoListInstanceName));
		tr = new XmlElement("tr");
		for (IntrospectedColumn column : columns) {
			if (!column.isBLOBColumn() || primaryKeys.size() == 0) {
				boolean isPK = false;
				for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
					if (primaryKeyColumn.getJavaProperty().equals(
						column.getJavaProperty())) {
						isPK = true;
						break;
					}
				}
				XmlElement td = new XmlElement("td");
				XmlElement sfield = new XmlElement("s:property");
				sfield.addAttribute(new Attribute(
					"value",
					column.getJavaProperty()));
				if (isPK) {
					XmlElement surl = new XmlElement("s:url");
					surl.addAttribute(new Attribute("namespace", namespace));
					surl.addAttribute(new Attribute(
						"action",
						readActionNameInput));
					for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
						XmlElement sparam = new XmlElement("s:param");
						sparam.addAttribute(new Attribute(
							"name",
							primaryKeyColumn.getJavaProperty()));
						sparam.addAttribute(new Attribute(
							"value",
							primaryKeyColumn.getJavaProperty()));
						surl.addElement(sparam);
					}
					/* addAllSparamJspIncludePageTag */
					if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
						addJspIncludePageTag(surl, allSparamViewFileName);
					}

					XmlElement linkToRead = new XmlElement("a");
					linkToRead.addAttribute(new Attribute(
						"href",
						surl.getFormattedContent(0)));
					linkToRead.addElement(sfield);
					td.addElement(linkToRead);
				} else {
					td.addElement(sfield);
				}
				tr.addElement(td);
			}
		}
		siterator.addElement(tr);
		table.addElement(siterator);

		if (useSearch != null && useSearch.size() > 0) { // 검색 폼
			// s:form
			XmlElement sform = new XmlElement("s:form");
			sform.addAttribute(new Attribute("action", listActionNameInput));
			sform.addAttribute(new Attribute("method", "get"));
			body.addElement(sform);
			for (IntrospectedColumn column : columns) {
				if (!column.isBLOBColumn() && useSearch.contains(column.getJavaProperty())) {
					XmlElement stextfield = new XmlElement("s:textfield");
					if (isCreateProperties) {
						stextfield.addAttribute(new Attribute(
							"label",
							"%{getText('" + dtoInstanceName + "." + column.getJavaProperty() + "')}"));
					} else {
						stextfield.addAttribute(new Attribute(
							"label",
							dtoInstanceName + "." + column.getJavaProperty()));
					}
					stextfield.addAttribute(new Attribute(
						"name",
						listSearchInfoInstanceName + "." + column.getJavaProperty()));
					sform.addElement(stextfield);

					// 숫자 혹은 날짜일때 범위 검색 기능 추가
					if (column.getJdbcTypeName().equals("DECIMAL") || column.getJdbcTypeName().equals(
						"TIMESTAMP")) {
						stextfield = new XmlElement("s:textfield");
						if (isCreateProperties) {
							stextfield.addAttribute(new Attribute(
								"label",
								"%{getText('" + dtoInstanceName + "." + column.getJavaProperty() + "From')}"));
						} else {
							stextfield.addAttribute(new Attribute(
								"label",
								dtoInstanceName + "." + column.getJavaProperty() + "From"));
						}
						stextfield.addAttribute(new Attribute(
							"name",
							listSearchInfoInstanceName + "." + column.getJavaProperty() + "From"));
						sform.addElement(stextfield);

						stextfield = new XmlElement("s:textfield");
						if (isCreateProperties) {
							stextfield.addAttribute(new Attribute(
								"label",
								"%{getText('" + dtoInstanceName + "." + column.getJavaProperty() + "To')}"));
						} else {
							stextfield.addAttribute(new Attribute(
								"label",
								dtoInstanceName + "." + column.getJavaProperty() + "To"));
						}
						stextfield.addAttribute(new Attribute(
							"name",
							listSearchInfoInstanceName + "." + column.getJavaProperty() + "To"));
						sform.addElement(stextfield);
					}
				}
			}

			// 정보 삽입
			addJspIncludePageTag(sform, forSearchShiddenViewFileName);

			// s:submit
			XmlElement ssubmit = new XmlElement("s:submit");
			if (isCreateProperties) {
				ssubmit.addAttribute(new Attribute(
					"value",
					"%{getText('" + dtoInstanceName + ".search.submit')}"));
			} else {
				ssubmit.addAttribute(new Attribute(
					"value",
					dtoInstanceName + ".search.submit"));
			}

			sform.addElement(ssubmit);
		}

		XmlElement ul = new XmlElement("ul");
		XmlElement li = new XmlElement("li");
		// linkToCreate
		XmlElement linkToCreate = new XmlElement("a");
		XmlElement surl = new XmlElement("s:url");
		surl.addAttribute(new Attribute("namespace", namespace));
		surl.addAttribute(new Attribute(
			"action",
			createActionNameInput + "-input"));

		if (useSearch != null) {
			for (IntrospectedColumn column : columns) {
				if (!column.isBLOBColumn() && useSearch.contains(column.getJavaProperty())) {
					XmlElement sparam = new XmlElement("s:param");
					sparam.addAttribute(new Attribute(
						"name",
						column.getJavaProperty()));
					sparam.addAttribute(new Attribute(
						"value",
						listSearchInfoInstanceName + "." + column.getJavaProperty()));
					surl.addElement(sparam);
				}
			}
		}

		/* addAllSparamJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(surl, allSparamViewFileName);
		}

		linkToCreate.addAttribute(new Attribute(
			"href",
			surl.getFormattedContent(0)));
		XmlElement linkToCreateText = new XmlElement("s:text");
		linkToCreateText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + ".linkto." + createActionNameInput));
		linkToCreate.addElement(linkToCreateText);

		li.addElement(linkToCreate);
		ul.addElement(li);
		body.addElement(ul);

		return getJspFromXml(document);
	}

	/**
	 * InputView
	 */
	public String generateInputView(String action) {
		Document document = new Document(
			"-//W3C//DTD HTML 4.01 Transitional//EN",
			"http://www.w3.org/TR/html4/loose.dtd");

		XmlElement root = new XmlElement("html");
		document.setRootElement(root);

		// body
		XmlElement body = new XmlElement("body");
		addHeadAndBodyAndTitle(root, body, action, false);

		// s:form
		XmlElement sform = new XmlElement("s:form");
		sform.addAttribute(new Attribute("action", action));
		body.addElement(sform);

		for (IntrospectedColumn column : columns) {
			XmlElement sfield;

			boolean isPK = false;
			if (action.equals(updateActionNameInput)) {
				for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
					if (primaryKeyColumn.getJavaProperty().equals(
						column.getJavaProperty())) {
						isPK = true;
						break;
					}
				}
			}
			if (isPK) {
				sfield = new XmlElement("s:hidden");
			} else if (column.isBLOBColumn()) {
				sfield = new XmlElement("s:textarea");
			} else {
				sfield = new XmlElement("s:textfield");
			}
			if (!isPK) {
				if (isCreateProperties) {
					sfield.addAttribute(new Attribute(
						"label",
						"%{getText('" + dtoInstanceName + "." + column.getJavaProperty() + "')}"));
				} else {
					sfield.addAttribute(new Attribute(
						"label",
						dtoInstanceName + "." + column.getJavaProperty()));
				}
			}
			sfield.addAttribute(new Attribute("name", column.getJavaProperty()));
			if (action.equals(updateActionNameInput)) {
				sfield.addAttribute(new Attribute(
					"value",
					"%{" + dtoInstanceName + "." + column.getJavaProperty() + "}"));
			}
			sform.addElement(sfield);
		}

		/* addAllShiddenJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(sform, allShiddenViewFileName);
		}

		// s:submit
		XmlElement ssubmit = new XmlElement("s:submit");
		if (isCreateProperties) {
			ssubmit.addAttribute(new Attribute(
				"value",
				"%{getText('" + dtoInstanceName + "." + action + ".submit')}"));
		} else {
			ssubmit.addAttribute(new Attribute(
				"value",
				dtoInstanceName + "." + action));
		}
		sform.addElement(ssubmit);

		XmlElement ul = new XmlElement("ul");
		XmlElement li;
		if (action.equals(updateActionNameInput)) {
			li = new XmlElement("li");
			// linkToRead
			XmlElement surl = new XmlElement("s:url");
			surl.addAttribute(new Attribute("namespace", namespace));
			surl.addAttribute(new Attribute("action", readActionNameInput));
			for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
				XmlElement sparam = new XmlElement("s:param");
				sparam.addAttribute(new Attribute(
					"name",
					primaryKeyColumn.getJavaProperty()));
				sparam.addAttribute(new Attribute(
					"value",
					dtoInstanceName + "." + primaryKeyColumn.getJavaProperty()));
				surl.addElement(sparam);
			}

			/* addAllSparamJspIncludePageTag */
			if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
				addJspIncludePageTag(surl, allSparamViewFileName);
			}

			XmlElement linkToRead = new XmlElement("a");
			linkToRead.addAttribute(new Attribute(
				"href",
				surl.getFormattedContent(0)));
			XmlElement linkToReadText = new XmlElement("s:text");
			linkToReadText.addAttribute(new Attribute(
				"name",
				dtoInstanceName + ".linkto." + readActionNameInput));
			linkToRead.addElement(linkToReadText);

			li.addElement(linkToRead);
			ul.addElement(li);
		}

		li = new XmlElement("li");
		// linkToList
		XmlElement linkToList = new XmlElement("a");
		XmlElement surl = new XmlElement("s:url");
		surl.addAttribute(new Attribute("namespace", namespace));
		surl.addAttribute(new Attribute("action", listActionNameInput));

		/* addAllSparamJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(surl, allSparamViewFileName);
		}

		linkToList.addAttribute(new Attribute(
			"href",
			surl.getFormattedContent(0)));
		XmlElement linkToListText = new XmlElement("s:text");
		linkToListText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + ".linkto." + listActionNameInput));
		linkToList.addElement(linkToListText);

		li.addElement(linkToList);
		ul.addElement(li);
		body.addElement(ul);

		return getJspFromXml(document);
	}

	/**
	 * ReadView
	 */
	public String generateReadView() {
		Document document = new Document(
			"-//W3C//DTD HTML 4.01 Transitional//EN",
			"http://www.w3.org/TR/html4/loose.dtd");

		XmlElement root = new XmlElement("html");
		document.setRootElement(root);

		// body
		XmlElement body = new XmlElement("body");
		addHeadAndBodyAndTitle(root, body, readActionNameInput, false);

		// table
		XmlElement table = new XmlElement("table");
		body.addElement(table);

		for (IntrospectedColumn column : columns) {
			XmlElement tr = new XmlElement("tr");
			XmlElement td = new XmlElement("td");
			XmlElement fieldTitleText = new XmlElement("s:text");
			fieldTitleText.addAttribute(new Attribute(
				"name",
				dtoInstanceName + "." + column.getJavaProperty()));
			td.addElement(fieldTitleText);
			td.addElement(new TextElement(":"));
			tr.addElement(td);
			td = new XmlElement("td");
			XmlElement sfield = new XmlElement("s:property");
			sfield.addAttribute(new Attribute(
				"value",
				dtoInstanceName + "." + column.getJavaProperty()));
			td.addElement(sfield);
			tr.addElement(td);
			table.addElement(tr);
		}

		XmlElement ul = new XmlElement("ul");
		XmlElement li = new XmlElement("li");

		// linkToList
		XmlElement linkToList = new XmlElement("a");
		XmlElement surl = new XmlElement("s:url");
		surl.addAttribute(new Attribute("namespace", namespace));
		surl.addAttribute(new Attribute("action", listActionNameInput));

		/* addAllSparamJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(surl, allSparamViewFileName);
		}

		linkToList.addAttribute(new Attribute(
			"href",
			surl.getFormattedContent(0)));
		XmlElement linkToListText = new XmlElement("s:text");
		linkToListText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + ".linkto." + listActionNameInput));
		linkToList.addElement(linkToListText);
		li.addElement(linkToList);
		ul.addElement(li);
		body.addElement(ul);

		ul = new XmlElement("ul");
		li = new XmlElement("li");

		// linkToUpdateInput
		surl = new XmlElement("s:url");
		surl.addAttribute(new Attribute("namespace", namespace));
		surl.addAttribute(new Attribute(
			"action",
			updateActionNameInput + "-input"));
		for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
			XmlElement sparam = new XmlElement("s:param");
			sparam.addAttribute(new Attribute(
				"name",
				primaryKeyColumn.getJavaProperty()));
			sparam.addAttribute(new Attribute(
				"value",
				dtoInstanceName + "." + primaryKeyColumn.getJavaProperty()));
			surl.addElement(sparam);
		}

		/* addAllSparamJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(surl, allSparamViewFileName);
		}

		XmlElement linkToUpdateInput = new XmlElement("a");
		linkToUpdateInput.addAttribute(new Attribute(
			"href",
			surl.getFormattedContent(0)));
		XmlElement linkToUpdateInputText = new XmlElement("s:text");
		linkToUpdateInputText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + ".linkto." + updateActionNameInput));
		linkToUpdateInput.addElement(linkToUpdateInputText);
		li.addElement(linkToUpdateInput);
		ul.addElement(li);
		li = new XmlElement("li");

		// linkToDelete
		surl = new XmlElement("s:url");
		surl.addAttribute(new Attribute("namespace", namespace));
		surl.addAttribute(new Attribute("action", deleteActionNameInput));
		for (IntrospectedColumn primaryKeyColumn : primaryKeys) {
			XmlElement sparam = new XmlElement("s:param");
			sparam.addAttribute(new Attribute(
				"name",
				primaryKeyColumn.getJavaProperty()));
			sparam.addAttribute(new Attribute(
				"value",
				dtoInstanceName + "." + primaryKeyColumn.getJavaProperty()));
			surl.addElement(sparam);
		}

		/* addAllSparamJspIncludePageTag */
		if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
			addJspIncludePageTag(surl, allSparamViewFileName);
		}

		XmlElement linkToDeletet = new XmlElement("a");
		linkToDeletet.addAttribute(new Attribute(
			"href",
			surl.getFormattedContent(0)));
		XmlElement linkToDeleteInputText = new XmlElement("s:text");
		linkToDeleteInputText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + ".linkto." + deleteActionNameInput));
		linkToDeletet.addElement(linkToDeleteInputText);
		li.addElement(linkToDeletet);
		ul.addElement(li);
		body.addElement(ul);

		return getJspFromXml(document);
	}

	private void addJspIncludePageTag(XmlElement target, String page) {
		XmlElement jspinclude = new XmlElement("jsp:include");
		jspinclude.addAttribute(new Attribute("page", "listinfo/" + page));
		target.addElement(jspinclude);
	}

	private String getJspFromXml(Document document) {
		String documentStr = document.getFormattedContent();
		documentStr = documentStr.substring(documentStr.indexOf("\n") + 1);

		// 공백을 탭으로 전환
		Pattern blankPattern = Pattern.compile("  ");
		Matcher blankMatcher = blankPattern.matcher(documentStr);

		StringBuffer sb = new StringBuffer();
		while (blankMatcher.find()) {
			blankMatcher.appendReplacement(sb, "\t");
		}
		blankMatcher.appendTail(sb);

		// <jsp:include page="(.*)" /> -> <%@ include file="$1" %>
		blankPattern = Pattern.compile("<jsp:include page=\"(.*)\" />");
		blankMatcher = blankPattern.matcher(sb.toString());

		sb = new StringBuffer();
		while (blankMatcher.find()) {
			blankMatcher.appendReplacement(sb, "<%@ include file=\"$1\" %>");
		}
		blankMatcher.appendTail(sb);

		// properties 파일을 만들지 않을 경우
		if (!isCreateProperties) {
			blankPattern = Pattern.compile("<s:text name=\"(.*)\" />");
			blankMatcher = blankPattern.matcher(sb.toString());

			sb = new StringBuffer();
			while (blankMatcher.find()) {
				blankMatcher.appendReplacement(sb, "$1");
			}
			blankMatcher.appendTail(sb);
		}

		return getJspInfo() + sb.toString();
	}

	private void addHeadAndBodyAndTitle(
			XmlElement root,
			XmlElement body,
			String actionName,
			boolean useTitleLink) {
		// head
		XmlElement head = new XmlElement("head");
		root.addElement(head);

		// meta
		XmlElement meta = new XmlElement("meta");
		meta.addAttribute(new Attribute("http-equiv", "Content-Type"));
		meta.addAttribute(new Attribute(
			"content",
			"text/html; charset=" + charset));
		head.addElement(meta);

		// title
		XmlElement title = new XmlElement("title");
		XmlElement titleText = new XmlElement("s:text");
		titleText.addAttribute(new Attribute(
			"name",
			dtoInstanceName + "." + actionName + ".title"));
		title.addElement(titleText);
		head.addElement(title);

		root.addElement(body);

		// h1
		XmlElement h1 = new XmlElement("h1");
		body.addElement(h1);

		if (useTitleLink) {
			XmlElement link = new XmlElement("a");
			link.addAttribute(new Attribute(
				"href",
				"<s:url namespace=\"" + namespace + "\" action=\"" + actionName + "\" />"));
			link.addElement(titleText);
			h1.addElement(link);
		} else {
			h1.addElement(titleText);
		}
	}
}
