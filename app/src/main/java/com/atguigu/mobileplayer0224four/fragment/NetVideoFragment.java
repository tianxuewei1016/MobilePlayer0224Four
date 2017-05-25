package com.atguigu.mobileplayer0224four.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer0224four.R;
import com.atguigu.mobileplayer0224four.adapter.NetVideoAdapter;
import com.atguigu.mobileplayer0224four.base.BaseFragment;
import com.atguigu.mobileplayer0224four.bean.MediaItem;
import com.atguigu.mobileplayer0224four.utils.CacheUtils;
import com.atguigu.mobileplayer0224four.utils.Constant;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：田学伟 on 2017/5/25 10:13
 * QQ：93226539
 * 作用：网络视频
 */

public class NetVideoFragment extends BaseFragment {

    @InjectView(R.id.lv)
    ListView lv;
    @InjectView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @InjectView(R.id.tv_nodata)
    TextView tvNodata;

    private NetVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItems;
    /**
     * 是否加载更多,默认是false
     */
    private boolean isLoadMore = false;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_net_video_pager, null);
        ButterKnife.inject(this, view);
        setListener();
        return view;
    }

    private void setListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                isLoadMore = false;
                getDataFromNet();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                //这个字段不能放在下面
                isLoadMore = true;
                getDataFromNet();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        //联网之前直接去缓存的数据
        String json = CacheUtils.getString(mContext, Constant.NET_WORK_VIDEO);
        if (!TextUtils.isEmpty(json)) {
            //解析缓存的数据
            processData(json);
            Log.e("TAG", "解析缓存的数据==" + json);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        //网络请求的地址
        RequestParams request = new RequestParams(Constant.NET_WORK_VIDEO);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "xUtils联网成功==" + result);
                CacheUtils.putString(mContext,Constant.NET_WORK_VIDEO,result);
                processData(result);
                //下拉刷新结束
                if(!isLoadMore) {
                    //结束下拉刷新
                    refresh.finishRefresh();
                }else{
                    //把上拉隐藏
                    refresh.finishRefreshLoadMore();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "xUtils联网失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        if (!isLoadMore) {
            mediaItems = parsedJson(json);
            if (mediaItems != null && mediaItems.size() > 0) {
                tvNodata.setVisibility(View.GONE);
                adapter = new NetVideoAdapter(mContext, mediaItems);
                lv.setAdapter(adapter);
            } else {
                tvNodata.setVisibility(View.VISIBLE);
            }
        } else {
            //加载更多
            ArrayList<MediaItem> mediaItem = parsedJson(json);
            mediaItems.addAll(mediaItem);
            //刷新适配器
            adapter.notifyDataSetChanged();

        }
    }

    /**
     * 使用系统的接口解析数据
     *
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("trailers");


            for (int i = 0; i < jsonArray.length(); i++) {

                MediaItem mediaItem = new MediaItem();
                mediaItems.add(mediaItem);//添加到集合中
                JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                String name = jsonObjectItem.optString("movieName");
                mediaItem.setName(name);
                String desc = jsonObjectItem.optString("videoTitle");
                mediaItem.setDesc(desc);
                String url = jsonObjectItem.optString("url");
                mediaItem.setData(url);
                String hightUrl = jsonObjectItem.optString("hightUrl");
                mediaItem.setHeightUrl(hightUrl);
                String coverImg = jsonObjectItem.optString("coverImg");
                mediaItem.setImageUrl(coverImg);
                int videoLength = jsonObjectItem.optInt("videoLength");
                mediaItem.setDuration(videoLength);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
