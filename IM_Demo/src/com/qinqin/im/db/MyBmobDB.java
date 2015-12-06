package com.qinqin.im.db;
import java.util.List;

import com.bmob.im.demo.bean.*;

import android.content.Context;
import android.widget.Toast;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyBmobDB //extends BmobDB
{
	static int UnRead_drawboard=0;
	static int UnRead_sharemood=0;

	//���������ⲿʵ����
	private MyBmobDB()
	{
		
	}
	
	static int getUnReadDrawBoard(final Context context,BmobChatUser current_user)
	{
		BmobQuery<BmobMsg> query = new BmobQuery<BmobMsg>();
		//����Ҫ��fun_drawboard���͵���Ϣ
		query.addWhereEqualTo("tag","fun_drawboard");
		//����Ҫ�Ƿ��͸���ǰ�û���
		query.addWhereEqualTo("toId",current_user.getObjectId());
		//��Ҫ��δ����(0��ʾδ����1��ʾ�Ѷ���
		query.addWhereEqualTo("isReaded", 0);
		query.findObjects(context, new FindListener<BmobMsg>()
				{

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "��ѯδ����Ϣ����", Toast.LENGTH_LONG).show();
						UnRead_drawboard=0;
					}

					@Override
					public void onSuccess(List<BmobMsg> arg0) {
						// TODO Auto-generated method stub
						UnRead_drawboard=arg0.size();
					}
			
				});
		return UnRead_drawboard;
		
		
	}
}
