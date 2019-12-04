package com.example.caijinhong.itime;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.leon.lib.settingview.LSettingItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.lankton.flowlayout.FlowLayout;

public class NewBuildActivity extends AppCompatActivity {
    private LSettingItem lSettingItemTime, lSettingItemSetting, lSettingItemImage, lSettingItemTag;
    private TextView textViewTime, textViewSetting,textView3, textViewTag;

    private AlertDialog alertDialog; //单选框
    MenuItem sure;//确定键
    private List<String> list=new ArrayList<String>();//标签列表
    private LinearLayout linearLayout;
    private FlowLayout flowlayout;
    private String[] items={"标签131231","标23签2","标123123签3","标签4","标签5"};

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
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
        linearLayout= (LinearLayout) findViewById(R.id.L_new_bulld);

        //添加标签控件初始化
        lSettingItemTag = (LSettingItem) findViewById(R.id.button_tag);
        textViewTag = (TextView) findViewById(R.id.text_view_tag);
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
       /* lSettingItemTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDatePickDlg();
            }
        });*/


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
                onClick();
            }
        });
    }

    public void onClick() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }
    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ((ImageView)findViewById(R.id.image)).setImageBitmap(bm);
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
                showTime();
                textViewTime.setText(time);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //时钟
    private void showTime(){
        Calendar calendar=Calendar.getInstance();
        String time1;
        TimePickerDialog timePickerDialog=new TimePickerDialog(NewBuildActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time=String.valueOf(hourOfDay)+"点"+String.valueOf(minute)+"分";
                textViewTime.setText(textViewTime.getText().toString()+time);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
    //标签-流式布局
    private void showTagDialog(){

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
                    }else {
                        v.setTag(true);
                        tv.setEnabled(true);
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
}
