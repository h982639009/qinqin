package com.bmob.im.demo.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Note extends BmobObject {
	BmobChatUser from;//˭���͵ı�����
	BmobRelation to;//���͸�˭�������ǵ�����Ҳ�����Ƕ���ˣ�
	int year,month,day,hour,second;//����ʱ��
	String content;//���ѵ�����
}
