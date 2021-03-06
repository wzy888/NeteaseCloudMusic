package com.imooc.imooc_voice.view.mine.radio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.R2;
import com.imooc.imooc_voice.view.discory.radio.detail.RadioDetailDelegate;
import com.imooc.lib_api.RequestCenter;
import com.imooc.lib_api.model.dj.DjSubListBean;
import com.imooc.lib_common_ui.delegate.NeteaseDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MineRadioDelegate extends NeteaseDelegate {


	@BindView(R2.id.rv_mine_sub_radio)
	RecyclerView mRvSubRadio;
	@BindView(R2.id.tv_mine_sub_title)
	TextView mTvSubRadioTitle;

	private MineRadioAdapter mAdapter;


	@Override
	public Object setLayout() {
		return R.layout.delegate_mine_radio;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
		RequestCenter.getSubRadioList(new DisposeDataListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onSuccess(Object responseObj) {
				DjSubListBean bean = (DjSubListBean) responseObj;
				List<DjSubListBean.DjRadios> djRadios = bean.getDjRadios();
				mTvSubRadioTitle.setText("我订阅的电台(" + djRadios.size() + ")");
				mAdapter = new MineRadioAdapter(djRadios);
				mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
						DjSubListBean.DjRadios entity = (DjSubListBean.DjRadios) baseQuickAdapter.getItem(i);
						getSupportDelegate().start(RadioDetailDelegate.newInstance(entity.getId()));
					}
				});
				mRvSubRadio.setAdapter(mAdapter);
				LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
					@Override
					public boolean canScrollVertically() {
						return false;
					}
				};

				mRvSubRadio.setLayoutManager(layoutManager);
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}


	@OnClick(R2.id.img_mine_radio_back)
	void onClickBack(){
		getSupportDelegate().pop();
	}

	static class MineRadioAdapter extends BaseQuickAdapter<DjSubListBean.DjRadios, BaseViewHolder>{

		private ImageLoaderManager manager;

		MineRadioAdapter(@Nullable List<DjSubListBean.DjRadios> data) {
			super(R.layout.item_mine_radio_content, data);
			manager = ImageLoaderManager.getInstance();
		}

		@Override
		protected void convert(@NonNull BaseViewHolder helper, DjSubListBean.DjRadios bean) {
			ImageView img = helper.getView(R.id.iv_item_radio_content_img);
			manager.displayImageForCorner(img, bean.getPicUrl(), 5);
			helper.setText(R.id.tv_item_radio_content_name, bean.getName());
			helper.setText(R.id.tv_item_radio_content_creator, "by " + bean.getDj().getNickname());
			helper.setText(R.id.tv_item_radio_content_last_program, bean.getLastProgramName());
		}
	}
}
