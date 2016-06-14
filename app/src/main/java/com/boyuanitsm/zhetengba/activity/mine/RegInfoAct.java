package com.boyuanitsm.zhetengba.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.XqgvAdapter;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.UserInfo;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyBitmapUtils;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.boyuanitsm.zhetengba.view.MyGridView;
import com.boyuanitsm.zhetengba.view.MySelfSheetDialog;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册信息界面面
 * Created by bitch-1 on 2016/5/9.
 */
public class RegInfoAct extends BaseActivity {
    @ViewInject(R.id.et_pickname)
    private EditText et_pickname;
    @ViewInject(R.id.boy_rd)
    private RadioButton boy_rd;
    @ViewInject(R.id.gvxq)
    private MyGridView gvqy;
    @ViewInject(R.id.rg_sex)
    private RadioGroup rg_sex;
    @ViewInject(R.id.iv_rgificon)
    private CircleImageView iv_icon;


    private XqgvAdapter xqgvAdapter;//兴趣标签适配器
    private Map<Integer,String>datamap;//用来封装适配器里面选中和取消相中后的标签
    private String connects;//标签选择的内容
    private String sex="男";//性别选择默认为男
    private String pickname;//昵称

    private String username;
    private String pwd;
    private String yzm;
    private UserInfo userInfo;


    private String photoSavePath;
    private String photoSaveName;
    Uri imageUri = null;
    public static final int PHOTOZOOM = 0;
    public static final int PHOTOTAKE = 1;
    public static final int IMAGE_COMPLETE = 2; // 结果


    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.ellipse) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.ellipse) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.ellipse) // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
            .build(); // 创建配置过得DisplayImageOption对象

    @Override
    public void setLayout() {
        setContentView(R.layout.act_reginfo);

    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("注册信息");
        datamap=new HashMap<>();
        userInfo=new UserInfo();


        xqgvAdapter = new XqgvAdapter(getApplicationContext(),this);
        gvqy.setAdapter(xqgvAdapter);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.girl_rd:
                        sex = "女";
                        break;
                    case R.id.boy_rd:
                        sex = "男";
                        break;
                }

            }
        });

    }
    @OnClick({R.id.wancheng,R.id.iv_rgificon})
    public void OnClick(View view){

        connects="";//被点击的选项内容
        for(int key:datamap.keySet()){
            connects=connects.concat(datamap.get(key).toString()+",");
        }

        pickname=et_pickname.getText().toString().trim();//昵称
        userInfo.setPetName(pickname);
        userInfo.setSex(sex);
        switch (view.getId()){
            case R.id.iv_rgificon://点击图像
                headIconDialog();
                break;

            case R.id.wancheng://
//                toRegister(userInfo,yzm,connects);
                MyToastUtils.showShortToast(getApplicationContext(), connects + sex+pickname);
                break;



        }

    }
    /**
     * 获取到适配器中选中的数据是适配器中有调用这个方法
     * @param datamap
     */
    public void setData(Map<Integer,String> datamap){
        this.datamap=datamap;

    }


    private void headIconDialog() {
        MySelfSheetDialog dialog = new MySelfSheetDialog(RegInfoAct.this);
        dialog.builder().addSheetItem("拍照", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
//                        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, PHOTOTAKE);
                } else {
                    MyToastUtils.showShortToast(RegInfoAct.this, "存储卡不存在");
                }

            }
        }).addSheetItem("从相册选取", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, PHOTOZOOM);
            }
        }).show();
    }

    /**
     * 返回的Path
     */
    private String temppath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri = null;
        Intent intent = null;
        switch (requestCode) {
            case PHOTOZOOM:// 相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                uri = data.getData();
                Bitmap userbitmap = MyBitmapUtils.decodeUriAsBitmap(this, uri);
                File user_head = MyBitmapUtils.saveBitmap(MyBitmapUtils.zoomImg(userbitmap, 100, 100), "user_head.png");
                intent = new Intent(this, ClipActivity.class);
                intent.putExtra("path", Environment.getExternalStorageDirectory() + "/" + "user_head.png");
                startActivityForResult(intent, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE:// 拍照
                if (resultCode != RESULT_OK) {
                    return;
                }
                String path = photoSavePath + photoSaveName;
                Intent intent2 = new Intent(this, ClipActivity.class);
                intent2.putExtra("path", path);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;

            case IMAGE_COMPLETE:// 完成
                temppath = data.getStringExtra("path");
                iv_icon.setImageBitmap(MyBitmapUtils.LoadBigImg(temppath, 120, 120));

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 保存用户信息
     * @param
     * @param
     * @param
     */
     private void saveUser(final UserInfo userInfo){
         RequestManager.getUserManager().modifyUserInfo(userInfo, new ResultCallback<ResultBean<String>>() {
             @Override
             public void onError(int status, String errorMsg) {

             }

             @Override
             public void onResponse(ResultBean<String> response) {
                 MyToastUtils.showShortToast(getApplicationContext(),"完善成功");

             }
         });

     }




}
