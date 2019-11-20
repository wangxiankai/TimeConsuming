package com.a3xh1.time

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.wxk.timeconsuminglib.ThreadCallback
import com.wxk.timeconsuminglib.TrackHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            TrackHandler.getInstance()
                .init(this)
                .setThreadCallback(object : ThreadCallback<String>{
                    override fun runOnThread(): String {
                        try {
                            Thread.sleep(10000)
                        }catch (e: InterruptedException){
                            e.printStackTrace()
                        }
                        return "线程执行完毕"
                    }

                    override fun runOnUi(t: String?) {
                        findViewById<Button>(R.id.btn).text = t
                    }
                })
                .start()
        }

    }
}
