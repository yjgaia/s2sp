package lotus.ibator.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lotus.ibator.struts2.scaffold.plugin.ActionViewsGenerator;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.GeneratedXmlFile;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * Struts2 Scaffold Plugin for iBATOR
 * 
 * Struts2�� ���� CRUD ����, �׼�, �� �ڵ� ���� �÷�����(iBator��)
 * 
 * @author Lotus
 * 
 */
public class Struts2ScaffoldPlugin extends ActionViewsGenerator {

	// �ڹ� ���ϵ� ����
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		if (actionPackage != null) {
			if (useSort != null) {
				answer.add(generateListSortInfo()); // ���� ���� Ŭ���� ����
			}
			if (useSearch != null) {
				answer.add(generateListSearchInfo()); // �˻� ���� Ŭ���� ����
			}
			if (addInfo != null || addInfo2 != null) {
				answer.add(generateListInfo()); // ��Ÿ ���� Ŭ���� ����
			}
		}
		if (!isUpdateProcees) { // ���������� �ƴҶ�
			answer.add(generateService()); // ���� ����
			if (actionPackage != null) { // �׼� ��Ű���� �Էµ��� ���
				answer.add(generateSuperAction()); // ���� �׼��� ����
				answer.add(generateListAction()); // ����Ʈ �׼�
				answer.add(generateCreateAction()); // Create �׼�
				if (primaryKeys.size() > 0) {
					answer.add(generateReadAction()); // Read �׼�
					answer.add(generateUpdateAction()); // Update �׼�
					answer.add(generateDeleteAction()); // Delete �׼�
				}
			}
		}
		return answer;
	}

	// ��Ÿ ���ϵ� ����
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		// ���������� �ƴϰ� �׼� ��Ű���� �Էµ��� ���
		if (actionPackage != null && !isUpdateProcees) {
			answer.add(generateCreateValidation()); // Create ��ȿ�� �˻�
			if (primaryKeys.size() > 0) {
				answer.add(generateUpdateValidation()); // Update ��ȿ�� �˻�
			}
			answer.add(generateStruts2Config()); // Struts2 Config ���� ����
		}
		if (webContentPath != null) { // Web Content ��ΰ� �Էµ��� ���
			try {
				if (isCreateProperties && actionPackage != null && srcPath != null && !isUpdateProcees) {
					StringBuilder sb = new StringBuilder();
					StringTokenizer st = new StringTokenizer(actionPackage, ".");
					sb.append(srcPath);
					sb.append(File.separatorChar);
					while (st.hasMoreTokens()) {
						sb.append(st.nextToken());
						sb.append(File.separatorChar);
					}
					// package.properties ����
					createFile(
						sb.toString(),
						"package.properties",
						generateProperties());
					// package_ko.properties ����
					createFile(
						sb.toString(),
						"package_ko.properties",
						generateKoProperties());
				}

				if (useSort != null || useSearch != null || addInfo != null || addInfo2 != null) {
					// all
					createIncludeViewFile(
						allShiddenViewFileName,
						generateAllShiddenView());

					createIncludeViewFile(
						allSparamViewFileName,
						generateAllSparamView());
				}

				if (useSort != null) {
					// forSort
					createIncludeViewFile(
						forSortShiddenViewFileName,
						generateForSortShiddenView());

					createIncludeViewFile(
						forSortSparamViewFileName,
						generateForSortSparamView());
				}

				if (useSearch != null) {
					// forSearch
					createIncludeViewFile(
						forSearchShiddenViewFileName,
						generateForSearchShiddenView());

					createIncludeViewFile(
						forSearchSparamViewFileName,
						generateForSearchSparamView());
				}

				if (addInfo != null || addInfo2 != null) {
					// forInfo
					createIncludeViewFile(
						forInfoShiddenViewFileName,
						generateForInfoShiddenView());

					createIncludeViewFile(
						forInfoSparamViewFileName,
						generateForInfoSparamView());
				}

				if (useSort != null) {
					// sort
					createIncludeViewFile(
						sortShiddenViewFileName,
						generateSortShiddenView());
					createIncludeViewFile(
						sortSparamViewFileName,
						generateSortSparamView());
				}

				if (useSearch != null) {
					// search
					createIncludeViewFile(
						searchShiddenViewFileName,
						generateSearchShiddenView());
					createIncludeViewFile(
						searchSparamViewFileName,
						generateSearchSparamView());
				}

				if (addInfo != null) {
					// addInfo
					createIncludeViewFile(
						infoShiddenViewFileName,
						generateInfoShiddenView());
					createIncludeViewFile(
						infoSparamViewFileName,
						generateInfoSparamView());
				}

				if (addInfo2 != null) {
					// addInfo2
					for (String key : addInfo2.keySet()) {
						createIncludeViewFile(
							key + "-shidden.jsp",
							generateInfo2ShiddenView(addInfo2.get(key)));
						createIncludeViewFile(
							key + "-sparam.jsp",
							generateInfo2SparamView(addInfo2.get(key)));
					}
				}

				if (actionPackage != null && !isUpdateProcees) {
					createViewFile(listViewFileName, generateListView()); // List
					// View
					// ����
					// ����
					createViewFile(
						createInputViewFileName,
						generateInputView(createActionNameInput)); // Create
					// View ����
					// ����
					if (primaryKeys.size() > 0) {
						createViewFile(
							updateInputViewFileName,
							generateInputView(updateActionNameInput)); // Update
						// View
						// ���� ����
						createViewFile(readViewFileName, generateReadView()); // Read
						// View
						// ����
						// ����
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}

	private void createViewFile(String fileName, String content) throws IOException {
		createFile(webContentPath + namespace, fileName, content);
	}

	private void createIncludeViewFile(String fileName, String content) throws IOException {
		createFile(webContentPath + namespace + "/listinfo", fileName, content);
	}

	private void createFile(
			String directoryPath,
			String fileName,
			String content) throws IOException {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File targetFile = new File(directory, fileName);
		if (targetFile.exists()) {
			targetFile = getUniqueFileName(directory, fileName);
		}
		writeFile(targetFile, content);
	}

	/**
	 * Writes, or overwrites, the contents of the specified file
	 * 
	 * @param file
	 * @param content
	 */
	private void writeFile(File file, String content) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
		bw.write(content);
		bw.close();
	}

	private File getUniqueFileName(File directory, String fileName) {
		File answer = null;

		// try up to 1000 times to generate a unique file name
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < 1000; i++) {
			sb.setLength(0);
			sb.append(fileName);
			sb.append('.');
			sb.append(i);

			File testFile = new File(directory, sb.toString());
			if (!testFile.exists()) {
				answer = testFile;
				break;
			}
		}

		if (answer == null) {
			throw new RuntimeException(Messages.getString(
				"RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
		}

		return answer;
	}

}
