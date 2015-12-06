package com.bmob.im.demo.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ShareMood_Comments extends BmobObject{

	private String content;//��������
	private BmobChatUser user;//������
	private BmobFile file;//���ı���������
	private ShareMood sharemood;//���۵�������̬
	
	public void setContent(String content)
	{
		this.content=content;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setUser(BmobChatUser user)
	{
		this.user=user;
	}
	
	public BmobChatUser getUser()
	{
		return user;
	}
	
	public void setSharemood(ShareMood sm)
	{
		sharemood=sm;
	}
	
	public ShareMood getSharemood()
	{
		return sharemood;
	}
}
