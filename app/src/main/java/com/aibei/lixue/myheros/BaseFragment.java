package com.aibei.lixue.myheros;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * 基础Fragment
 * Created by Administrator on 2016/12/2.
 */

public class BaseFragment extends Fragment {
    /**在surpport24.0.0的版本上已经修复了这个bug**/
    //使用hide() show()时，都是使用add（）方式加载Fragment的，add配合hide使Fragment的视图改变为GONE状态；
    //而replace（）是销毁Fragment的视图。页面重启的时候，add会全部走Fragment的生命周期，创建视图
    //而replace()的非栈顶Fragment不会走生命周期，只有back的时候，会逐一走栈顶Fragemnt的全部生命周期
    //当“内存重启”的时候，重新走一遍Fragment的生命周期，我们之前hide的Fragment就会显示出来了，因为默认的mhidden值是false,所以重叠现象
    //另外一个原因，重复使用了add replace Fragment，没有判断savedInstanceState所以，导致多个Fragment重叠
        private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";//保存显示Fragment的状态值

        protected Activity mActivity;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //重复使用add replace Fragment，会造成Fragment的重叠
            if (savedInstanceState != null){
                // 这里replace或add 根Fragment
                boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (isSupportHidden){
                    ft.hide(this);
                }else {
                    ft.show(this);
                }
                ft.commit();
            }else {
                //在这里加载Fragment，Fragment中的onCreateView等生命周期里加载根子Fragment
            }
//            getFragmentManager().beginTransaction().setCustomAnimations(enter, exit)  设置动画
            // 如果你有通过tag/id同时出栈多个Fragment的情况时，
            // 请谨慎使用.setCustomAnimations(enter, exit, popEnter, popExit)
            // 因为在出栈多Fragment时，伴随出栈动画，会在某些情况下发生异常
            // 你需要搭配Fragment的onCreateAnimation()临时取消出栈动画，或者延迟一个动画时间再执行一次上面提到的Hack方法，排序

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            //不建议使用getActivity()，以防万一出现getActivity为空指针（有可能内存重启的时候，Activity会
            // 情况：有可能你的Fragment已经onDetach()了宿主activity,此时你刚刚pop）了这个Fragment，
            // 那么你在这个Fragment中getActivity（）就会报空指针异常
            this.mActivity = (Activity) context;
        }
}
