package com.serezha.balloonSmash

import android.content.Context
import android.graphics.Canvas

import android.graphics.Paint
import android.view.SurfaceView

import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View

import android.util.Log
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.TextView
import java.util.*
import android.net.Uri
import android.os.Handler
import android.util.DisplayMetrics
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build


private const val RECORD = "record"
class GameView( context:Context) : SurfaceView(context),Runnable, View.OnTouchListener {
     // размер по вертикали
    companion object{
         val max = 20 // размер по горизонтали

         var unitW = 1f  // пикселей в юните по горизонтали
         var unitH = 1f // пикселей в юните по вертикали
     }

    //val draw_objs = Draws()
    private var firstTime = true

    private var gameThread:Thread
    private var surfaceHolder:SurfaceHolder
    private var paint:Paint
    var time = 40
    private var gameTime = Timer()
    var gameRunning = true
    var stopped = false
    private lateinit var canvas:Canvas
    lateinit var level:Level

    //lateinit var balloon:Balloon
    var cord:Cord
    val animationList = AnimationList()


    init
    {
        //draw_objs.add(Balloon(this.context, Cord()))
        gameTime.schedule(object: TimerTask() {
            override fun run() {
                if(!stopped) {
                    time -= 1
                }
            }
        },1000,1000)
        cord = Cord()

        paint = Paint()
        surfaceHolder = holder
        setOnTouchListener(this)
        gameThread = Thread(this)
        gameThread.start()
    }


    override fun run(){
        while (gameRunning) {
            if (stopped) {
            Thread.sleep(300)
        }
            while(!stopped) {


                update()
                draw()
                control()
            }
        }

    }


    private fun update()
    {
        if(!firstTime) {
                level.update()
        }
    }

    private fun draw(){
        if (surfaceHolder.surface.isValid) {  //проверяем валидный ли surface

            if(firstTime){ // инициализация при первом запуске
                firstTime = false
                unitW = surfaceHolder.surfaceFrame.width().toFloat()/max.toFloat() // вычисляем число пикселей в юните
                unitH = surfaceHolder.surfaceFrame.height().toFloat()/max.toFloat()
                this.level= Level(this)
                //balloon = Balloon(this.context,this.cord); // добавляем корабль
            }

            canvas = surfaceHolder.lockCanvas() // закрываем canvas

            //canvas = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //    surfaceHolder.lockHardwareCanvas()
           // } else surfaceHolder.lockCanvas()
            //try to use for rendering on GPU
            //canvas.drawColor(Color.WHITE) // заполняем фон чёрным

            //draw_objs.draw(paint, canvas); // рисуем корабль
            level.draw(paint,canvas)
            animationList.draw(paint,canvas)
            val mainHandler = Handler(context.mainLooper)

            val myRun = Runnable {
                val textView = (context as Activity).findViewById(R.id.textot) as TextView
                textView.text=("Score:"+level.score+"   Time:"+time)

            }
            mainHandler.post(myRun)
            //val textView = (context as Activity).findViewById(R.id.textot) as TextView
            //textView.text=("Score:"+level.score+"   Time:"+time)
            surfaceHolder.unlockCanvasAndPost(canvas) // открываем canvas
        }
    }


    private fun control() {

        Thread.sleep(17)

        if (time<=0) {
            gameRunning = false
            stopped = true
            val ended =GameBody.TimeEnd(this)
            canvas = surfaceHolder.lockCanvas()
            ended.draw(paint,canvas)
            surfaceHolder.unlockCanvasAndPost(canvas)
            recSetRes()
            Thread.sleep(4000)
            val activity = context as Main3Activity
            activity.finish()
            }
        }














    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action ==(MotionEvent.ACTION_DOWN)) {
            synchronized(cord.used) {
                cord.used = false
                cord.x = event.x.toInt()
                Log.d("MyLog", "x" + cord.x)
                cord.y = event.y.toInt()
                Log.d("MyLog", "y" + cord.y)
            }
        }
        return true
    }


    class Cord{
        var used = true
        var x = 0
        var y = 0

        constructor(x1:Int,y1:Int)
        {
            x = x1
            y = y1
        }

        constructor()
    }

    fun recSetRes()
    {
        val a = RecordsBase(context)
        val mDatabase = a.writableDatabase

        val value = MainActivity.valFromBase(mDatabase)
        if(value?.toInt( )!! < level.score) {
            val data = Intent()//context,Main3Activity::class.java)

            data.data = Uri.parse(level.score.toString())
            val activity = context as Main3Activity
            if (value.toInt() < level.score)
                activity.setResult(RESULT_OK, data)
        }
    }
}