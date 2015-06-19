package com.pointrestapp.pointrest.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;

import com.pointrestapp.pointrest.Constants;
import com.pointrestapp.pointrest.R;
import com.pointrestapp.pointrest.adapters.TabAdapter;
import com.pointrestapp.pointrest.data.PuntiContentProvider;
import com.pointrestapp.pointrest.fragments.FragmentListFrame;
import com.pointrestapp.pointrest.fragments.FragmentMap;
import com.pointrestapp.pointrest.fragments.FragmentTitleScreen;

public class MainScreenActivity extends BaseActivity implements
		TabAdapter.TabSelectedListener,
		FragmentListFrame.Callback {

	private static final String TAG_MAP_SCREEN = "TAG_MAP_SCREEN";
	private static final String TAG_TITLE_SCREEN = "TAG_TITLE_SCREEN";

	private FragmentTitleScreen mTitleScreenFragment;	
	private FragmentMap mMapFragment;
	
    private ContentObserver mObserver;
    private boolean mInitialized = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences vPintrestPreferences =
				this.getSharedPreferences(Constants.POINTREST_PREFERENCES, Context.MODE_PRIVATE);
		
		if (vPintrestPreferences.getBoolean(Constants.RAN_FOR_THE_FIRST_TIME, false))
			initializeScreen(savedInstanceState);
		
		Bundle b = null;
		if (savedInstanceState != null)
			b = (Bundle) savedInstanceState.clone();
		final Bundle bFinal = b;
		
		//The first time the app is launched, we must wait until the Categories are populated
		//before we initialize the viewPager. We know PUNTI get populated for last
		//so we listen to those changes
		//Maybe we should add a dummy uri to avvise us?
		mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
			@Override
			public void onChange(boolean selfChange, Uri uri) {
				super.onChange(selfChange, uri);
				if (!mInitialized)
					initializeScreen(bFinal);
				else {
					mMapFragment.updateMarkers();
				}
			}
		};
        getContentResolver().registerContentObserver(PuntiContentProvider.DUMMY_NOTIFIER_URI, false, mObserver);
	}

	protected void initializeScreen(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			mTitleScreenFragment = FragmentTitleScreen.getInstance();
			mMapFragment = FragmentMap.getInstance(0);
			getFragmentManager().beginTransaction()
				.add(R.id.container, mMapFragment, TAG_MAP_SCREEN)
				.add(R.id.container, mTitleScreenFragment, TAG_TITLE_SCREEN)
				.commit();
			
		} else {
			mTitleScreenFragment = (FragmentTitleScreen) getFragmentManager().findFragmentByTag(TAG_TITLE_SCREEN);
			mMapFragment = (FragmentMap) getFragmentManager().findFragmentByTag(TAG_MAP_SCREEN);
		}
		mInitialized = true;
	}

	@Override
	public void onTabSelected(int puntoType) {
		mMapFragment.onTabSelected(puntoType);
		mTitleScreenFragment.onTabSelected(puntoType);
	}

	@Override
	public void goToDetailScreen(int pointId) {
		
	}
	
	@Override
	public void goToMapScreen(float x, float y) {
		//mMapFragment.prepareForShow(x, y);
		getFragmentManager().beginTransaction()
		.hide(mTitleScreenFragment)
		.addToBackStack(null)
		.commit();
	}
	
	/**
	 * The two Fragments need to know when back is pressed to undo
	 * the transformations 
	 */
	@Override
	public void onBackPressed() {
		mMapFragment.onBackPressed();
		mTitleScreenFragment.OnBackPressed();
		super.onBackPressed();
	}

	@Override
	public void goToMapScreen(MotionEvent event) {
		mMapFragment.prepareForShow(event);
		getFragmentManager().beginTransaction()
		.hide(mTitleScreenFragment)
		.addToBackStack(null)
		.commit();
	}

}
