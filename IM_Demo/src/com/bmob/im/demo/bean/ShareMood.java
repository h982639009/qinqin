package com.bmob.im.demo.bean;

import java.io.Serializable;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class ShareMood extends BmobObject implements Serializable{
	private BmobChatUser from;//������
	private BmobRelation to;//���Կ����˶�̬����
	private String content;//�ı�����
	private BmobFile img;//���ı����ݣ�ͼƬ��
	private BmobRelation like;//���޵���
	private BmobRelation comments;//����
	
	public void setUserFrom(BmobChatUser user)
	{
		from=user;
	}
	
	public BmobChatUser getUserFrom()
	{
		return from;
	}
	
	public void setUserTo(BmobRelation to)
	{
		this.to = to;
	}
	
	public BmobRelation getUserTo()
	{
		return to;
	}
	
	public void setContent(String content)
	{
		this.content=content;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setWhoLikes(BmobRelation likes)
	{
		this.like=likes;
	}
	
	public BmobRelation getWhoLikes()
	{
		return like;
	}
	
	public void setComments(BmobRelation comments)
	{
		this.comments=comments;
	}
	
	public BmobRelation getComments()
	{
		return comments;
	}

}
