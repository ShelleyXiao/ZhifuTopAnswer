package com.xyl.zhifutopanser.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.xyl.architectrue.rxsupport.RxAppCompatActivity;
import com.xyl.architectrue.utils.LogUtils;
import com.xyl.zhifutopanser.R;
import com.xyl.zhifutopanser.fragment.BaseFragment;
import com.xyl.zhifutopanser.fragment.CollectionFragment;
import com.xyl.zhifutopanser.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends RxAppCompatActivity {

    private List<BaseFragment> mFragments = new ArrayList<>();
    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;

    private int mCheckPosition = 0;
    private int mLastCheckPosition = -1;

    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initTitle();
        initBottomNaviagtion();

        itemChecked();
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void initBottomNaviagtion() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_nav);
        mBottomNavigationView.addTab(new BottomNavigationItem(getString(R.string.topic), R.color.colorPrimary, R.drawable.ic_topic));
        mBottomNavigationView.addTab(new BottomNavigationItem(getString(R.string.save),R.color.colorPrimary, R.drawable.ic_save));
        mBottomNavigationView.setOnBottomNavigationItemClickListener((index) -> {
            if(index == mCheckPosition) {
                return;
            } else {
                mLastCheckPosition = mCheckPosition;
                mCheckPosition = index;
                itemChecked();
            }
        });
    }

    private void initTitle() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setTitle(R.string.app_name);
    }

    private void initFragment() {
        mFragments.add(MainFragment.newInstance());
        mFragments.add(CollectionFragment.newInstance());
    }

    private void itemChecked() {
        LogUtils.e("mCheckPostion" + mCheckPosition);
        if(mCheckPosition >= 0) {
            switchFragment(mLastCheckPosition >= 0 ? mFragments.get(mLastCheckPosition): null, mFragments.get(mCheckPosition));
        }
    }

    public void switchFragment(BaseFragment from , BaseFragment to) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(from == null) {
            if(to != null) {
                ft.replace(R.id.content, to);
                ft.commit();
                LogUtils.e("***********switchFragment");
            }
        } else {
            if(!to.isAdded()) {
                ft.hide(from)
                        .add(R.id.content, to)
                        .commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }

    private void exitBy2Click() {
        Timer exit;
        if(false == isExit) {
            isExit = true;
            toast(getString(R.string.exit_two_click));
            exit = new Timer();
            exit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 3000);


        } else {
            this.finish();
        }
    }
}
