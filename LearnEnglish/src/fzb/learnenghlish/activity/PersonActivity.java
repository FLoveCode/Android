package fzb.learnenghlish.activity;

import java.util.List;

import org.json.JSONArray;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.learnenglish.R;

import fzb.learnenglish.entity.MyUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * �û�����
 */
public class PersonActivity extends Activity {

	TextView tv_account;
	TextView tv_email;
	TextView tv_phone;
	TextView tv_modify;
	TextView tv_look;
	Button bn_logout;
	
	SharedPreferences sp;
	MyUser user=null;
	
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_layout);
		
		tv_account=(TextView) findViewById(R.id.tv_account);
		tv_email=(TextView) findViewById(R.id.tv_email);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		tv_modify=(TextView) findViewById(R.id.tv_modify);
		tv_look=(TextView) findViewById(R.id.tv_look);
		bn_logout=(Button) findViewById(R.id.bn_logout);
		
		sp=this.getSharedPreferences("user",MODE_PRIVATE);
		Intent i=getIntent();
		
		username=i.getStringExtra("username");
		tv_account.setText(username);
		
		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
		
		query.addWhereEqualTo("username", username);
		
		query.findObjects(PersonActivity.this, new FindListener<MyUser>(){

			@Override
			public void onError(int arg0, String arg1) {
				Log.d(getPackageName(), "��ѯʧ��"+arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				Log.d(getPackageName(), arg0.toString());
				
				user=arg0.get(0);
				
				tv_email.setText(user.getEmail());
				tv_phone.setText(user.getPhone());
			}
			
		});
		
		
	}

	
	/**
	 * �˳���¼
	 */
	public void DoLogout(View v){
		
		Log.d(getPackageName(), "�˳�");
		
		BmobUser.logOut(PersonActivity.this);
		Editor editor=sp.edit();
		
		editor.putBoolean("isLogin", false);
		editor.putBoolean("auto", false);
		
		editor.putString("username", null);
		editor.putString("session", null);
		
		editor.commit();
		finish();
	}
	
	/**
	 * �޸ĵ绰����
	 */
	public void update_phone(View v){
	
	AlertDialog.Builder builder=new Builder(PersonActivity.this);
	AlertDialog dialog;
	final EditText et=new EditText(PersonActivity.this);
	
	builder.setMessage("������绰����");
	builder.setView(et);
	builder.setNegativeButton("ȡ��", null);
	builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {
		
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			final String newPhone=et.getText().toString();
			user.setPhone(newPhone);
			user.setSessionToken(sp.getString("session", null));
			
			user.update(PersonActivity.this, user.getObjectId(),new UpdateListener(){

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(PersonActivity.this, "�޸ĵ绰����ʧ��", Toast.LENGTH_SHORT).show();
					Log.d(getPackageName(), arg1);
				}

				@Override
				public void onSuccess() {
					Toast.makeText(PersonActivity.this, "�޸ĵ绰����ɹ�", Toast.LENGTH_SHORT).show();
					tv_phone.setText(newPhone);
				}
				
			});
			
			dialog.dismiss();
		}
	});
	
	dialog=builder.create();
	dialog.show();
	
	}
	
	/**
	 * �޸ĵ����ʼ�
	 */
	public void update_email(View view){
		AlertDialog.Builder builder=new Builder(PersonActivity.this);
		AlertDialog dialog;
		final EditText et=new EditText(PersonActivity.this);
		
		builder.setMessage("����������ʼ�");
		builder.setView(et);
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				final String newEmail=et.getText().toString();
				user.setEmail(newEmail);
				user.setSessionToken(sp.getString("session", null));
				
				user.update(PersonActivity.this, user.getObjectId(),new UpdateListener(){

					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(PersonActivity.this, "�޸ĵ����ʼ�ʧ��", Toast.LENGTH_SHORT).show();
						Log.d(getPackageName(), arg1);
					}

					@Override
					public void onSuccess() {
						Toast.makeText(PersonActivity.this, "�޸ĵ����ʼ��ɹ�", Toast.LENGTH_SHORT).show();
						tv_email.setText(newEmail);
					}
					
				});
				
				dialog.dismiss();
			}
		});
		
		dialog=builder.create();
		dialog.show();
	}
	
	/**
	 * �޸�����
	 */
	public void modify_pwd(View view){
		AlertDialog.Builder builder=new Builder(PersonActivity.this);
		AlertDialog dialog;
		final EditText et=new EditText(PersonActivity.this);
		
		builder.setMessage("�������µ�����");
		builder.setView(et);
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				final String newPwd=et.getText().toString();
				user.setPassword(newPwd);
				user.setSessionToken(sp.getString("session", null));
				
				user.update(PersonActivity.this, user.getObjectId(),new UpdateListener(){

					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(PersonActivity.this, "�޸�����ʧ��", Toast.LENGTH_SHORT).show();
						Log.d(getPackageName(), arg1);
					}

					@Override
					public void onSuccess() {
						Toast.makeText(PersonActivity.this, "�޸�����ɹ�", Toast.LENGTH_SHORT).show();
					}
					
				});
				
				dialog.dismiss();
			}
		});
		
		dialog=builder.create();
		dialog.show();
	}
	
	/**
	 * �鿴���ʱ�
	 */
	public void look_words(View v){
		
		Intent intent=new Intent(PersonActivity.this,WordsBookActivity.class);
		
		startActivity(intent);
		finish();
	}
}
