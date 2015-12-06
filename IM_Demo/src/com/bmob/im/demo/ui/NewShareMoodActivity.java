package com.bmob.im.demo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.R;
import com.bmob.im.demo.bean.ShareMood;
import com.bmob.im.demo.util.CollectionUtils;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;

public class NewShareMoodActivity extends ActivityBase implements OnClickListener{
	LinearLayout include_gv;//����gridView�Ĳ���
	GridView gridView;
	EditText et_content;//��������
	Button bn_send;//���Ͱ�ť
	RelativeLayout rlayout_whosee;//ѡ����Щ���˿ɼ��Ĳ��֣��������õ���¼�
	TextView tv_whosee;//���ڸ��µ�ǰ������Щ�˿ɼ���ģʽ�������˿ɼ���ѡ�����˿ɼ���

	BmobChatUser current_user;//��ǰ�û�
	BmobRelation to;//���Կ�������������û�
	List<BmobChatUser> list;//�����б�
	List<BmobChatUser> checkedUser;//��ѡ�еĺ����б�
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun_sharemood_new);
		init();
		bn_send.setOnClickListener(this);
		rlayout_whosee.setOnClickListener(this);
	}

	void init() {
		//��ȡ��ǰ�û�
		current_user=BmobUserManager.getInstance(this).getCurrentUser();
		//��ȡ��ǰ�����б�
		list=CollectionUtils.map2list(CustomApplcation.getInstance().getContactList());
		//ʵ����ѡ�к����б�
		checkedUser = new ArrayList<BmobChatUser>();
		
		//�����е����
		include_gv =(LinearLayout)findViewById(R.id.include_choose_friend_layout);
		gridView = (GridView) findViewById(R.id.choose_friend_gridview);
		et_content = (EditText) findViewById(R.id.fun_sharemood_new_content);
		bn_send = (Button)findViewById(R.id.fun_sharemood_new_send);
		rlayout_whosee = (RelativeLayout) findViewById(R.id.fun_sharemood_rlayout_whosee);
		tv_whosee = (TextView)findViewById(R.id.fun_sharemood_tv_whosee);
	
		
		//ΪgridView���Adapter
		CustomApplcation ca = CustomApplcation.getInstance();
		HashMap<String, BmobChatUser> map = (HashMap<String, BmobChatUser>) ca.getContactList();
		list=CollectionUtils.map2list(map);
		//_GridViewAdapter adapter = new _GridViewAdapter(this, list);
		//gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				BmobChatUser user=list.get(position);
				String user_all="";
				if(checkedUser.contains(user))
				{//����Ѿ���ѡ�У�ȡ��ѡ�У�ȥ��ѡ�б��
					checkedUser.remove(user);
					view.findViewById(R.id.choose_friend_item_iv_tips).setVisibility(View.GONE);
					
					for(BmobChatUser u:checkedUser)
					{
						user_all+=(u.getUsername()+" ");
					}
					ShowToast(user_all);
				}
				else{//��δ��ѡ��
					checkedUser.add(user);
					view.findViewById(R.id.choose_friend_item_iv_tips).setVisibility(View.VISIBLE);

					for(BmobChatUser u:checkedUser)
					{
						user_all+=(u.getUsername()+" ");
					}
					ShowToast(user_all);
				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id)
		{
		case R.id.fun_sharemood_new_send:
			String content=et_content.getText().toString();
			
			ShareMood sm=new ShareMood();
			sm.setUserFrom(current_user);
			to= new BmobRelation();
			if(checkedUser.size()>0)
			{
				//��ѡ�˿ɼ�����
				for(BmobChatUser user:checkedUser)
				{
					to.add(user);
				}
			}
			else{
				//û����ѡ�ɼ����ˣ�Ĭ��Ϊ�������˿ɼ�
				for(BmobChatUser user:list)
				{
					to.add(user);
				}
			}
			to.add(current_user);
			sm.setUserTo(to);
			sm.setContent(content);
			
			sm.save(this, new SaveListener(){

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ShowToast("�������鷢��ʧ����!");
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					ShowToast("���鷢�ͳɹ���");
				}
				
			});
			this.finish();
			break;
		case R.id.fun_sharemood_rlayout_whosee:
			if(include_gv.getVisibility()==View.VISIBLE)
			{
				//�����ǰ�ɼ�������󣬽�����Ϊ��ǰ���ɼ���ͬʱ����ģʽ����
				include_gv.setVisibility(View.GONE);
				tv_whosee.setText("�������˿ɼ�");
			}
			else if(include_gv.getVisibility()==View.GONE)
			{
				include_gv.setVisibility(View.VISIBLE);
				tv_whosee.setText("��ѡ�����˿ɼ�");
			}
			break;
		}
		
	}
}
