package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.bean.CircleEntity;
import com.boyuanitsm.zhetengba.bean.ImageInfo;
import com.boyuanitsm.zhetengba.bean.MemberEntity;
import com.boyuanitsm.zhetengba.http.IZtbUrl;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子详情adapter
 * Created by bitch-1 on 2016/5/12.
 */
public class CirxqAdapter extends RecyclerView.Adapter<CirxqAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private Context context;
    private int isInCircle;
    private List<MemberEntity> list=new ArrayList<>();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public CirxqAdapter(Context context, List<MemberEntity> list, int isInCircle) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.isInCircle=isInCircle;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        CircleImageView mCir;
    }

    /**
     * itemClick接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_gl_cirxq, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mCir = (CircleImageView) view.findViewById(R.id.civ_hand);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (isInCircle!=0){
        if (list.size()==0){
            switch (i){
                case 0:
                    viewHolder.mCir.setImageResource(R.mipmap.cirxq_add);
                    if (mOnItemClickListener != null) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                            }
                        });
                    }
                    break;
                case 1:
                    viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                    if (mOnItemClickListener != null) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                            }
                        });
                    }
                    break;
            }
        }else if(list!=null&&list.size()>0) {
            if (list.size() <= 4) {
                if (i == list.size()) {
                    viewHolder.mCir.setImageResource(R.mipmap.cirxq_add);
                    if (mOnItemClickListener != null) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                            }
                        });
                    }
                } else if (i==(list.size()+1)){
                    viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                    if (mOnItemClickListener != null) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                            }
                        });
                    }
                } else {
                    if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                        ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(i).getIcon(), viewHolder.mCir, options);
                    }
//                viewHolder.mCir.setImageResource(list.get(i));
                }
            } else {
                switch (i) {
                    case 0:
                        if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                            ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(0).getIcon(), viewHolder.mCir, options);
                        }
//                    viewHolder.mCir.setImageResource(list.get(0));
                        break;
                    case 1:
                        if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                            ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(1).getIcon(), viewHolder.mCir, options);
                        }
//                    viewHolder.mCir.setImageResource(list.get(1));
                        break;
                    case 2:
                        if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                            ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(2).getIcon(), viewHolder.mCir, options);
                        }
//                    viewHolder.mCir.setImageResource(list.get(2));
                        break;
                    case 3:
                        if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                            ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(3).getIcon(), viewHolder.mCir, options);
                        }
//                    viewHolder.mCir.setImageResource(list.get(3));
                        break;
                    case 4:
                        viewHolder.mCir.setImageResource(R.mipmap.cirxq_add);
                        if (mOnItemClickListener != null) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                                }
                            });
                        }
                        break;
                    case 5:
                        viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                        if (mOnItemClickListener != null) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                                }
                            });
                        }
                        break;
                }

            }
        }
        }else {
            //0不再圈子里，不现实加号
            if (list.size()==0){
                switch (i){
                    case 0:
                        viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                        if (mOnItemClickListener != null) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                                }
                            });
                        }
                        break;
                }
            }else if(list!=null&&list.size()>0) {
                if (list.size() <= 4) {
                    if (i == list.size()) {
                        viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                        if (mOnItemClickListener != null) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                                }
                            });
                        }
                    } else {
                        if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                            ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(i).getIcon(), viewHolder.mCir, options);
                        }
//                viewHolder.mCir.setImageResource(list.get(i));
                    }
                } else {
                    switch (i) {
                        case 0:
                            if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                                ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(0).getIcon(), viewHolder.mCir, options);
                            }
//                    viewHolder.mCir.setImageResource(list.get(0));
                            break;
                        case 1:
                            if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                                ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(1).getIcon(), viewHolder.mCir, options);
                            }
//                    viewHolder.mCir.setImageResource(list.get(1));
                            break;
                        case 2:
                            if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                                ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(2).getIcon(), viewHolder.mCir, options);
                            }
//                    viewHolder.mCir.setImageResource(list.get(2));
                            break;
                        case 3:
                            if(!TextUtils.isEmpty(list.get(i).getIcon())) {
                                ImageLoader.getInstance().displayImage(IZtbUrl.BASE_URL + list.get(3).getIcon(), viewHolder.mCir, options);
                            }
//                    viewHolder.mCir.setImageResource(list.get(3));
                            break;
                        case 4:
                            viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
                            if (mOnItemClickListener != null) {
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mOnItemClickListener.onItemClick(viewHolder.itemView, i);
                                    }
                                });
                            }
                            break;
//                        case 5:
//                            viewHolder.mCir.setImageResource(R.mipmap.cirxq_more);
//                            if (mOnItemClickListener != null) {
//                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mOnItemClickListener.onItemClick(viewHolder.itemView, i);
//                                    }
//                                });
//                            }
//                            break;
                    }

                }
            }
        }


    }


    @Override
    public int getItemCount() {
        if (isInCircle!=0) {
            return list.size() <= 4 ? (list.size() + 2) : 6;
        }else {
            return list.size() <= 4 ? (list.size() + 1) : 5;
        }
    }
}
