package com.airAd.yaqinghui;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.fragment.CepItemFragment;
import com.airAd.yaqinghui.ui.IndexView;
/**
 * CEP活动
 * 
 * @author Panyi
 */
public class CepActivity extends BaseActivity
{
	private ImageButton mBack;
	private ImageButton mShakeBtn;
	private ViewPager mGallery;
	private RequestTask mTask;
	private List<Cep> ceps;
	private IndexView mIndexView;
	private ProgressDialog progressDialog;
	private User mUser;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cep);
		init();
	}
	private void init()
	{
		mUser= MyApplication.getCurrentUser();
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		mShakeBtn= (ImageButton) findViewById(R.id.search_btn);
		mGallery= (ViewPager) findViewById(R.id.gallery);
		mIndexView= (IndexView) findViewById(R.id.gallery_index);
		request();
	}
	private void request()
	{
		progressDialog= new ProgressDialog(this);
		progressDialog.setTitle(R.string.dialog_title);
		progressDialog.setMessage(getResources().getText(R.string.dialog_msg));
		progressDialog.setCancelable(true);
		progressDialog.show();
		mTask= new RequestTask();
		mTask.execute(0);
	}
	private final class PageAdapter extends FragmentStatePagerAdapter
	{
		public PageAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int index)
		{
			return CepItemFragment.newInstance(ceps.get(index));
		}
		@Override
		public int getCount()
		{
			return ceps.size();
		}
	}// end inner class
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			CepActivity.this.finish();
		}
	}// end inner class
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mTask != null)
		{
			mTask.cancel(true);
		}
	}
	private final class RequestTask extends AsyncTask<Integer, Integer, List<Cep>>
	{
		@Override
		protected List<Cep> doInBackground(Integer... params)
		{
			return new CepService().getCeps(MyApplication.getCurrentUser().getId());
		}
		@Override
		protected void onPostExecute(List<Cep> result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			ceps= result;
			if (ceps == null)
			{
				Toast.makeText(CepActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
				return;
			}
			mGallery.setAdapter(new PageAdapter(getSupportFragmentManager()));
			mShakeBtn.setOnClickListener(new DoShakeSearch());
			mGallery.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int index)
				{
					mIndexView.setPoint(index);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
				}
				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}
			});
			mIndexView.setNum(ceps.size());
			mIndexView.setPoint(0);
		}
	}// end inner class
	private final class DoShakeSearch implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent(CepActivity.this, ShakeActivity.class);
			MyApplication.getCurrentApp().push(ceps);
			CepActivity.this.startActivity(it);
		}
	}//end inner class
}// end class
