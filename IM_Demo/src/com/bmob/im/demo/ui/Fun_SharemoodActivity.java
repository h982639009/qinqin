package com.bmob.im.demo.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.R;
import com.bmob.im.demo.adapter.EmoViewPagerAdapter;
import com.bmob.im.demo.adapter.EmoteAdapter;
import com.bmob.im.demo.adapter.ShareMoodAdapter;
import com.bmob.im.demo.adapter.SharemoodCommentsAdapter;
import com.bmob.im.demo.bean.FaceText;
import com.bmob.im.demo.bean.ShareMood;
import com.bmob.im.demo.bean.ShareMood_Comments;
import com.bmob.im.demo.ui.ChatActivity.VoiceTouchListen;
import com.bmob.im.demo.util.CollectionUtils;
import com.bmob.im.demo.util.CommonUtils;
import com.bmob.im.demo.util.FaceTextUtils;
import com.bmob.im.demo.view.EmoticonsEditText;
import com.bmob.im.demo.view.xlist.XListView;
import com.bmob.im.demo.view.xlist.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class Fun_SharemoodActivity extends ActivityBase implements OnClickListener, IXListViewListener {

	XListView xlist_content;
	Button btn_new;
	ShareMood current_sharemood;// ��ǰ��������۰�ť���ߵ��ް�ť�����鶯̬
	BmobChatUser current_user;
	ShareMoodAdapter adapter;
	int currentPager = 0;// ��ǰҳ��
	int num_eachPage = 10;
	List<ShareMood> list = new ArrayList<ShareMood>();// �û����Կ��������鹲���б�
	// �����б�ÿ��item�е�listView��adapter
	List<SharemoodCommentsAdapter> list_cmAdapter = new ArrayList<SharemoodCommentsAdapter>();
	// ��ʱ���ÿ������������б��ڹ�����������adapter�����ݴ������
	List<ShareMood_Comments> list_smc = new ArrayList<ShareMood_Comments>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun_sharemood);
		init();
		getShareMood();// ��ȡ���˷���������
	}

	void init() {

		// edit_user_comment =
		// (EmoticonsEditText)findViewById(R.id.edit_user_comment);
		xlist_content = (XListView) findViewById(R.id.sharemood_xlist_content);
		// ������ظ���
		xlist_content.setPullLoadEnable(true);
		// ��������
		xlist_content.setPullRefreshEnable(true);
		// ���ü�����
		xlist_content.setXListViewListener(this);
		xlist_content.pullRefreshing();
		xlist_content.setDividerHeight(0);
		btn_new = (Button) findViewById(R.id.sharemood_btn_new);
		btn_new.setOnClickListener(this);

		current_user = BmobUserManager.getInstance(this).getCurrentUser();
	}

	void getShareMood() {
		BmobQuery<ShareMood> query = new BmobQuery<ShareMood>();
		query.addWhereEqualTo("to", current_user);
		query.order("-createdAt");
		query.setLimit(num_eachPage);// ��ҳ��ѯ��
		query.include("from");// ͬʱҪ�ѷ����˵���ϢҲ���ҳ���������Ҫ��Ӵ˾䣬������÷������������get����ʱ�õ��Ľ�Ϊnull
		//query.setCachePolicy(CachePolicy.CACHE_ONLY);// Ĭ�Ͻ���ʱֻ�ӻ����л�ȡ�������û��ȴ������
		query.findObjects(this, new FindListener<ShareMood>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("��ȡ���鶯̬ʧ�ܣ�");

			}

			@Override
			public void onSuccess(List<ShareMood> arg0) {
				// TODO Auto-generated method stub
				// ��handler��Ϊ�˷�����listView�е������ӵ���¼������µ�ǰActivity�ĵײ�������
				for (ShareMood sm : arg0) {
					list.add(sm);
					findComments(sm);// �������ۣ����adapter����ӵ��б�
				}
				adapter = new ShareMoodAdapter(Fun_SharemoodActivity.this, arg0, null, list_cmAdapter);
				xlist_content.setAdapter(adapter);
				currentPager++;
			}
		});

		// ��ʱ��ʾ��Ϻ󣬿�ʼ�������ȡ�ж��Ƿ��������ݣ�����У���ȡ֮
		//getShareMoodFromNetwork(list.get(0).getCreatedAt());// ���ұȻ������ݸ����¡�������
	}

	void getShareMoodFromNetwork(String str_date) {
		String pattern = "yyyy-MM-dd HH:mm:ss";// ʱ���ʽ
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(str_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BmobQuery<ShareMood> query = new BmobQuery<ShareMood>();
		query.addWhereEqualTo("to", current_user);
		query.addWhereGreaterThan("createdAt", new BmobDate(date));
		query.order("createdAt");// ��С������
		query.setLimit(num_eachPage);// ��ҳ��ѯ��
		query.include("from");// ͬʱҪ�ѷ����˵���ϢҲ���ҳ���������Ҫ��Ӵ˾䣬������÷������������get����ʱ�õ��Ľ�Ϊnull
		query.setCachePolicy(CachePolicy.NETWORK_ONLY);// ֻ�������ȡ���µ����ݣ����Զ����浽���أ�
		query.findObjects(this, new FindListener<ShareMood>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("��ȡ���鶯̬ʧ�ܣ�");

			}

			@Override
			public void onSuccess(List<ShareMood> arg0) {
				// TODO Auto-generated method stub
				// ��handler��Ϊ�˷�����listView�е������ӵ���¼������µ�ǰActivity�ĵײ�������
				for (ShareMood sm : arg0) {
					list.add(0, sm);
					findComments(sm);// �������ۣ����adapter����ӵ��б�
				}
				adapter.setList(list);// ������������
				currentPager++;
			}
		});
	}

	void findComments(final ShareMood sm) {
		// �������۴���б�

		BmobQuery<ShareMood_Comments> query = new BmobQuery<ShareMood_Comments>();
		query.addWhereEqualTo("sharemood", sm);
		query.order("-createdAt");
		query.setLimit(10);// �����ʾ10������
		query.include("user");// ͬʱҪ�������˵���ϢҲ���ҳ���������Ҫ��Ӵ˾䣬��������������������get����ʱ�õ��Ľ�Ϊnull
		query.findObjects(this, new FindListener<ShareMood_Comments>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("��ȡ����ʧ�ܣ�");

			}

			@Override
			public void onSuccess(List<ShareMood_Comments> arg0) {
				// TODO Auto-generated method stub
				// ��handler��Ϊ�˷�����listView�е������ӵ���¼������µ�ǰActivity�ĵײ�������
				for (ShareMood_Comments smc : arg0) {
					list_smc.add(smc);
				}
				// ����adapter
				SharemoodCommentsAdapter smc_adapter = new SharemoodCommentsAdapter(Fun_SharemoodActivity.this,
						list_smc);
				smc_adapter.setBelongShareMood(sm);
				// ��adapter��ӵ��б���
				list_cmAdapter.add(smc_adapter);
			}
		});
		return;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sharemood_btn_new:// �����¶�̬
			Intent intent = new Intent(Fun_SharemoodActivity.this, NewShareMoodActivity.class);
			startActivity(intent);
			break;
		// case R.id.btn_chat_send:// �����ı�
		// final String msg = edit_user_comment.getText().toString();
		// if (msg.equals("")) {
		// ShowToast("�����뷢����Ϣ!");
		// return;
		// }
		// boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		// if (!isNetConnected) {
		// ShowToast(R.string.network_tips);
		// // return;
		// }
		// // �����������ϴ���������
		// ShareMood_Comments smc = new ShareMood_Comments();
		// smc.setUser(current_user);
		// smc.setContent(msg);
		// smc.setSharemood(current_sharemood);
		// smc.save(Fun_SharemoodActivity.this, new SaveListener() {
		//
		// @Override
		// public void onFailure(int arg0, String arg1) {
		// // TODO Auto-generated method stub
		//
		// ShowToast("�쳣����");
		//
		// }
		//
		// @Override
		// public void onSuccess() {
		// // TODO Auto-generated method stub
		// ShowToast("���۷���ɹ�");
		//
		// }
		//
		// });
		// // ˢ�½���
		// // refreshMessage(message);
		//
		// break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		ShowToast("����ˢ��");
		BmobQuery<ShareMood> query = new BmobQuery<ShareMood>();
		query.addWhereEqualTo("to", current_user);
		// query.order("-createAt");
		query.setLimit(num_eachPage);// ��ҳ��ѯ
		// query.setSkip(list.size());// ��������ʾ����Ϣ�����ͨ��ʱ���ж���ʵ��

		query.include("from");// ͬʱҪ�ѷ����˵���ϢҲ���ҳ���������Ҫ��Ӵ˾䣬������÷������������get����ʱ�õ��Ľ�Ϊnull
		query.findObjects(this, new FindListener<ShareMood>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("��ȡ���鶯̬ʧ�ܣ�");

			}

			@Override
			public void onSuccess(List<ShareMood> arg0) {
				// TODO Auto-generated method stub
				// ��handler��Ϊ�˷�����listView�е������ӵ���¼������µ�ǰActivity�ĵײ�������
				if (arg0.size() <= 0) {
					ShowToast("��ǰ�Ѿ�û�и��ද̬");
					return;
				}
				for (ShareMood sm : arg0) {
					list.add(sm);
					findComments(sm);// �������ۣ����adapter����ӵ��б�
				}
				adapter.setList(list);// ���Զ���������Դ
				currentPager++;// ҳ����1
			}
		});
		xlist_content.stopLoadMore();// ���Ӵ˾��һֱִ�м��ظ��ࣻ
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		ShowToast("���Լ���");
		BmobQuery<ShareMood> query = new BmobQuery<ShareMood>();
		query.addWhereEqualTo("to", current_user);
		query.order("-createAt");
		query.setLimit(num_eachPage);// ��ҳ��ѯ
		query.setSkip(currentPager * num_eachPage);// ��������ʾ����Ϣ
		query.include("from");// ͬʱҪ�ѷ����˵���ϢҲ���ҳ���������Ҫ��Ӵ˾䣬������÷������������get����ʱ�õ��Ľ�Ϊnull
		query.findObjects(this, new FindListener<ShareMood>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("��ȡ���鶯̬ʧ�ܣ�");

			}

			@Override
			public void onSuccess(List<ShareMood> arg0) {
				// TODO Auto-generated method stub
				// ��handler��Ϊ�˷�����listView�е������ӵ���¼������µ�ǰActivity�ĵײ�������
				if (arg0.size() <= 0) {
					ShowToast("��ǰ�Ѿ�û�и��ද̬");
					return;
				}
				for (ShareMood sm : arg0) {
					list.add(sm);
				}
				adapter.setList(list);// ���Զ���������Դ
				currentPager++;// ҳ����1
			}
		});
		xlist_content.stopLoadMore();// ���Ӵ˾��һֱִ�м��ظ��ࣻ
	}

	// ��ʾ�����
	// public void showSoftInputView() {
	// if (getWindow().getAttributes().softInputMode ==
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
	// if (getCurrentFocus() != null)
	// ((InputMethodManager)
	// getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edit_user_comment,
	// 0);
	// }
	// }

}
