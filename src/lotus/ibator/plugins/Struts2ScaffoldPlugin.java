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
 * Struts2를 위한 CRUD 서비스, 액션, 뷰 자동 생성 플러그인(iBator용)
 * 
 * @author Lotus
 * 
 */
public class Struts2ScaffoldPlugin extends ActionViewsGenerator {

	// 자바 파일들 생성
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		if (actionPackage != null) {
			if (useSort != null) {
				answer.add(generateListSortInfo()); // 정렬 정보 클래스 생성
			}
			if (useSearch != null) {
				answer.add(generateListSearchInfo()); // 검색 정보 클래스 생성
			}
			if (addInfo != null || addInfo2 != null) {
				answer.add(generateListInfo()); // 기타 정보 클래스 생성
			}
		}
		if (!isUpdateProcees) { // 유지보수가 아닐때
			answer.add(generateService()); // 서비스 생성
			if (actionPackage != null) { // 액션 패키지가 입력됐을 경우
				answer.add(generateSuperAction()); // 상위 액션을 만듦
				answer.add(generateListAction()); // 리스트 액션
				answer.add(generateCreateAction()); // Create 액션
				if (primaryKeys.size() > 0) {
					answer.add(generateReadAction()); // Read 액션
					answer.add(generateUpdateAction()); // Update 액션
					answer.add(generateDeleteAction()); // Delete 액션
				}
			}
		}
		return answer;
	}

	// 기타 파일들 생성
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		// 유지보수가 아니고 액션 패키지가 입력됐을 경우
		if (actionPackage != null && !isUpdateProcees) {
			answer.add(generateCreateValidation()); // Create 유효성 검사
			if (primaryKeys.size() > 0) {
				answer.add(generateUpdateValidation()); // Update 유효성 검사
			}
			answer.add(generateStruts2Config()); // Struts2 Config 파일 생성
		}
		if (webContentPath != null) { // Web Content 경로가 입력됐을 경우
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
					// package.properties 생성
					createFile(
						sb.toString(),
						"package.properties",
						generateProperties());
					// package_ko.properties 생성
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
					// 파일
					// 생성
					createViewFile(
						createInputViewFileName,
						generateInputView(createActionNameInput)); // Create
					// View 파일
					// 생성
					if (primaryKeys.size() > 0) {
						createViewFile(
							updateInputViewFileName,
							generateInputView(updateActionNameInput)); // Update
						// View
						// 파일 생성
						createViewFile(readViewFileName, generateReadView()); // Read
						// View
						// 파일
						// 생성
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
