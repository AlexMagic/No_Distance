package com.project.activity.smack;







public interface Smack {
	/**
	 * ��½
	 * @param account �û���
	 * @param password ����
	 * @return
	 * @throws XXException
	 */
	public boolean login(String account, String password) throws XXException;

	/**
	 * ע��
	 * @return
	 */
	
	public boolean logout();

	/**
	 * �Ƿ������Ϸ�����
	 * @return
	 */
	
	public boolean isAuthenticated();
	
	/**
	 * ��Ӻ���
	 * @param user ����id
	 * @param alias �ǳ�
	 * @param group ���ڷ���
	 * @throws XXException
	 */
	
	public void addRosterItem(String user, String alias, String group)
			throws XXException;

	/**
	 * ɾ������
	 * @param user 
	 * @throws XXException
	 */
	
	public void removeRosterItem(String user) throws XXException;

	
	/**
	 * �޸ĺ����ǳ�
	 * @param user
	 * @param newName
	 * @throws XXException
	 */
	
	public void renameRosterItem(String user, String newName)
			throws XXException;

	
	/**
	 * �ƶ����ѵ��µķ���
	 * @param user
	 * @param group
	 * @throws XXException
	 */
	
	public void moveRosterItemToGroup(String user, String group)
			throws XXException;

	/**
	 * ����������
	 * @param group ֮ǰ�ķ���
	 * @param newGroup 
	 */
	
	public void renameRosterGroup(String group, String newGroup);

	/**
	 * �������������Ȩ��������Ӻ���ʧ��ʱ���ظ���� �ٴ���Է���������
	 * @param user
	 */
	
	public void requestAuthorizationForRosterItem(String user);

	/**
	 * ����·���
	 * @param group
	 */
	
	public void addRosterGroup(String group);

	/**
	 * ���õ�ǰ����״̬
	 */
	public void setStatusFromConfig();

	
	/**
	 * ������Ϣ
	 * @param user
	 * @param message
	 */
	public void sendMessage(String user, String message);

	/**
	 * ���������ping-pong��
	 */
	
	public void sendServerPing();

	/**
	 * ��jid�л�ȡ������
	 * @param jid
	 * @return
	 */
	
	public String getNameForJID(String jid);
}
