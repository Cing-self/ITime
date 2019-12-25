package com.example.caijinhong.itime;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.caijinhong.itime.data.TimeData;
import com.leon.lib.settingview.LSettingItem;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.lankton.flowlayout.FlowLayout;

public class NewBuildActivity extends AppCompatActivity {
    private static final String TAG = "NewBuildActivity";

    private TimeData record1,record2;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    /*@按钮  时间,重复设置,图片添加标签*/
    private LSettingItem lSettingItemTime, lSettingItemSetting, lSettingItemImage, lSettingItemTag;
    /* 按钮文本*/
    private TextView textViewTime, textViewSetting,textViewTag;

    private AlertDialog alertDialog; //单选框
    MenuItem sure;//确定键
    private List<String> list=new ArrayList<String>();//标签列表
    private FlowLayout flowlayout;
    private String[] items={"标签131231","标23签2","标123123签3","标签4","标签5"};

    private ImageButton buttoncheck;
    @ViewInject(R.id.L_new_bulld)
    private LinearLayout photo;
    @ViewInject(R.id.image_gallery)
    private ImageButton buttonGallery;
    @ViewInject(R.id.image_takephoto)
    private ImageButton buttonTake;

    private EditText editTextTitle,editTextRemark;
    private int editPosition;
    private ImageView imageView;
    private static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);

        ViewUtils.inject(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("新建日历");
        }

        //日历控件初始化
        lSettingItemTime = (LSettingItem) findViewById(R.id.button_time);
        textViewTime = (TextView) findViewById(R.id.text_view_time);

        //重复设定控件初始化
        lSettingItemSetting = (LSettingItem) findViewById(R.id.button_setting);
        textViewSetting = (TextView) findViewById(R.id.text_view_setting);
        //图片控件初始化
        lSettingItemImage = (LSettingItem) findViewById(R.id.button_image);

        //确定控件初始化
        buttoncheck= (ImageButton) findViewById(R.id.button_check);
        buttoncheck.bringToFront();

        //添加标签控件初始化
        lSettingItemTag = (LSettingItem) findViewById(R.id.button_tag);
        textViewTag= (TextView) findViewById(R.id.text_view_tag);
        for(int i=0;i<5;i++){
            list.add(items[i]);
        }
        //标签点击事件
        lSettingItemTag.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showTagDialog();
            }
        });
        //日历点击事件
        lSettingItemTime.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showDatePickDlg();
            }
        });
        //重复设定点击事件
        lSettingItemSetting.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showListDialog();
            }
        });

        //照片点击
        lSettingItemImage.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showImageButton();
            }
        });

        editPosition=getIntent().getIntExtra("edit_position",0);

        editTextTitle=(EditText)findViewById(R.id.edit_title);
        editTextRemark=(EditText)findViewById(R.id.edit_remarks);
/*
        String goodTitle=getIntent().getStringExtra("Itime_title");
        String goodRemark= getIntent().getStringExtra("Itime_remark");
        String goodTime=getIntent().getStringExtra("Itime_time");
        String goodSetting=getIntent().getStringExtra("Itime_setting");
        String goodTag=getIntent().getStringExtra("Itime_tag");
        if(goodTitle!=null) {
            editTextTitle.setText(goodTitle);
            editTextRemark.setText(goodRemark);
            textViewTime.setText(goodTime);
            textViewSetting.setText(goodSetting);
            textViewTag.setText(goodTag);
        }*/
        record1= (TimeData) getIntent().getSerializableExtra("record");
        record2= (TimeData) getIntent().getSerializableExtra("recordDToE");
        if(record2!=null){
            editTextTitle.setText(record2.getTitle());
            editTextRemark.setText(record2.getMotto());
            String text=record2.getYear() + "年" + record2.getMonth() + "月" + record2.getDay()+"日  "+record2.getHour()+"："+record2.getMinute();
            textViewTime.setText(text);
        }
        //确定点击
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                Bundle bundle=new Bundle();

                if(record2!=null){
                    record2.setTitle(editTextTitle.getText().toString());
                    record2.setMotto(editTextRemark.getText().toString());
                    record2.setPhotoId(R.drawable.adver1);
                    bundle.putSerializable("record",record2);

                }
                if(record1!=null) {
                    record1.setTitle(editTextTitle.getText().toString());
                    record1.setMotto(editTextRemark.getText().toString());
                    record1.setPhotoId(R.drawable.adver1);
                    bundle.putSerializable("record", record1);
                }
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                NewBuildActivity.this.finish();
                /*
                Intent intent = new Intent();
                intent.putExtra("edit_position", editPosition);
                intent.putExtra("Itime_title", editTextTitle.getText().toString().trim());
                intent.putExtra("Itime_remark",editTextRemark.getText().toString().trim());
                intent.putExtra("Itime_time","111");
                intent.putExtra("Itime_setting",textViewSetting.getText().toString().trim());
                intent.putExtra("Itime_tag",textViewTag.getText().toString().trim());

                Bundle b = new Bundle();
                b.putParcelable("bitmap", bitmap);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                NewBuildActivity.this.finish();
                */
            }
        });
    }

    //新建页面---返回按钮
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //日历表
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewBuildActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String time=String.valueOf(year)+"年"+String.valueOf(monthOfYear+1)+"月"+Integer.toString(dayOfMonth)+"日";
                showTime(year,monthOfYear,dayOfMonth);
                textViewTime.setText(time);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //时钟
    private void showTime(final int year, final int monthOfYear, final int dayOfMonth){
        Calendar calendar=Calendar.getInstance();
        String time1;
        TimePickerDialog timePickerDialog=new TimePickerDialog(NewBuildActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time=String.valueOf(hourOfDay)+"点"+String.valueOf(minute)+"分";
                if(record1!=null) {
                    record1.setYear(year);
                    record1.setMonth(monthOfYear);
                    record1.setDay(dayOfMonth);
                    record1.setHour(hourOfDay);
                    record1.setMinute(minute);
                }
                if(record2!=null) {
                    record2.setYear(year);
                    record2.setMonth(monthOfYear);
                    record2.setDay(dayOfMonth);
                    record2.setHour(hourOfDay);
                    record2.setMinute(minute);
                }
                textViewTime.setText(textViewTime.getText().toString()+time);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
    //标签-流式布局
    private void showTagDialog(){
        final String[] tag = {new String()};
        final View view=View.inflate(NewBuildActivity.this,R.layout.tag_flow,null);
        flowlayout= (FlowLayout) view.findViewById(R.id.flowlayout);
        for(int i=0;i<list.size();i++){
            View view1=View.inflate(NewBuildActivity.this,R.layout.item_tag,null);
            TextView tv= (TextView) view1.findViewById(R.id.textView1);
            tv.setText(list.get(i));
            tv.setTag(i);
            view1.setTag(false);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv= (TextView) v.findViewById(R.id.textView1);
                    Log.i("dx1","TextView click");
                    if((Boolean)v.getTag()){
                        v.setTag(false);
                        tv.setEnabled(false);
                        tag[0] = tag[0] +tv.getText().toString();
                    }else {
                        v.setTag(true);
                        tv.setEnabled(true);
                        tag[0] =tv.getText().toString();
                    }
                }
            });
            flowlayout.addView(view1);
        }
        final AlertDialog.Builder listDialog=new AlertDialog.Builder(NewBuildActivity.this);
        final AlertDialog.Builder editDialog=new AlertDialog.Builder(NewBuildActivity.this);
        final EditText edit = new EditText(this);
        final AlertDialog.Builder editTag = new AlertDialog.Builder(NewBuildActivity.this);
        listDialog.setTitle("标签").setIcon(R.drawable.ic_launcher);
        listDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textViewTag.setText(tag[0]);
            }
        }).setNegativeButton("取消",null).setNeutralButton("自定义", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTag.setTitle("输入").setView(edit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text=edit.getText().toString();
                        list.add(text);
                    }
                }) .setNegativeButton("取消",null).create().show();
            }
        }).setView(view).create().show();
    }

    private void initTagView() {

    }

    //确定按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_new_build, menu);
        return true;
    }

    //重复设定
    /**
     * 自定义列表Dialog
     */
    private void showListDialog() {
        final String[] items = {"每年", "每月", "每周", "每天", "自定义", "无"};
        final EditText editSelf = new EditText(this);
        AlertDialog.Builder listDialog = new AlertDialog.Builder(NewBuildActivity.this);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(NewBuildActivity.this);
        listDialog.setIcon(R.drawable.person_notevaluate);//图标
        listDialog.setTitle("选择").setNegativeButton("取消", null);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 4) {
                    textViewSetting.setText(items[which]);
                } else {
                    editDialog.setTitle("输入").setView(editSelf).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textViewSetting.setText(editSelf.getText().toString());
                        }
                    }).setNegativeButton("取消", null).show();
                }
            }
        });
        listDialog.show();
    }

    private void showImageButton(){
        final View view=View.inflate(NewBuildActivity.this,R.layout.image_button_dialog,null);
        final AlertDialog.Builder listDialog=new AlertDialog.Builder(NewBuildActivity.this);
        listDialog.setTitle("选择").setView(view).create();
        buttonTake= (ImageButton) view.findViewById(R.id.image_takephoto);
        buttonGallery= (ImageButton) view.findViewById(R.id.image_gallery);
        buttonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainCameraPermission();
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainStoragePermission();
            }
        });
        listDialog.show();
    }
    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(NewBuildActivity.this, "com.zz.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(NewBuildActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
                default:
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        photo.setBackground(new BitmapDrawable(bitmap));
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}

