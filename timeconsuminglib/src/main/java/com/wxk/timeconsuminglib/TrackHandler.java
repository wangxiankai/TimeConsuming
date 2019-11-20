package com.wxk.timeconsuminglib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import com.wxk.timeconsuminglib.loadingdialog.view.LoadingDialog;

/**
 * Date: 2019/11/20
 * Author: wangxiankai
 * Description:
 */
public class TrackHandler {

    private static TrackHandler mTrackHandler = null;
    private Work mWorkHandler;
    private Context mContext;
    private LoadingDialog mLoadingDialog;
    private ThreadCallback mThreadCallback;

    public static TrackHandler getInstance(){
        if (mTrackHandler == null){
            synchronized (TrackHandler.class){
                if (mTrackHandler == null){
                    mTrackHandler = new TrackHandler();
                }
            }
        }
        return mTrackHandler;
    }

    private TrackHandler(){
        //实例化一个特殊的线程HandlerThread，必须给其指定一个名字
        HandlerThread thread = new HandlerThread("learning-test-thread");
        //开启这个线程
        thread.start();
        mWorkHandler = new Work(thread.getLooper());
    }

    /**
     * 初始化
     * @param context
     * @return
     */
    public TrackHandler init(Context context){
        mContext = context;
        mLoadingDialog = new LoadingDialog(mContext);
        return this;
    }

    /**
     * 加载load
     *
     * @return
     */
    public TrackHandler load(){
        return this;
    }

    /**
     * 线程处理
     * @param callback
     */
    public TrackHandler setThreadCallback(ThreadCallback callback){
        mThreadCallback = callback;
        return this;
    }

    /**
     * 开始执行
     */
    public void start(){
        mLoadingDialog.show();
        Message message = new Message();
        message.obj = mUiHandler;
        mWorkHandler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    private Handler mUiHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mThreadCallback != null){
                mThreadCallback.runOnUi(msg.obj);
            }
            mLoadingDialog.close();
        }
    };

    /**
     * 工作线程
     */
    class Work extends Handler {

        public Work(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Object object = null;
            if (mThreadCallback != null){
                object = mThreadCallback.runOnThread();
            }

            //处理回到主线程
            Handler handle = (Handler) msg.obj;
            Message message = new Message();
            message.obj = object;
            handle.sendMessage(message);
        }
    }
}
