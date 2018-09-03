package com.example.fangxi1998.update;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

import ser.Myservice;
import util.DataUtil;
import util.VibratorUtil;

public class Mainactivity extends AppCompatActivity {


    public static final String ACTION = "com.example.Activity";
    MyReceiver receiver = new MyReceiver();
    IntentFilter filter = new IntentFilter(ACTION);
    Myservice carService=new Myservice();
    //视频
    private SurfaceView surface;
    private SurfaceHolder holder;
    public static String url;
    private boolean thread;
    private Handler handler;
    private Canvas canvas;
    URL videoUrl;
    HttpURLConnection conn;
    Bitmap bmp;
    private SpeechSynthesizer mTts ;
    private ImageButton btn_up;
    private TextView home_dingwei;
    private TextView home_tx_wendu;
    private TextView home_tx_shidu;
    private ImageButton btn_left;
    private ImageButton btn_xunhang;
    private ImageButton btn_right;
    private ImageButton btn_down;
    private ImageButton btn_startxj;
    private ImageButton btn_stopxj;
    private ImageView home_hongwai;
    private ImageView home_huoyan;
    private String dingwei;
    private Button up;
    private Button down;
    private ImageView rq;
    private ImageView sd;
    MySynthesizerListener mTtsListener;
    private ReentrantLock mReentrantLock = new ReentrantLock();
    static  int count=0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maina);

        findView();
        surface.setKeepScreenOn(true);  //视频
        holder = surface.getHolder();
        setListener();
        Intent in = new Intent(Mainactivity.this,Myservice.class);
        startService(in);
        this.registerReceiver(receiver, filter); //开启广播
        CarSendThread carSendThread = new CarSendThread();
        carSendThread.start();
        initSpeech();
        speekText();


    }

    private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility. createUtility( this, SpeechConstant. APPID + "=5b0424f1" );
    }




    private void speekText() {
        //1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
        mTts = SpeechSynthesizer.createSynthesizer( this, null);
        //2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录 13.2
        mTts.setParameter(SpeechConstant. VOICE_NAME, "vixy" ); // 设置发音人
        mTts.setParameter(SpeechConstant. SPEED, "50" );// 设置语速
        mTts.setParameter(SpeechConstant. VOLUME, "80" );// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant. ENGINE_TYPE, SpeechConstant. TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在 “./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
        // mTts.setParameter(SpeechConstant. TTS_AUDIO_PATH, "./sdcard/iflytek.pcm" );
        //3.开始合成


    }

    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            //  showTip(" 开始播放 ");
        }

        @Override
        public void onSpeakPaused() {
            //   showTip(" 暂停播放 ");
        }

        @Override
        public void onSpeakResumed() {
            // showTip(" 继续播放 ");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos ,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //showTip("播放完成 ");
            } else if (error != null ) {
                showTip(error.getPlainDescription( true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {

        }
    }


    private void showTip (String data) {
        Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
    }

    private void setListener() {



        btn_up.setOnTouchListener(onTouchListener);
        btn_down.setOnTouchListener(onTouchListener);
        btn_left.setOnTouchListener(onTouchListener);
        btn_right.setOnTouchListener(onTouchListener);
        btn_xunhang.setOnClickListener(onClickListener);
        btn_stopxj.setOnTouchListener(onTouchListener);
        btn_startxj.setOnTouchListener(onTouchListener);
        up.setOnClickListener(ButtonOnClickListener);
        down.setOnClickListener(ButtonOnClickListener);



    }


    //启动视频绘制
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        thread=true;
        new gotoLoginAct().start();
        super.onResume();
    }



    public class MyReceiver extends BroadcastReceiver {
        // 接收SocketService发送过来的广播，更新主界面UI
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataReceiver = intent.getStringExtra("str");
            if ("a".equals(dataReceiver)) {
                dingwei="a";
                home_dingwei.setText("客厅");
            }else if ("b".equals(dataReceiver)) {
                dingwei="b";
                home_dingwei.setText("客厅");
            }else if ("c".equals(dataReceiver)) {
                dingwei="c";
                home_dingwei.setText("厨房");
            }else if ("d".equals(dataReceiver)) {
                dingwei="d";
                home_dingwei.setText("厨房");
            }else if ("e".equals(dataReceiver)) {
                dingwei="e";
                home_dingwei.setText("卧室");
            }else if ("f".equals(dataReceiver)) {
                dingwei="f";
                home_dingwei.setText("卧室");
            }else if ("h".equals(dataReceiver)) {
                home_huoyan.setBackgroundResource(R.drawable.huoyan_no);
            }else if ("i".equals(dataReceiver)) {
                home_huoyan.setBackgroundResource(R.drawable.huoyan_yes);
                //mTts.startSpeaking("家中有火焰，请及时处理~",mTtsListener);
                VibratorUtil.Vibrate(Mainactivity.this, 500);
                if ("a".equals(dingwei)) {
                    //Toast.makeText(Mainactivity.this, "40", Toast.LENGTH_SHORT).show();

                    mTts.startSpeaking("客厅有火焰，请及时处理~",mTtsListener);
                }else if ("b".equals(dingwei)) {
                   // Toast.makeText(Mainactivity.this, "188", Toast.LENGTH_SHORT).show();
                    mTts.startSpeaking("客厅有火焰，请及时处理~",mTtsListener);
                }else if ("c".equals(dingwei)) {
                   // Toast.makeText(Mainactivity.this, "126", Toast.LENGTH_SHORT).show();
                    mTts.startSpeaking("厨房有火焰，请及时处理~",mTtsListener);
                }else if ("d".equals(dingwei)) {
                   // Toast.makeText(Mainactivity.this, "129", Toast.LENGTH_SHORT).show();
                    mTts.startSpeaking("厨房有火焰，请及时处理~",mTtsListener);
                }else if ("e".equals(dingwei)) {
                    //Toast.makeText(Mainactivity.this, "143", Toast.LENGTH_SHORT).show();
                    mTts.startSpeaking("卧室有火焰，请及时处理~",mTtsListener);
                }else if ("f".equals(dingwei)) {


                    //Toast.makeText(Mainactivity.this, "220", Toast.LENGTH_SHORT).show();
                   mTts.startSpeaking("卧室有火焰，请及时处理~",mTtsListener);
                }
            }else if ("j".equals(dataReceiver)) {
                home_hongwai.setBackgroundResource(R.drawable.one_hongwai_yes);
            }else if ("k".equals(dataReceiver)) {
                home_hongwai.setBackgroundResource(R.drawable.one_hongwai_no);
            }else if (dataReceiver.contains("湿,")) {
                String onedata[] = dataReceiver.split(",");
                home_tx_shidu.setText(onedata[1]+"%");
                home_tx_wendu.setText(onedata[3]+"℃");
            }
            else if ("l".equals(dataReceiver)) {
                count++;
                rq.setBackgroundResource(R.drawable.ryhq);
                VibratorUtil.Vibrate(Mainactivity.this, 500);
                if(count==1) {
                    mTts.startSpeaking("家中燃气浓度过高,有泄露风险，请及时处理~", mTtsListener);

                }
                if(count==60){

                    count=0;
                }


            }else if ("m".equals(dataReceiver)) {
                rq.setBackgroundResource(R.drawable.byhq);
                count=0;

            }else if ("n".equals(dataReceiver)) {
                sd.setBackgroundResource(R.drawable.ryd);
                VibratorUtil.Vibrate(Mainactivity.this, 500);
                mTts.startSpeaking("家中有流水,请及时处理~",mTtsListener);

            }else if ("o".equals(dataReceiver)) {
                sd.setBackgroundResource(R.drawable.byd);

            }


        }
    }
//视频绘制实现
    private void draw() {
        // TODO Auto-generated method stub


        try {
            InputStream inputstream = null;
            //创建一个URL对象

            url = "http://192.168.1.150:8080/?action=snapshot";
            videoUrl=new URL(url);
            //利用HttpURLConnection对象从网络中获取网页数据
            conn = (HttpURLConnection)videoUrl.openConnection();
            //设置输入流
            conn.setDoInput(true);
            //连接
            conn.connect();
            //得到网络返回的输入流
            inputstream = conn.getInputStream();
            //创建出一个bitmap
            bmp = BitmapFactory.decodeStream(inputstream);
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            RectF rectf = new RectF(0, 0, 1100, 750);
            canvas.drawBitmap(bmp, null, rectf, null);
            holder.unlockCanvasAndPost(canvas);
            //关闭HttpURLConnection连接
            conn.disconnect();
        } catch (Exception ex) {
        } finally {
        }
    }
//声明控件
    private void findView() {
        surface = (SurfaceView)findViewById(R.id.surface);
        home_dingwei = (TextView)findViewById(R.id.home_dingwei);
        home_tx_wendu = (TextView)findViewById(R.id.home_tx_wendu);
        home_tx_shidu = (TextView)findViewById(R.id.home_tx_shidu);
        btn_up = (ImageButton)findViewById(R.id.btn_up);
        btn_left = (ImageButton)findViewById(R.id.btn_left);
        btn_xunhang = (ImageButton)findViewById(R.id.btn_xunhang);
        btn_right = (ImageButton)findViewById(R.id.btn_right);
        btn_down = (ImageButton)findViewById(R.id.btn_down);
        btn_startxj = (ImageButton)findViewById(R.id.xj1);
        btn_stopxj=(ImageButton)findViewById(R.id.xj2);
        home_hongwai = (ImageView)findViewById(R.id.home_hongwai);
        home_huoyan = (ImageView)findViewById(R.id.home_huoyan);
        up=findViewById(R.id.button1);
        down = findViewById(R.id.button2);
        sd=findViewById(R.id.yd);
        rq=findViewById(R.id.rq);


    }


//副线程发送数据
    class CarSendThread extends Thread{
        @SuppressLint("HandlerLeak")
        public void run(){

            Looper.prepare();
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String s = (String) msg.obj;

                   carService.sendDate(s);
                }
            };
            Looper.loop();



        }




    }
    //副线程启动视频绘制
    class gotoLoginAct extends Thread{
        @Override
        public void run() {
            super.run();
            while(thread){
                draw();
            }
        }
    }
//onclicklistener的监听
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void       onClick(View view) {
            if (DataUtil.xunhang==true) {
                btn_xunhang.setBackgroundResource(R.drawable.button);

                DataUtil.go=false;
                DataUtil.xunhang=false;

                Message msg = handler.obtainMessage();
                msg.obj="xt";
                handler.sendMessage(msg);
            }else {
                btn_xunhang.setBackgroundResource(R.drawable.buttonyes);

                DataUtil.go=true;
                DataUtil.xunhang=true;

                Message msg = handler.obtainMessage();
                msg.obj="xj";
                handler.sendMessage(msg);
            }


        }
    };
    View.OnClickListener ButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.button1:
                    Message msg = handler.obtainMessage();
                    msg.obj="cu";
                    handler.sendMessage(msg);
                        break;
                case R.id.button2:
                    Message msg1 = handler.obtainMessage();
                    msg1.obj="cd";
                    handler.sendMessage(msg1);
                        break;



            }

        }
    };

//ontouchlistener 的监听
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {


            switch (view.getId()){
                case R.id.btn_down:
                    if (DataUtil.go==false) {

                        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                            btn_down.setBackgroundResource(R.drawable.down);
                            Message msg = handler.obtainMessage();
                         msg.obj="cs";
                         handler.sendMessage(msg);



                        }
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                            btn_down.setBackgroundResource(R.drawable.downyes);

                            Message msg = handler.obtainMessage();
                           msg.obj="cb";
                           handler.sendMessage(msg);
                        }

                    }
                    break;
                case R.id.btn_up:
                    if (DataUtil.go==false) {

                        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                            btn_up.setBackgroundResource(R.drawable.up);

                            Message msg = handler.obtainMessage();
                       msg.obj="cs";
                       handler.sendMessage(msg);
                        }
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                            btn_up.setBackgroundResource(R.drawable.upyes);

                            Message msg = handler.obtainMessage();
                            msg.obj="ca";
                            handler.sendMessage(msg);
                        }

                    }
                    break;
                case R.id.btn_left:
                    if (DataUtil.go==false) {

                        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                            btn_left.setBackgroundResource(R.drawable.left);

                            Message msg = handler.obtainMessage();
                            msg.obj="cs";
                            handler.sendMessage(msg);

                        }
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                            btn_left.setBackgroundResource(R.drawable.leftyes);

                            Message msg = handler.obtainMessage();
                            msg.obj="cl";
                            handler.sendMessage(msg);
                        }

                    }
                    break;
                case R.id.btn_right:
                    if (DataUtil.go==false) {

                        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                            btn_right.setBackgroundResource(R.drawable.right);

                            Message msg = handler.obtainMessage();
                            msg.obj="cs";
                            handler.sendMessage(msg);
                        }
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                            btn_right.setBackgroundResource(R.drawable.rightyes);

                            Message msg = handler.obtainMessage();
                            msg.obj="cr";
                            handler.sendMessage(msg);
                        }

                    }
                    break;
                case R.id.xj1:
                    if(DataUtil.go==false){
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                            //Toast.makeText(Mainactivity.this,"xs",Toast.LENGTH_SHORT).show();

                            Message msg = handler.obtainMessage();
                            msg.obj="xs";
                            handler.sendMessage(msg);
                        }





                    }
                    break;
                case R.id.xj2:
                    if(DataUtil.go==false){
                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                           // Toast.makeText(Mainactivity.this,"xt",Toast.LENGTH_SHORT).show();

                            Message msg = handler.obtainMessage();
                            msg.obj="xt";
                            handler.sendMessage(msg);
                        }





                    }
                    break;








            }
            return false;
        }
    };


}
