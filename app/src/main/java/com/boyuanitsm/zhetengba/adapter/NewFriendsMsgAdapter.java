package com.boyuanitsm.zhetengba.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.PersonalAct;
import com.boyuanitsm.zhetengba.bean.ChatUserBean;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.UserInfo;
import com.boyuanitsm.zhetengba.chat.db.InviteMessgeDao;
import com.boyuanitsm.zhetengba.chat.domain.InviteMessage;
import com.boyuanitsm.zhetengba.chat.domain.InviteMessage.InviteMesageStatus;
import com.boyuanitsm.zhetengba.db.ChatUserDao;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Date;
import java.util.List;

//import com.hyphenate.easeui.widget.CircleImageView;

/**
 * Created by wangbin on 16/5/13.
 */
public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

    private Context context;
    private InviteMessgeDao messgeDao;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        messgeDao = new InviteMessgeDao(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
            holder.avator = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.reason = (TextView) convertView.findViewById(R.id.message);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.agree = (Button) convertView.findViewById(R.id.agree);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvState);
            holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
            holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.refuse= (Button) convertView.findViewById(R.id.refuse);
            holder.llState= (RelativeLayout) convertView.findViewById(R.id.llState);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String str1 = context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
        String str2 = context.getResources().getString(R.string.agree);

        String str3 = context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
        String str4 = context.getResources().getString(R.string.Apply_to_the_group_of);
        String str5 = context.getResources().getString(R.string.Has_agreed_to);
        String str6 = context.getResources().getString(R.string.Has_refused_to);

        String str7 = context.getResources().getString(R.string.refuse);
        String str8 = context.getResources().getString(R.string.invite_join_group);
        String str9 = context.getResources().getString(R.string.accept_join_group);
        String str10 = context.getResources().getString(R.string.refuse_join_group);

        final InviteMessage msg = getItem(position);
        if (msg != null) {
            holder.llState.setVisibility(View.GONE);
//            holder.agree.setVisibility(View.INVISIBLE);

            if (msg.getGroupId() != null) { // 显示群聊提示
                holder.groupContainer.setVisibility(View.GONE);
                holder.groupname.setText(msg.getGroupName());
//                holder.name.setText(msg.getFrom());
            } else {
                holder.groupContainer.setVisibility(View.GONE);
            }

            holder.reason.setText(msg.getReason());

            holder.time.setText(DateUtils.getTimestampString(new
                    Date(msg.getTime())));
            if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
                holder.tvStatus.setVisibility(View.INVISIBLE);
                holder.reason.setText(str1);
            } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMesageStatus.BEAPPLYED ||
                    msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                holder.llState.setVisibility(View.VISIBLE);
//                holder.agree.setVisibility(View.VISIBLE);
//                holder.agree.setEnabled(true);
//                holder.agree.setBackgroundResource(android.R.drawable.btn_default);
//                holder.agree.setText(str2);

//                holder.status.setVisibility(View.VISIBLE);
//                holder.status.setEnabled(true);
//                holder.status.setBackgroundResource(android.R.drawable.btn_default);
//                holder.status.setText(str7);
                if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
                    if (msg.getReason() == null) {
                        // 如果没写理由
                        holder.reason.setText(str3);
                    }
                } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //入群申请
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str4 + msg.getGroupName());
                    }
                } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str8 + msg.getGroupName());
                    }
                }

                // 设置点击事件
                holder.agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 同意别人发的好友请求
                        addFriends(holder.llState, holder.tvStatus, msg);
                    }
                });
                holder.refuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 拒绝别人发的好友请求
                        refuseInvitation(holder.llState, holder.tvStatus, msg);
                    }
                });
            } else if (msg.getStatus() == InviteMesageStatus.AGREED) {
                holder.tvStatus.setText(str5);
//                holder.status.setBackgroundDrawable(null);
//                holder.status.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
                holder.tvStatus.setText(str6);
//                holder.status.setBackgroundDrawable(null);
//                holder.status.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION_ACCEPTED) {
                String str = msg.getGroupInviter() + str9 + msg.getGroupName();
                holder.reason.setText(str);
//                holder.status.setBackgroundDrawable(null);
                holder.tvStatus.setText("");
//                holder.status.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION_DECLINED) {
                String str = msg.getGroupInviter() + str10 + msg.getGroupName();
                holder.reason.setText(str);
//                holder.status.setBackgroundDrawable(null);
//                holder.status.setEnabled(false);
                holder.tvStatus.setText("");
            }
            if (ChatUserDao.findUserById(msg.getFrom()) != null) {
                EaseUser easeUser = ChatUserDao.findUserById(msg.getFrom());
                holder.name.setText(easeUser.getNick());
                ImageLoader.getInstance().displayImage(easeUser.getAvatar(), holder.avator, optionsImag);
            } else {
                // 设置用户头像
                getUser(msg.getFrom(), holder.avator, holder.name);
            }

        }

        return convertView;
    }

    /**
     * 同意好友请求或者群申请
     *
     * @param buttonAgree
     * @param buttonRefuse
     */
    private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_agree_with);
        final String str2 = context.getResources().getString(R.string.Has_agreed_to);
        final String str3 = context.getResources().getString(R.string.Agree_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {
                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//同意好友请求
                        EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //同意加群申请
                        EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
                    }
                    msg.setStatus(InviteMesageStatus.AGREED);
                    // 更新db
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonAgree.setText(str2);
                            buttonAgree.setBackgroundDrawable(null);
                            buttonAgree.setEnabled(false);

                            buttonRefuse.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

    /**
     * 拒绝好友请求或者群申请
     *
     * @param
     * @param buttonRefuse
     */
    private void refuseInvitation(final RelativeLayout rlS, final TextView buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(R.string.Has_refused_to);
        final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的拒绝方法
                try {
                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//拒绝好友请求
                        EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //同意加群申请
                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
                    }
                    msg.setStatus(InviteMesageStatus.REFUSED);
                    // 更新db
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
//                            buttonRefuse.setBackgroundDrawable(null);
//                            buttonRefuse.setEnabled(false);
                              rlS.setVisibility(View.GONE);
//                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

    public void update(Context context) {
        this.context=context;
        messgeDao=new InviteMessgeDao(context);
        super.notifyDataSetChanged();

    }

    private static class ViewHolder {
        CircleImageView avator;
        TextView name;
        TextView reason;
        Button agree;
        Button refuse;
        TextView tvStatus;
        LinearLayout groupContainer;
        TextView groupname;
        TextView time;
        RelativeLayout llState;
    }

    private void addFriends(final RelativeLayout rlS, final TextView buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_agree_with);
        final String str2 = context.getResources().getString(R.string.Has_agreed_to);
        final String str3 = context.getResources().getString(R.string.Agree_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        RequestManager.getMessManager().aggreeFriend(msg.getFrom(), new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                pd.dismiss();
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                msg.setStatus(InviteMesageStatus.AGREED);
                // 更新db
                ContentValues values = new ContentValues();
                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                messgeDao.updateMessage(msg.getId(), values);
                Intent intent=new Intent(context, PersonalAct.class);
                Bundle bundle=new Bundle();
                bundle.putString("userId",msg.getFrom());
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pd.dismiss();
                        buttonRefuse.setText(str2);
                        rlS.setVisibility(View.GONE);
//                        buttonAgree.setBackgroundDrawable(null);
//                        buttonAgree.setEnabled(false);

//                        buttonRefuse.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }


    private void getUser(final String personId, final CircleImageView cvHead, final TextView tvNick) {
        RequestManager.getMessManager().findUserByHId(personId, new ResultCallback<ResultBean<UserInfo>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<UserInfo> response) {
                UserInfo userInfo = response.getData();
                if (userInfo != null) {
                    ChatUserBean chatUserBean = new ChatUserBean();
                    chatUserBean.setUserId(personId);
                    chatUserBean.setNick(userInfo.getPetName());
                    chatUserBean.setIcon(Uitls.imageFullUrl(userInfo.getIcon()));
                    ChatUserDao.saveUser(chatUserBean);
                    ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(userInfo.getIcon()), cvHead, optionsImag);
                    tvNick.setText(userInfo.getPetName());
                }
            }
        });
    }
}
