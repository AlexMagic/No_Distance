package com.project.activity.smack;







public interface Smack {
	/**
	 * 登陆
	 * @param account 用户名
	 * @param password 密码
	 * @return
	 * @throws XXException
	 */
	public boolean login(String account, String password) throws XXException;

	/**
	 * 注销
	 * @return
	 */
	
	public boolean logout();

	/**
	 * 是否连接上服务器
	 * @return
	 */
	
	public boolean isAuthenticated();
	
	/**
	 * 添加好友
	 * @param user 好友id
	 * @param alias 昵称
	 * @param group 所在分组
	 * @throws XXException
	 */
	
	public void addRosterItem(String user, String alias, String group)
			throws XXException;

	/**
	 * 删除好友
	 * @param user 
	 * @throws XXException
	 */
	
	public void removeRosterItem(String user) throws XXException;

	
	/**
	 * 修改好友昵称
	 * @param user
	 * @param newName
	 * @throws XXException
	 */
	
	public void renameRosterItem(String user, String newName)
			throws XXException;

	
	/**
	 * 移动好友到新的分组
	 * @param user
	 * @param group
	 * @throws XXException
	 */
	
	public void moveRosterItemToGroup(String user, String group)
			throws XXException;

	/**
	 * 重命名分组
	 * @param group 之前的分组
	 * @param newGroup 
	 */
	
	public void renameRosterGroup(String group, String newGroup);

	/**
	 * 请求好友重新授权，用在添加好友失败时，重复添加 再次向对方发出申请
	 * @param user
	 */
	
	public void requestAuthorizationForRosterItem(String user);

	/**
	 * 添加新分组
	 * @param group
	 */
	
	public void addRosterGroup(String group);

	/**
	 * 设置当前在线状态
	 */
	public void setStatusFromConfig();

	
	/**
	 * 发送消息
	 * @param user
	 * @param message
	 */
	public void sendMessage(String user, String message);

	/**
	 * 向服务器送ping-pong包
	 */
	
	public void sendServerPing();

	/**
	 * 从jid中获取好友名
	 * @param jid
	 * @return
	 */
	
	public String getNameForJID(String jid);
}
