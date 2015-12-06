package com.bmob.im.demo.ui;

import java.util.HashMap;

import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.R;
import com.bmob.im.demo.bean.DrawBoard;
import com.bmob.im.demo.config.myBmobConfig;
import com.bmob.im.demo.util.CollectionUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import com.bmob.im.demo.config.myBmobConfig;

public class NewDrawBoardActivity extends ActivityBase implements OnClickListener{
	EditText et_content;
	Button bt_send;
	BmobChatUser cuser;
	final Handler handler=new Handler(){
		public void handleMessage(Message msg)
		{
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun_drawboard_new);
		init();
	}
	
	void init()
	{
		cuser = BmobUserManager.getInstance(this).getCurrentUser();
		et_content=(EditText)findViewById(R.id.fun_board_new_content);
		bt_send=(Button)findViewById(R.id.fun_board_new_send);
		
		bt_send.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CustomApplcation ca = CustomApplcation.getInstance();
		HashMap<String, BmobChatUser> hm = (HashMap) ca.getContactList();
		DrawBoard drawBoard = new DrawBoard();
		drawBoard.setUserFrom(cuser);// ���ù��淢����
		drawBoard.setContent(et_content.getText().toString());// ���ù�������
		BmobRelation br = new BmobRelation();
		for (BmobChatUser user : CollectionUtils.map2list(hm)) {
			BmobChatManager.getInstance(this).sendTextMessage(user,
					BmobMsg.createTextSendMsg(this, user.getObjectId(), "���԰巢������Ϣ��"));
			BmobChatManager.getInstance(this).sendTagMessage(myBmobConfig.FUN_DRAWBOARD, 
					user.getObjectId(), new PushListener()
					{

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							ShowToast("�¹�����Ϣ֪ͨ����ʧ�ܣ�"+arg1);
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							ShowToast("�¹�����Ϣ֪ͨ���ͳɹ�");
						}
				
					});
			br.add(user);
		}
		drawBoard.setUserTo(br);
		drawBoard.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				ShowToast("���淢���ɹ���");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("���淢��ʧ�ܣ������룺" + arg1);
			}
		});
		
		Intent intent=getIntent();
		intent.putExtra("content", et_content.getText().toString());
		NewDrawBoardActivity.this.setResult(0,intent);
		NewDrawBoardActivity.this.finish();
	}
	
	//����Ҫ��дonDestroy������û�е����ť�������棬����ֱ�ӵ�����ؼ�������һ��Activityʱ���ͻ���Ϊû�з��ؽ�������³���
	@Override 
	public void onDestroy()
	{
		super.onDestroy();
		Intent intent=getIntent();
		NewDrawBoardActivity.this.setResult(0,intent);
		NewDrawBoardActivity.this.finish();
	}

}
