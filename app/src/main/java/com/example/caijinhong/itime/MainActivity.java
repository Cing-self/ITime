package com.example.caijinhong.itime;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActionBarContainer;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caijinhong.itime.data.FileDataSource;
import com.example.caijinhong.itime.data.TimeData;
import com.example.caijinhong.itime.data.TimeItem;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //主页轮播图
  //  public static final int REQUEST_CODE_NEW_TIME = 901;
 //   public static final int REQUEST_CODE_UPDATE_GOOD = 902;
 //   private ArrayList<TimeData> theTime;
    private ListView listViewSuper;
    //private TimeArrayAdapter theAdaper;

    private Handler mHandler;
    private TextView textViewBottom,textViewMiddle,textViewTop;
    private FileDataSource fileDataSource;
    private View view;
    private  ItimeRecordArrayAdapter itimeAdaper;
    private ArrayList<TimeData> itimeRecords=new ArrayList<TimeData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT>=21){

            getSupportActionBar().setElevation(0);

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this,NewBuildActivity.class);
                TimeData itimeRecord=new TimeData();
                Bundle bundle=new Bundle();
                bundle.putSerializable("record",itimeRecord);
                intent.putExtras(bundle);

                startActivityForResult(intent, 1);
            }
        });

        InitData();
//列表
        listViewSuper= (ListView) findViewById(R.id.list_view_time);
        textViewBottom= (TextView) this.findViewById(R.id.text_view_time_bottom);
        textViewMiddle= (TextView) this.findViewById(R.id.text_view_time_middle);
        textViewTop= (TextView) this.findViewById(R.id.text_view_time_top);
        view=this.findViewById(R.id.linear_layout_time);


        itimeAdaper=new ItimeRecordArrayAdapter(this,R.layout.list_view_item_time,itimeRecords);
        listViewSuper.setAdapter(itimeAdaper);


        new TimeThread().start();
        mHandler = new Handler(){  //实现主页面倒计时
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1: {
                        if (itimeRecords != null) {
                            SimpleDateFormat dateFormatterChina = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            // TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");//获取时区 这句加上，很关键。
                            //dateFormatterChina.setTimeZone(timeZoneChina);//设置系统时区
                            long sysTime = System.currentTimeMillis();//获取系统时间

                            // CharSequence sysTimeStr = dateFormatterChina.format(sysTime);//时间显示格式
                            Drawable drawable=getResources().getDrawable(R.drawable.adver1);

                            view.setBackground(drawable);

                            TimeData recordFirst = itimeRecords.get(0);
                            textViewTop.setText(recordFirst.getTitle());
                            textViewMiddle.setText(recordFirst.getYear() + "年" + recordFirst.getMonth() + "月" + recordFirst.getDay() + "日");


                            Calendar cal = Calendar.getInstance();
                            cal.set(recordFirst.getYear(), recordFirst.getMonth(), recordFirst.getDay(), recordFirst.getHour(), recordFirst.getMinute(), 0);//第二个参数月份是实际值减1
                            Date date = cal.getTime();
                            long timeStamp = date.getTime() - 8 * 60 * 60 * 1000;//获取时间戳
                            // CharSequence timeStampStr = dateFormatterChina.format(timeStamp);


                            if (sysTime < timeStamp) {
                                long dateMinus = timeStamp - sysTime;
                                long totalSeconds = dateMinus / 1000;

                                //求出现在的秒
                                long currentSecond = totalSeconds % 60;

                                //求出现在的分
                                long totalMinutes = totalSeconds / 60;
                                long currentMinute = totalMinutes % 60;

                                //求出现在的小时
                                long totalHour = totalMinutes / 60;
                                long currentHour = totalHour % 24;

                                //求出现在的天数
                                long totalDay = totalHour / 24;


                                textViewBottom.setText(totalDay + "天" + currentMinute + "分" + currentSecond + "秒");
                            } else {
                                long dateMinus = sysTime - timeStamp;
                                long totalSeconds = dateMinus / 1000;

                                //求出现在的秒
                                long currentSecond = totalSeconds % 60;

                                //求出现在的分
                                long totalMinutes = totalSeconds / 60;
                                long currentMinute = totalMinutes % 60;

                                //求出现在的小时
                                long totalHour = totalMinutes / 60;
                                long currentHour = totalHour % 24;

                                //求出现在的天数
                                long totalDay = totalHour / 24;
                                textViewBottom.setText(totalDay + "天" + currentMinute + "分" + currentSecond + "秒");
                            }

                        }
                    }
                    // CharSequence sysTimeStr = dateFormatterChina.format( sysTime);//时间显示格式
                    //textView.setText(sysTimeStr); //更新时间
                    break;
                }
            }
        };

        listViewSuper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                TimeData itimeRecord=itimeRecords.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("record",itimeRecord);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                startActivityForResult(intent, 2);
               /* Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                TimeItem timeItem = theTime.get(position);
                intent.putExtra("edit_position",position);
                intent.putExtra("Itime_title",timeItem.getTitle());
                intent.putExtra("Itime_remark",timeItem.getRemark());
                intent.putExtra("Itime_time",timeItem.getTime());
                startActivity(intent);*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle =  data.getExtras();
                    TimeData record= (TimeData) bundle.getSerializable("record");
                    itimeRecords.add(record);
                    itimeAdaper.notifyDataSetChanged();

                    // ItimeRecord recordFirst=itimeRecords.get(0);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    TimeData record= (TimeData) data.getExtras().getSerializable("record");
                    int position=data.getIntExtra("position",-1);
                    if(position!=-1){

                        itimeRecords.remove(position);
                        itimeRecords.add(position,record);
                        itimeAdaper.notifyDataSetChanged();

                    }
                }else{
                    int position=data.getIntExtra("position",-1);
                    if(position!=-1) {

                        itimeRecords.remove(position);
                        itimeAdaper.notifyDataSetChanged();
                    }
                }
            /*
            case REQUEST_CODE_NEW_TIME:
                if(resultCode==RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String title = data.getStringExtra("Itime_title");
                    String remark = data.getStringExtra("Itime_remark");
                    String time = data.getStringExtra("Itime_time");

                    Bundle b=data.getExtras();
                    Bitmap bmp=(Bitmap) b.getParcelable("bitmap");

                    theTime.add(position, new TimeItem(title,remark,bmp,time));
                    theAdaper.notifyDataSetChanged();

                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_GOOD:
                if(resultCode==RESULT_OK)
                {
                    int position = data.getIntExtra("edit_position", 0);
                    String title = data.getStringExtra("Itime_title");
                    String remark = data.getStringExtra("Itime_remark");
                    String time = data.getStringExtra("Itime_time");

                    Bundle b=data.getExtras();
                    Bitmap bmp=(Bitmap) b.getParcelable("bitmap");

                    TimeItem timeItem=theTime.get(position);
                    timeItem.setTitle(title);
                    timeItem.setRemark(remark);
                    timeItem.setTime(time);
                    timeItem.setImage(bmp);
                    theAdaper.notifyDataSetChanged();

                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;*/

        }
    }
    /*
    private void InitData() {
        theTime =new ArrayList<TimeItem>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.adver1);
        theTime.add(new TimeItem("fish","1",bitmap,"1"));
    }*/
    private void InitData() {
        fileDataSource=new FileDataSource(this);
        itimeRecords=fileDataSource.load();
        itimeRecords=new ArrayList<TimeData>();
        itimeRecords.add(new TimeData("111","111",2020,1,1,5,4,R.drawable.adver1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        fileDataSource.save();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_tag) {

        } else if (id == R.id.nav_color) {

        } else if (id == R.id.nav_primery) {

        }  else if (id == R.id.nav_lock) {

        }else if (id == R.id.nav_setting) {

        }else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_help) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ItimeRecordArrayAdapter extends ArrayAdapter<TimeData> {
        int resourceId;
        public ItimeRecordArrayAdapter(@NonNull Context context, int resource, @NonNull List<TimeData> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //super.getView(position, convertView, parent);
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(resourceId,null);

            TextView top = (TextView)item.findViewById(R.id.list_text_view_title);
            TextView middle= (TextView) item.findViewById(R.id.list_text_view_remark);
            TextView bottom = (TextView)item.findViewById(R.id.list_text_view_time);
            TextView left= (TextView) item.findViewById(R.id.text_view_record_left);

            TimeData itimeRecord_item= this.getItem(position);

            top.setText(itimeRecord_item.getTitle());
            middle.setText(itimeRecord_item.getYear()+"年"+(itimeRecord_item.getMonth()+1)+"月"+itimeRecord_item.getDay()+"日");
            bottom.setText(itimeRecord_item.getMotto());

            long sysTime = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.set(itimeRecord_item.getYear(), itimeRecord_item.getMonth(), itimeRecord_item.getDay(), itimeRecord_item.getHour(), itimeRecord_item.getMinute(), 0);//第二个参数月份是实际值减1
            Date date = cal.getTime();
            long timeStamp = date.getTime() - 8 * 60 * 60 * 1000;
            if (sysTime < timeStamp) {
                long dateMinus = timeStamp - sysTime;
                long totalSeconds = dateMinus / 1000;
                //求出总的分
                long totalMinutes = totalSeconds / 60;
                //求出总的小时
                long totalHour = totalMinutes / 60;
                //求出总的天数
                long totalDay = totalHour / 24;
                left.setText(totalDay + "天" );
            } else {
                long dateMinus =sysTime-timeStamp ;
                long totalSeconds = dateMinus / 1000;
                //求出总的分
                long totalMinutes = totalSeconds / 60;
                //求出总的小时
                long totalHour = totalMinutes / 60;
                //求出总的天数
                long totalDay = totalHour / 24;
                left.setText(totalDay + "天" );
            }
            return item;
        }
    }
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

}
