package com.bmob.im.demo.adapter;

import java.util.List;

import com.bmob.im.demo.R;
import com.bmob.im.demo.adapter.base.BaseListAdapter;
import com.bmob.im.demo.adapter.base.ViewHolder;
import com.bmob.im.demo.bean.ShareMood;
import com.bmob.im.demo.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.PushListener;

public class ShareMoodAdapter extends BaseListAdapter<ShareMood> {

	Handler handler;//���Ը�������Activity�����еĵײ�������
	List<SharemoodCommentsAdapter> list_cmAdapter;//ÿ��item�е�listView��Adapter
	public ShareMoodAdapter(Context context, List<ShareMood> list,
			Handler handler,List<SharemoodCommentsAdapter> list_adapter) {
		super(context, list);
		this.handler=handler;
		this.list_cmAdapter=list_adapter;
	}
	

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_sharemood, null);
		}
		
		final ShareMood sharemood = getList().get(position);
		// ʹ�����ViewHolder�����;�ǲ���Ҫ����Adapter�����ٶ���һ���ڲ�ViewHolder��
		// ͷ��
		ImageView img_avatar = ViewHolder.get(convertView, R.id.item_sharemood_img_avatar);
		// �ǳƻ��û���
		TextView tv_username = ViewHolder.get(convertView, R.id.item_sharemood_tv_username);
		//����ʱ��
		TextView tv_date =ViewHolder.get(convertView, R.id.item_sharemood_tv_date);
		// ��������
		TextView tv_content = ViewHolder.get(convertView, R.id.item_sharemood_tv_content);
		// ����
		LinearLayout llayout_comment = ViewHolder.get(convertView, R.id.item_sharemood_llayout_comment);
		// ϲ��
		LinearLayout llayout_like = ViewHolder.get(convertView, R.id.item_sharemood_llayout_like);
		// ��ʾϲ�������������
		TextView tv_wholike = ViewHolder.get(convertView, R.id.item_sharemood_tv_wholike);
		//������
		ListView lv_comments= ViewHolder.get(convertView, R.id.item_sharemood_lv_comments);
		
		//Ϊ�˻�֪���ĸ�item�еĵ��ޡ����۰�ť���������ҪΪÿ���ɵ�����ֵ���setTag�ķ����洢��item����ֵ
		llayout_comment.setTag(position);
		llayout_like.setTag(position);

		String avatar = sharemood.getUserFrom().getAvatar();// ��ȡͷ����Ϣ
		String nickName = sharemood.getUserFrom().getNick();// ��ȡ�ǳ�
		String userName = sharemood.getUserFrom().getUsername();// ��ȡ�û��������ǳ�Ϊ��ʱ����ʾ�û���
		String str_content = sharemood.getContent();// ��ȡ��������
		String date= sharemood.getCreatedAt();
		
		/*ShowToast("ͷ����Ϣ:"+avatar);
		ShowToast("�ǳ���Ϣ:"+nickName);
		ShowToast("username��"+userName);
		ShowToast("content:"+str_content);*/

		// ����ͷ��
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, img_avatar, ImageLoadOptions.getOptions());
		} else {
			img_avatar.setImageResource(R.drawable.default_head);
		}

		// �����ǳ�
		if (!TextUtils.isEmpty(nickName)) {
			// �ǳƲ�Ϊ��
			tv_username.setText(nickName);
		} else if(!TextUtils.isEmpty(userName)){
			tv_username.setText(userName);
		}else{
			tv_username.setText("unknown user!");
		}
		//���÷���ʱ��
		tv_date.setText(date);
		// ������������
		tv_content.setText(str_content);
		//��������,�����б���Ҫ�붯̬ƥ��
		SharemoodCommentsAdapter my_smcadapter;
		if(null!=(my_smcadapter=searchCommentAdapter(sharemood)))
		{
			lv_comments.setAdapter(my_smcadapter);
		}else{
			lv_comments.setAdapter(null);
		}
		
		/*if(!TextUtils.isEmpty(comments)){
		tv_comments.setText(comments);}
		else{
			tv_comments.setText("��ʱ��û������Ӵ��");
		}*/
		

		llayout_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowToast("˵��ʲô�ɣ�");
				if(handler!=null)
				{
					Bundle bundle=new Bundle();
					final int position =(Integer) arg0.getTag();//��ȡ����
					bundle.putSerializable("which_sm", list.get(position));
					Message msg=new Message();
					msg.what=0x1233;
					msg.setData(bundle);
					handler.sendMessage(msg);
					
				}
				else
				{
					ShowToast("�쳣��������������ʧ�ܣ�");
				}
				// TODO Auto-generated method stub
				/*final ProgressDialog progress = new ProgressDialog(mContext);
				progress.setMessage("�������...");
				progress.setCanceledOnTouchOutside(false);
				progress.show();
				// ����tag����
				BmobChatManager.getInstance(mContext).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, contract.getObjectId(),
						new PushListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("��������ɹ����ȴ��Է���֤!");
					}

					@Override
					public void onFailure(int arg0, final String arg1) {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("��������ʧ�ܣ����������!");
						ShowLog("��������ʧ��:" + arg1);
					}
				});*/
			}
		});

		llayout_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowToast("������ϲ����");
				// TODO Auto-generated method stub
				/*final ProgressDialog progress = new ProgressDialog(mContext);
				progress.setMessage("�������...");
				progress.setCanceledOnTouchOutside(false);
				progress.show();
				// ����tag����
				BmobChatManager.getInstance(mContext).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, contract.getObjectId(),
						new PushListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("��������ɹ����ȴ��Է���֤!");
					}

					@Override
					public void onFailure(int arg0, final String arg1) {
						// TODO Auto-generated method stub
						progress.dismiss();
						ShowToast("��������ʧ�ܣ����������!");
						ShowLog("��������ʧ��:" + arg1);
					}
				});*/
			}
		});
		return convertView;
	}
	
	//Ѱ���Ƿ����뵱ǰ��̬ƥ��������б�
	SharemoodCommentsAdapter searchCommentAdapter(ShareMood sm)
	{
		for(SharemoodCommentsAdapter smc_adapter:list_cmAdapter)
		{
			if(sm==smc_adapter.getBelongShareMood(sm))
				return smc_adapter;
		}
		return null;
		
	}

}
