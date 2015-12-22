/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.project.domain;

import java.io.Serializable;

import com.easemob.chat.EMContact;

public class User extends EMContact{
	
	
	
	private int unreadMsgCount;
	private String header;
//	private String school;
	private String sex;
	private String stuId;
	private String email;
	private String nickname;
//	private String[] favourite;
	
//	public String getSchool() {
//		return school;
//	}
//
//	public void setSchool(String school) {
//		this.school = school;
//	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}
		return getUsername().equals(((User) o).getUsername());
	}
	
	

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
