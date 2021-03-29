package com.airAd.yaqinghui.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.AccountService;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.Constants;
/**
 * 登陆Fragment
 * 
 * @author Panyi
 */
public class LoginFragment extends Fragment
{
	private EditText mUserText;
	private EditText mPwdText;
	private Button mLogin;
	private ProgressDialog progressDialog;
	private DoLoginTask loginTask;
	private boolean isLogining= false;
	public static LoginFragment newInstance()
	{
		LoginFragment fragment= new LoginFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	@Override
	public void onStop()
	{
		if (loginTask != null)
		{
			loginTask.cancel(true);
		}
		super.onStop();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mUserText= (EditText) getActivity().findViewById(R.id.user_text);
		mPwdText= (EditText) getActivity().findViewById(R.id.pwd_text);
		mLogin= (Button) getActivity().findViewById(R.id.login);
		mLogin.setOnClickListener(new LoginClick());
		progressDialog= new ProgressDialog(getActivity());
		progressDialog.setMessage(getActivity().getString(R.string.islogining));
	}
	private final class LoginClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
				Info info= new Info();
				info.user= mUserText.getText().toString().trim();
				info.pwd= mPwdText.getText().toString().trim();
				//				info.user= "200970";
				//				info.pwd= "032747";   111111
				loginTask= new DoLoginTask();
				//loginTask.execute(info);

				User user = new User();
				user.setName(info.user);
				user.setTemp("siwangqishiq");
				user.setFlag(Constants.FLAG_SUCC);
				loginTask.onPostExecute(user);
		}
	}

	private final class Info
	{
		public String user;
		public String pwd;
	}

	private final class DoLoginTask extends AsyncTask<Info, Void, User>
	{
		@Override
		protected void onPreExecute()
		{
			isLogining= true;
			super.onPreExecute();
			progressDialog.show();
		}
		@Override
		protected User doInBackground(Info... params)
		{
			Info info= params[0];
			User user = new User();
			user.setName(info.user);
			user.setTemp("siwangqishiq");
			user.setFlag(Constants.FLAG_SUCC);
			return user;
		}
		@Override
		protected void onPostExecute(User result)
		{
			super.onPostExecute(result);
			isLogining= false;
			progressDialog.dismiss();
			if (result != null)
			{

				if (Constants.FLAG_SUCC.equals(result.getFlag()))
				{
					SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
					sp.edit().putString(Config.USER_INFO_KEY, result.getTemp()).commit();
					MyApplication.getCurrentApp().setUser(result);
					Intent intent= new Intent(getActivity(), HomeActivity.class);
					getActivity().startActivity(intent);
					getActivity().finish();
				}
				else
				{
					Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
			}





		}
	}
}// end class
