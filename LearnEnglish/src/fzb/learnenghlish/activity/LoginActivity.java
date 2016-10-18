package fzb.learnenghlish.activity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.example.learnenglish.R;

import fzb.learnenglish.entity.MyUser;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 登录界面
 */

public class LoginActivity extends Activity {
	
	private EditText et_account;
	private EditText et_pwd;
	private CheckBox cb_remeber;
	private TextView tv_forget;
	private TextView tv_register;
	private Button bn_login;
	
	String account;
	String pwd;
	MyUser user;
	
	SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		
		tv_register=(TextView) findViewById(R.id.tv_register);
		bn_login=(Button) findViewById(R.id.bn_login);
		et_account=(EditText) findViewById(R.id.et_account);
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		cb_remeber=(CheckBox) findViewById(R.id.cb_remeber);
		
		sp=this.getSharedPreferences("user", MODE_PRIVATE);
		
		Bmob.initialize(this, "a20f25b9f4d3aec8b9484d742e88c047");
		
		tv_register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i=new Intent();
				
				i.setAction(getResources().getString(R.string.RegisterActivity));
				i.addCategory(Intent.CATEGORY_DEFAULT);
				startActivity(i);
			}
			
		});
		
		bn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				account=et_account.getText().toString();
				pwd=et_pwd.getText().toString();
				
				user=new MyUser();
				user.setUsername(account);
				user.setPassword(pwd);
				
				user.login(LoginActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
						
						SaveUser();
						Intent intent=new Intent(LoginActivity.this,PersonActivity.class);
						
						intent.putExtra("username", account);
						startActivity(intent);
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		if(sp.getBoolean("isLogin", false)){
			Intent intent=new Intent(LoginActivity.this,PersonActivity.class);
			
			intent.putExtra("username", sp.getString("username", null));
			startActivity(intent);
			finish();
		}
			
		
		AutoLogin();
		
	}
	
	/**
	 * 保存用户配置信息
	 */
	public void SaveUser(){
		
		Editor editor=sp.edit();
		
		editor.putString("username", account);
		editor.putString("session", user.getSessionToken());
		editor.putBoolean("auto", cb_remeber.isChecked());
		editor.putBoolean("isLogin", true);
		
		editor.commit();
	}
	
	/**
	 * 自动登录
	 */
	public void AutoLogin(){
		
		if(sp!=null&&sp.getBoolean("auto", false)){
			user=new MyUser();
			
			user.setUsername(sp.getString("username", null));
			user.setPassword(sp.getString("pwd", null));
			
			user.login(LoginActivity.this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					
					SaveUser();
					Intent intent=new Intent(LoginActivity.this,PersonActivity.class);
					
					intent.putExtra("username", account);
					startActivity(intent);
					finish();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
