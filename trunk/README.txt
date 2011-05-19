
Struts2 Scaffold Plugin for iBATOR
----------------------------------

Ruby on Rails�� Scaffold ����� �ڹٹ������� ������ iBATOR�� �÷�����.
Struts2, iBatis �����ӿ�ũ�� ������� �մϴ�.

�� DB ����� ���� Į���� number(19~)�� ����� iBATOR�� java.math.BigDecimal���� �ν��մϴ�.
�� DB ����� ���� Į���� number(10~18)�� ����� iBATOR�� java.lang.Long���� �ν��մϴ�.
�� DB ����� ���� Į���� number(5~9)�� ����� iBATOR�� java.lang.Integer���� �ν��մϴ�.
�� DB ����� ���� Į���� number(0~5)�� ����� iBATOR�� java.lang.Short���� �ν��մϴ�.

��ġ ���:
  iBATOR ��ġ (Eclipse 3.5.1 ����):
    1. Eclipse�� Help �� Install New Software���� ����� Add...��ư�� Ŭ��
    2. Add Site â���� Archive...��ư�� Ŭ��
    3. Struts2 Scaffold Plugin for iBATOR�� ���Ե� IbatorForEclipse1.2.1.zip�� ��θ� ����
    4. Ibator Version 1.2�� üũ�� �� Next��ư�� Ŭ��
    5. Next��ư�� Ŭ��
    6. ���̼����� �а� 'I accept the terms of the license agreement'�� ���� �� Finish��ư�� Ŭ��
    7. ��ġ �Ϸ�

  Struts2 Scaffold Plugin ��ġ:
    8. Eclipse�� ����
    9. Eclipse ��ġ ������ \plugins\org.apache.ibatis.ibator.core_1.2.1 ���� ���� ibator.jar��
       Struts2 Scaffold Plugin for iBATOR�� ���Ե� ibator.jar�� ���� ����
    10. ��ġ �Ϸ�


�⺻ ����:
  1. �ƹ� ������Ʈ�� Struts2 Scaffold Plugin for iBATOR�� ���Ե� ibator-config_1_0.dtd�� ibatorConfig.xml�� ������ ����
  2. ibatorConfig.xml���� ?��� ���� �κ��� ä��
     �� �ڼ��� ���� Struts2 Scaffold Plugin for iBATOR�� ���Ե� \iBATOR doc\index.html�� XML Configuration File Reference�� ����
     �� targetProject�� Struts2, iBatis �����ӿ�ũ�� ������ ��ġ�Ǿ� �־�� �մϴ�.
  3. ibatorConfig.xml������ ����Ŭ�� �� Generate iBATIS Artifacts�� Ŭ��
  4. �ڵ����� �ڵ尡 �����Ǹ� ������Ʈ�� Refresh(F5)�� (view ������ ������ ���� Ȯ���� �� �ֽ��ϴ�.)
  5. iBatis ���� ������ sqlMapConfig.xml���Ͽ� <sqlMap resource="Map ��Ű��/{Schema}_{Table Name}_SqlMap.xml" />�� ����
  6. Struts2 ���� ������ struts.xml�� <include file="�׼� ��Ű��/{Domain Object Name}-struts.xml" />�� ����
  7. �Ϸ�


�ǰ�:
  Struts2 Scaffold Plugin for iBATOR�� �̿��Ͽ� ������ ��,
  �������� ������ ���� ���� ���ϵ��� �ǵ��� �������� ���ñ� �ٶ��ϴ�.
    Sql Map:
      {Schema}_{Table Name}_SqlMap.xml,
    ��:
      {Domain Object Name},
      {Domain Object Name}DAO,
      {Domain Object Name}ListSortInfo,
      {Domain Object Name}ListSearchInfo,
      {Domain Object Name}ListInfo,
      {Domain Object Name}Example,
      {Domain Object Name}Key,
    ��:
      \{View ����}\listinfo ������ �� ���ϵ�

  ���� ���ϵ��� �����Ӱ� �����ϼŵ� �����ϴ�.
    �׼� ��Ű�� ������ ���ϵ�,
    ����:
      {Domain Object Name}Service,
    ��:
      \{View ����} ������ �� ���ϵ� (\{View ����}\listinfo ������ �� ���ϵ� ����)

  �̷��Ը� �Ѵٸ� isUpdateProcees ������ �̿��Ͽ�
  ���̺� Į���� �߰��� ���� ���� ������������ �Ⱓ�� �ִ��� ������ �� �ֽ��ϴ�.

  isUpdateProcees ����:
    isUpdateProcees�� true�̸� ���� ���ϵ��� �������� �ʽ��ϴ�.
      �׼� ��Ű�� ������ ���ϵ�,
      ����:
        {Domain Object Name}Service,
      ��:
        \{View ����} ������ �� ���ϵ� (\{View ����}\listinfo ������ �� ���ϵ� ����)

    ���� ���ϵ��� �����˴ϴ�.
      Sql Map:
        {Schema}_{Table Name}_SqlMap.xml,
      ��:
        {Domain Object Name},
        {Domain Object Name}DAO,
        {Domain Object Name}ListSortInfo,
        {Domain Object Name}ListSearchInfo,
        {Domain Object Name}ListInfo,
        {Domain Object Name}Example,
        {Domain Object Name}Key,
      ��:
        \{View ����}\listinfo ������ �� ���ϵ�
  
  ����) ���̺� Į���� �߰��� ��:
    1. ���� ���ϵ��� �����մϴ�. (���� ���ϵ��� �����Ͽ��ٸ� �ݵ�� ����մϴ�.)
      Sql Map:
        {Schema}_{Table Name}_SqlMap.xml,
      ��:
        {Domain Object Name},
        {Domain Object Name}DAO,
        {Domain Object Name}ListSortInfo,
        {Domain Object Name}ListSearchInfo,
        {Domain Object Name}ListInfo,
        {Domain Object Name}Example,
        {Domain Object Name}Key,
      ��:
        \{View ����}\listinfo ������ �� ���ϵ�
    2. ibatorConfig.xml������ Struts2 Scaffold Plugin ������ '<property name="isUpdateProcees" value="true"/>'�� �߰�
    3. ibatorConfig.xml������ ����Ŭ�� �� Generate iBATIS Artifacts�� Ŭ��
    4. �ڵ����� �ڵ尡 �����Ǹ� ������Ʈ�� Refresh(F5)�� (\{View ����}\listinfo ������ �� ���ϵ��� �ٽ� ������ ���� Ȯ���� �� �ֽ��ϴ�.)
    5. ���� ���ϵ��� �߰��� Į���� �˸°� ����
      �׼� ��Ű�� ������ ���ϵ�,
      ����:
        {Domain Object Name}Service,
      ��:
        \{View ����} ������ �� ���ϵ� (\{View ����}\listinfo ������ �� ���ϵ� ����)
    6. �Ϸ� (����� ������ �ִٸ� �����Ͽ� ����)

