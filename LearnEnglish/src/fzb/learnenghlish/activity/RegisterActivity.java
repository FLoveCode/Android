package fzb.learnenghlish.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.example.learnenglish.R;

import fzb.learnenglish.entity.MyUser;

public class RegisterActivity extends Activity {
	
	EditText et_username;
	EditText et_userpwd;
	EditText et_confirm;
	EditText et_email;
	EditText et_phone;
//	EditText et_check;
	Button bn_check;
	Button bn_register;
	
	String username;
	String pwd;
	String confirm;
	String email;
	String phone;
//	String check;
	
	MyUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_layout);
		
		et_username=(EditText) findViewById(R.id.et_username);
		et_userpwd=(EditText) findViewById(R.id.et_userpwd);
		et_confirm=(EditText) findViewById(R.id.et_confirm);
		et_email=(EditText) findViewById(R.id.et_email);
		et_phone=(EditText) findViewById(R.id.et_phone);
//		et_check=(EditText) findViewById(R.id.et_check);
		bn_register=(Button) findViewById(R.id.bn_register);
		
		Bmob.initialize(this, "a20f25b9f4d3aec8b9484d742e88c047");
		
		bn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckInfo()){
					
					user=new MyUser();
					
					user.setUsername(username);
					user.setPassword(pwd);
					user.setEmail(email);
					user.setPhone(phone);
					
					user.signUp(RegisterActivity.this, new SaveListener(){

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess() {
							Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
							
							Intent intent=new Intent(RegisterActivity.this,PersonActivity.class);
							
							intent.putExtra("username", username);
							intent.putExtra("pwd", pwd);
							startActivity(intent);
							finish();
						}
						
					});
					
				}
			}
			
		});
		
	}
	
	/**
	 * 检查是否合法输入
	 * @return 是否合法输入
	 */
	public boolean CheckInfo(){
	
		username=et_username.getText().toString();
		pwd=et_userpwd.getText().toString();
		confirm=et_confirm.getText().toString();
		email=et_email.getText().toString();
		phone=et_phone.getText().toString();
//		check=et_check.getText().toString();
	
		if(!username.matches("^[a-zA-Z][a-zA-Z0-9]{3,7}$")){
			
			Toast.makeText(RegisterActivity.this, "用户名错误", Toast.LENGTH_SHORT).show();
			return false;
			}
		
		if(!(pwd.equals(confirm)&&pwd.matches("^[a-zA-Z0-9]{8}$"))){
			
			Toast.makeText(RegisterActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
			return false;
			}
		
		if(!email.matches("^([a-zA-Z0-9]+[-|_|\\.]?)+[a-zA-Z0-9]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[A-Za-z]{2,}$")){
			
			Toast.makeText(RegisterActivity.this, "邮箱错误", Toast.LENGTH_SHORT).show();
			return false;
		    }
		
		if(!phone.matches("^[1]([3][0-9]{1}|59|58|83|88|89)[0-9]{8}$")){
			
			Toast.makeText(RegisterActivity.this, "请输入有效的电话号码", Toast.LENGTH_SHORT).show();
			return false;			
		    }
		
		return true;
	}

}
