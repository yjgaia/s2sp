
Struts2 Scaffold Plugin for iBATOR
----------------------------------

Ruby on Rails의 Scaffold 기능을 자바버젼으로 구현한 iBATOR용 플러그인.
Struts2, iBatis 프레임워크를 기반으로 합니다.

※ DB 설계시 숫자 칼럼을 number(19~)로 만들면 iBATOR가 java.math.BigDecimal으로 인식합니다.
※ DB 설계시 숫자 칼럼을 number(10~18)로 만들면 iBATOR가 java.lang.Long으로 인식합니다.
※ DB 설계시 숫자 칼럼을 number(5~9)로 만들면 iBATOR가 java.lang.Integer으로 인식합니다.
※ DB 설계시 숫자 칼럼을 number(0~5)로 만들면 iBATOR가 java.lang.Short으로 인식합니다.

설치 방법:
  iBATOR 설치 (Eclipse 3.5.1 기준):
    1. Eclipse의 Help → Install New Software에서 상단의 Add...버튼을 클릭
    2. Add Site 창에서 Archive...버튼을 클릭
    3. Struts2 Scaffold Plugin for iBATOR에 포함된 IbatorForEclipse1.2.1.zip의 경로를 설정
    4. Ibator Version 1.2를 체크한 후 Next버튼을 클릭
    5. Next버튼을 클릭
    6. 라이센스를 읽고 'I accept the terms of the license agreement'를 선택 후 Finish버튼을 클릭
    7. 설치 완료

  Struts2 Scaffold Plugin 설치:
    8. Eclipse를 종료
    9. Eclipse 설치 폴더의 \plugins\org.apache.ibatis.ibator.core_1.2.1 폴더 내의 ibator.jar를
       Struts2 Scaffold Plugin for iBATOR에 포함된 ibator.jar로 덮어 쒸움
    10. 설치 완료


기본 사용법:
  1. 아무 프로젝트에 Struts2 Scaffold Plugin for iBATOR에 포함된 ibator-config_1_0.dtd와 ibatorConfig.xml를 복사해 넣음
  2. ibatorConfig.xml에서 ?라고 적힌 부분을 채움
     → 자세한 것은 Struts2 Scaffold Plugin for iBATOR에 포함된 \iBATOR doc\index.html의 XML Configuration File Reference를 참고
     → targetProject는 Struts2, iBatis 프레임워크가 사전에 설치되어 있어야 합니다.
  3. ibatorConfig.xml파일을 오른클릭 후 Generate iBATIS Artifacts를 클릭
  4. 자동으로 코드가 생성되면 프로젝트를 Refresh(F5)함 (view 파일이 생성된 것을 확인할 수 있습니다.)
  5. iBatis 설정 파일인 sqlMapConfig.xml파일에 <sqlMap resource="Map 패키지/{Schema}_{Table Name}_SqlMap.xml" />를 삽입
  6. Struts2 설정 파일인 struts.xml에 <include file="액션 패키지/{Domain Object Name}-struts.xml" />를 삽입
  7. 완료


권고:
  Struts2 Scaffold Plugin for iBATOR를 이용하여 개발할 시,
  유지보수 측면을 위해 이하 파일들은 되도록 수정하지 마시기 바랍니다.
    Sql Map:
      {Schema}_{Table Name}_SqlMap.xml,
    모델:
      {Domain Object Name},
      {Domain Object Name}DAO,
      {Domain Object Name}ListSortInfo,
      {Domain Object Name}ListSearchInfo,
      {Domain Object Name}ListInfo,
      {Domain Object Name}Example,
      {Domain Object Name}Key,
    뷰:
      \{View 폴더}\listinfo 내부의 뷰 파일들

  이하 파일들은 자유롭게 개발하셔도 좋습니다.
    액션 패키지 내부의 파일들,
    서비스:
      {Domain Object Name}Service,
    뷰:
      \{View 폴더} 내부의 뷰 파일들 (\{View 폴더}\listinfo 내부의 뷰 파일들 제외)

  이렇게만 한다면 isUpdateProcees 설정을 이용하여
  테이블에 칼럼이 추가될 때와 같은 유지보수에서 기간을 최대한 단축할 수 있습니다.

  isUpdateProcees 설정:
    isUpdateProcees가 true이면 이하 파일들이 생성되지 않습니다.
      액션 패키지 내부의 파일들,
      서비스:
        {Domain Object Name}Service,
      뷰:
        \{View 폴더} 내부의 뷰 파일들 (\{View 폴더}\listinfo 내부의 뷰 파일들 제외)

    이하 파일들은 생성됩니다.
      Sql Map:
        {Schema}_{Table Name}_SqlMap.xml,
      모델:
        {Domain Object Name},
        {Domain Object Name}DAO,
        {Domain Object Name}ListSortInfo,
        {Domain Object Name}ListSearchInfo,
        {Domain Object Name}ListInfo,
        {Domain Object Name}Example,
        {Domain Object Name}Key,
      뷰:
        \{View 폴더}\listinfo 내부의 뷰 파일들
  
  예시) 테이블에 칼럼이 추가될 때:
    1. 이하 파일들을 삭제합니다. (이하 파일들을 수정하였다면 반드시 백업합니다.)
      Sql Map:
        {Schema}_{Table Name}_SqlMap.xml,
      모델:
        {Domain Object Name},
        {Domain Object Name}DAO,
        {Domain Object Name}ListSortInfo,
        {Domain Object Name}ListSearchInfo,
        {Domain Object Name}ListInfo,
        {Domain Object Name}Example,
        {Domain Object Name}Key,
      뷰:
        \{View 폴더}\listinfo 내부의 뷰 파일들
    2. ibatorConfig.xml파일의 Struts2 Scaffold Plugin 설정에 '<property name="isUpdateProcees" value="true"/>'를 추가
    3. ibatorConfig.xml파일을 오른클릭 후 Generate iBATIS Artifacts를 클릭
    4. 자동으로 코드가 생성되면 프로젝트를 Refresh(F5)함 (\{View 폴더}\listinfo 내부의 뷰 파일들이 다시 생성된 것을 확인할 수 있습니다.)
    5. 이하 파일들을 추가된 칼럼에 알맞게 수정
      액션 패키지 내부의 파일들,
      서비스:
        {Domain Object Name}Service,
      뷰:
        \{View 폴더} 내부의 뷰 파일들 (\{View 폴더}\listinfo 내부의 뷰 파일들 제외)
    6. 완료 (백업한 파일이 있다면 참고하여 수정)

