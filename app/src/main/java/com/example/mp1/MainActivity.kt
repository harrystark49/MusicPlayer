package com.example.mp1

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list = ArrayList<Int>()
        var imglist = ArrayList<Int>()
        val song_name = ArrayList<String>()

        song_name.add("dusk_till_dawn")
        song_name.add("let_me_down_slowly")
        song_name.add("my_heart_will_go_on")
        song_name.add("one_piece")
        song_name.add("memories")

        list.add(R.raw.dusk)
        list.add(R.raw.let_me_down_slowly)
        list.add(R.raw.heart)
        list.add(R.raw.luffy_fierce_attack)
        list.add(R.raw.memories)

        imglist.add(R.drawable.dusk_till_dawn)
        imglist.add(R.drawable.song)
        imglist.add(R.drawable.titanic)
        imglist.add(R.drawable.luffy)
        imglist.add(R.drawable.memories)
        var i = 0
        fun oncomplete(mp:MediaPlayer){
            mp.setOnCompletionListener {
                next.callOnClick()
            }

        }

        var mp = MediaPlayer.create(this, list[i])
        play.setOnClickListener {
            if (!mp.isPlaying) {
                seekBar.progress = 0
                seekBar.max = mp.duration
                mp.start()
                play.setImageResource(R.drawable.pause)
                song_image.setImageResource(imglist[i])
                song_id.text = song_name[i]
                oncomplete(mp)

            } else {
                mp.pause()
                play.setImageResource(R.drawable.play)
            }
        }

            next.setOnClickListener {

                if(i==list.size-1){
                    Toast.makeText(this,"you've reached end of the file so final song will be played again",Toast.LENGTH_SHORT).show()
                    mp.stop()
                    mp=MediaPlayer.create(this,list[list.size-1])
                    seekBar.progress=0
                    song_image.setImageResource(imglist[list.size-1])
                    song_id.text=song_name[list.size-1]
                    seekBar.max=mp.duration
                    mp.start()
                    oncomplete(mp)
                }
               if(i<list.size-1){
                    mp.pause()
                i++;
                mp=MediaPlayer.create(this,list[i])
                seekBar.progress=0
                seekBar.max=mp.duration
                song_image.setImageResource(imglist[i])
                song_id.text=song_name[i]
                mp.start()
                   oncomplete(mp)
            }
            }
            previous.setOnClickListener {
                if(i==0){
                    mp.pause()
                    seekBar.progress=0
                    seekBar.max=mp.duration
                    song_image.setImageResource(imglist[0])
                    song_id.text=song_name[0]
                    mp=MediaPlayer.create(this,list[0])
                    mp.start()
                    oncomplete(mp)
                }
                else if(i>0){
                    mp.pause()
                i--
                mp=MediaPlayer.create(this,list[i])
                    seekBar.progress=0
                    seekBar.max=mp.duration
                    song_image.setImageResource(imglist[i])
                    song_id.text=song_name[i]
                mp.start()
                    oncomplete(mp)
            }

        }
        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mp.seekTo(progress)
                }

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {            }
        })

        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: Exception) {
                }
            }
        }).start()

    }
    fun createTimeLabel(time:Int):String{
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if(sec < 10)    timeLabel+="0"
        timeLabel +=sec

        return timeLabel
    }

    var handler=object : Handler(){
        override fun handleMessage(msg: Message) {
            var totalTime:Int = seekBar.max
            var currenposition = msg.what
            seekBar.progress=currenposition
            var elapsedTime = createTimeLabel(currenposition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currenposition)
            remaining.text = "-$remainingTime"
        }


    }

}

