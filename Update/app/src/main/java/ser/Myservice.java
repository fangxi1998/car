package ser;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class Myservice extends Service {


    String IP="192.168.137.105";
    int Port=1200;
    private Socket socket;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    static byte RxBuf[] = new byte[256];
    int len;
    static PrintWriter printWriter = null;
    Context context;


    @Override
    public IBinder onBind(Intent intent) {
        Log.v("Myserivce","on bind");
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       socketconnect(this);
        return super.onStartCommand(intent, flags, startId);
    }
    public void socketconnect(Context context){
        this.context = context;
        showToast("正在连接网关");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket(IP,Port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    inputStream = socket.getInputStream();
                    outputStream= socket.getOutputStream();
                    printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("gb2312"))));

                    showToast("网关连接成功");
                    getData();



                } catch (Exception e) {
                 showToast("网管连接失败");
                    e.printStackTrace();
                }
            }
        }).start();







    }
    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setTimerTask();
                while (socket.isConnected()) {
                    try {
                        len = inputStream.read(RxBuf);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (len > 0) {
                        showToast("V,"+RxBuf[21]+",V1,"+RxBuf[22]);

                       // System.out.println(RxBuf[11]);
                        if (RxBuf[11] == 1) {//
                            showToast("a");
                        }else if (RxBuf[11] == 2) {


                            showToast("b");
                        }else if (RxBuf[11] == 3) {
                            showToast("c");
                        }else if (RxBuf[11] == 4) {
                            showToast("d");
                        }else if (RxBuf[11] == 5) {
                            showToast("e");
                        }else if (RxBuf[11] == 6) {
                            showToast("f");
                        }else if (RxBuf[11] == 54) {
                            showToast("g");
                        }
                        if (RxBuf[15] == 1) {
                            showToast("h");
                        }else if (RxBuf[15] == 0) {
                            showToast("i");
                        }
                        if (RxBuf[16] == 1) {
                            showToast("j");
                        }else if (RxBuf[16] == 0) {
                            showToast("k");
                        }
                        if (RxBuf[17] != 0) {
                            showToast("湿,"+RxBuf[17]+","+"温,"+RxBuf[18]);
                        }if (RxBuf[19]==1){
                            showToast("l");


                        }else if (RxBuf[19]==0){

                            showToast("m");

                        }if (RxBuf[20]==1){
                            showToast("n");


                        }else if (RxBuf[20]==0){

                            showToast("o");

                        }
                    }

                }
            }
        }).start();
    }
    public  void setTimerTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sendDate("s");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void showToast(String str){

        Intent intent = new Intent();
        intent.putExtra("str",str);
        intent.setAction("com.example.Activity");
        sendBroadcast(intent);


    }
    public void sendDate(String str){
        printWriter.print(str);
        printWriter.flush();



    }
}
