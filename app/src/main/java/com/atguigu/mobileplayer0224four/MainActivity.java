package com.atguigu.mobileplayer0224four;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.atguigu.mobileplayer0224four.base.BaseFragment;
import com.atguigu.mobileplayer0224four.fragment.LocalAudioFragment;
import com.atguigu.mobileplayer0224four.fragment.LocalVideoFragment;
import com.atguigu.mobileplayer0224four.fragment.NetAudioFragment;
import com.atguigu.mobileplayer0224four.fragment.NetVideoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;

    /**
     * Fragment页面的下标位置
     */
    private int position;
    /**
     * 缓存当前显示的Fragment,把之前实例化的东西缓存下来
     */
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        //Android6.0动态获取权限
        isGrantExternalRW(this);
        initFragment();
        initListenter();
    }

    private void initListenter() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_local_video:
                        position = 0;
                        break;
                    case R.id.rb_local_audio:
                        position = 1;
                        break;
                    case R.id.rb_net_audio:
                        position = 2;
                        break;
                    case R.id.rb_net_video:
                        position = 3;
                        break;
                }
                //根据位置得到相应的Fragment
                BaseFragment currentFragment = fragments.get(position);
                addFragment(currentFragment);
            }
        });
        //默认选中本地视频
        rg_main.check(R.id.rb_local_video);
    }

    private void addFragment(BaseFragment currentFragment) {
        //缓存不等于当前的
        if (tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //切换
            if (currentFragment != null) {
                if (!currentFragment.isAdded()) {
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    //如果没有添加过九天假
                    ft.add(R.id.fl_mainc_content, currentFragment);
                } else {
                    //把之前的隐藏
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    ft.show(currentFragment);
                }
                //提交事物
                ft.commit();
            }
            tempFragment = currentFragment;
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoFragment());//本地视频
        fragments.add(new LocalAudioFragment());//本地音乐
        fragments.add(new NetAudioFragment());//网络音乐
        fragments.add(new NetVideoFragment());//网络视频
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy");
    }
}
