package com.upc.learnmooc.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.ArticleList;

import java.util.List;

/**
 * RecyclerView适配器
 * Created by Explorer on 2016/4/4.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

	private List<?> mData;
	private final BitmapUtils bitmapUtils;

	public RecyclerAdapter(List<?> data, Context context) {
		mData = data;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_artilce);//默认加载图
	}

	public OnItemClickListener itemClickListener;

	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		public LinearLayout mLayout;
		public TextView tvTitle;
		public TextView tvClassify;
		public TextView tvNum;
		public TextView tvDetail;
		public ImageView ivPic;

		public ViewHolder(View itemView) {
			super(itemView);
			mLayout = (LinearLayout) itemView;
			tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
			tvClassify = (TextView) itemView.findViewById(R.id.tv_classify_article);
			tvNum = (TextView) itemView.findViewById(R.id.tv_num);
			tvDetail = (TextView) itemView.findViewById(R.id.tv_detail);
			ivPic = (ImageView) itemView.findViewById(R.id.iv_cover_img);
			mLayout.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (itemClickListener != null) {
				itemClickListener.onItemClick(v, getPosition());
			}
		}
	}

	@Override
	public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_detail_listview, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
		ArticleList.ArticleInfo articleInfo = (ArticleList.ArticleInfo) mData.get(position);

		holder.tvTitle.setText(articleInfo.getTitle());
		holder.tvClassify.setText(articleInfo.getClassify());
		holder.tvNum.setText(articleInfo.getNum() + "");
		bitmapUtils.display(holder.ivPic, articleInfo.getImg());

		//设置文章内容部分展示的填充
		//又后台获取html内容 解析后返回给前端
		//直接又前端解析 耗时大 显示延迟严重
		holder.tvDetail.setText(articleInfo.getAbstractInfo());
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}
}
