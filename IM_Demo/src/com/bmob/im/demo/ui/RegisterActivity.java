package com.bmob.im.demo.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.sms.*;
import cn.bmob.sms.bean.*;
import cn.bmob.sms.exception.*;
import cn.bmob.sms.listener.*;
import cn.bmob.v3.listener.FindListener;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


import com.bmob.im.demo.R;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.config.BmobConstants;
import com.bmob.im.demo.util.CommonUtils;


public class RegisterActivity extends BaseActivity {

	Button btn_register,btn_getCheckCode;
	EditText et_checkcode,et_password, et_password2,et_phone;
	RadioButton rb_sex_male,rb_sex_female;
	BmobChatUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initTopBarForLeft("ע��");
		et_checkcode = (EditText) findViewById(R.id.activity_register_checkcode);
		et_password = (EditText) findViewById(R.id.activity_register_password);
		et_password2 = (EditText) findViewById(R.id.activity_register_password2);
		et_phone = (EditText)findViewById(R.id.activity_register_username);
		
		rb_sex_male=(RadioButton)findViewById(R.id.activity_register_sex_male);
		rb_sex_female=(RadioButton)findViewById(R.id.activity_register_sex_female);
		
		btn_register = (Button) findViewById(R.id.activity_register_button_ok);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				register();
			}
		});
		
		btn_getCheckCode=(Button)findViewById(R.id.activity_register_getCheckCode);
		btn_getCheckCode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String number = et_phone.getText().toString();
				if(!TextUtils.isEmpty(number)){
					BmobSMS.requestSMSCode(RegisterActivity.this, number, "ע��ģ��",new RequestSMSCodeListener() {
						@Override
						public void done(Integer smsId,BmobException ex) {
							// TODO Auto-generated method stub
							if(ex==null){//��֤�뷢�ͳɹ�
								ShowToast("��֤�뷢�ͳɹ�������id��"+smsId);//���ڲ�ѯ���ζ��ŷ�������
							}
						}
					});
				}else{
					ShowToast("�������ֻ�����");
				}
			}
		});
		checkUser();
	}
	
	
	private void checkUser(){
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", "smile");
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if(arg0!=null && arg0.size()>0){
					User user = arg0.get(0);
					user.setPassword("1234567");
					user.update(RegisterActivity.this, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							userManager.login("smile", "1234567", new SaveListener() {
								
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									Log.i("smile", "��½�ɹ�");
								}
								
								@Override
								public void onFailure(int code, String msg) {
									// TODO Auto-generated method stub
									Log.i("smile", "��½ʧ�ܣ�"+code+".msg = "+msg);
								}
							});
						}
						
						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
	}
	
	private void register(){
		//ȡ���û��������롢ȷ������
		String phone = et_phone.getText().toString();
		String password = et_password.getText().toString();
		String pwd_again = et_password2.getText().toString();
		String checkcode = et_checkcode.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}
		if (!pwd_again.equals(password)) {
			ShowToast(R.string.toast_error_comfirm_password);
			return;
		}
		
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if(!isNetConnected){
			ShowToast(R.string.network_tips);
			return;
		}
		
		
		//��֤��֤���Ƿ���ȷ
		if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(checkcode)){
			BmobSMS.verifySmsCode(RegisterActivity.this,phone,checkcode, 
					new VerifySMSCodeListener() {
				@Override
				public void done(BmobException ex) {
					// TODO Auto-generated method stub
					if(ex==null){//������֤������֤�ɹ�
						ShowToast("��֤ͨ��");
					}else{
						ShowToast("��֤ʧ�ܣ�code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
					}
				}
			});
		}

		
		final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("����ע��...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		//����ÿ��Ӧ�õ�ע����������϶���һ������IM sdkδ�ṩע�᷽�����û��ɰ���bmod SDK��ע�᷽ʽ����ע�ᡣ
		//ע���ʱ����Ҫע�����㣺1��User���а��豸id��type��2���豸���а�username�ֶ�
		final User bu = new User();
		bu.setUsername(phone);
		bu.setPassword(password);
		if(rb_sex_male.isChecked())
		{
			bu.setSex(true);
		}
		else
		{
			bu.setSex(false);
		}
		bu.setPhone(phone);
		//��user���豸id���а�aa
		
		bu.setDeviceType("android");
		bu.setInstallId(BmobInstallation.getInstallationId(this));
		bu.signUp(RegisterActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				progress.dismiss();
				ShowToast("ע��ɹ�");
				// ���豸��username���а�
				userManager.bindInstallationForRegister(bu.getUsername());
				//���µ���λ����Ϣ
				updateUserLocation();
				//���㲥֪ͨ��½ҳ���˳�
				sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
				// ������ҳ
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				BmobLog.i(arg1);
				ShowToast("ע��ʧ��:" + arg1);
				progress.dismiss();
			}
		});
	}
	

	

}
