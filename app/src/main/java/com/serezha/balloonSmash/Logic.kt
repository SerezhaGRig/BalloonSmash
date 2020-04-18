package com.serezha.balloonSmash

import android.media.AudioManager
import android.media.SoundPool
import android.util.Log

import java.util.*
import kotlin.collections.ArrayList
import android.os.Build




class Logic(private val gameView: GameView) {


    private var firstTime = true
    lateinit var ballArr:Level.Balloons
    private lateinit var spamer:Spamer
    val mSoundPool:SoundPool
    val ball_s:Int
    val bomb_s:Int
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = SoundPool.Builder().setMaxStreams(10).build()

        } else {
            @Suppress("DEPRECATION")
            mSoundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 1)

        }
        ball_s = mSoundPool.load(gameView.context, R.raw.nw_pop, 1)
        bomb_s = mSoundPool.load(gameView.context, R.raw.bomb_compl, 1)
    }



    fun work(ball_arr:Level.Balloons) {
        if (firstTime) {
            firstTime = false
            spamer = Spamer(ball_arr,gameView)

        }
        lifeTimeHandling(gameView.level)
        touchHandling(gameView.level)
        spamer.work()

    }
    private fun lifeTimeHandling(level: Level) {
        val removeList = ArrayList<Balloon>()
        for (i in (level.ballArr.size-1) downTo 0) {
            if(level.ballArr[i].lifeEnded){
                removeList.add((level.ballArr[i]))
                 }
            }
        for(i in removeList)
        {
            val some = i.getAnimation()
            gameView.animationList.animation.add(some)
            mSoundPool.play(ball_s, 0.05f, 0.05f, 1, 0, 1f)
            level.ballArr.remove(i)
        }


    }

    private fun touchHandling(level:Level)
    {
        val c = gameView.cord
        synchronized(c.used) {
            if (!c.used) {
                c.used = true
                for (i in (level.ballArr.size-1) downTo 0) {
                    if (level.ballArr[i].touchInBalloon(c)) {

                        if(level.ballArr[i].lifeHandler()) {
                            var some = level.ballArr[i].getAnimation()
                            when(level.ballArr[i].type)
                            {
                                GameBody.BallType.Bomb->{

                                    val some1 = level.ballArr[i].getScoreAnimation()
                                    some = level.ballArr[i].getBoom()
                                    gameView.animationList.animation.add(some1)
                                    mSoundPool.play(bomb_s, 1f, 1f, 1, 0, 1f)
                                    //level.score += level.ballArr[i].cost
                                    if (level.score>50) {
                                        level.score += level.ballArr[i].cost
                                    }
                                }
                                GameBody.BallType.Golden->{
                                    val some1 = level.ballArr[i].getScoreAnimation()
                                    gameView.animationList.animation.add(some1)
                                    mSoundPool.play(ball_s, 0.05f, 0.05f, 1, 0, 1f)
                                    level.score += level.ballArr[i].cost
                                }
                                else->{
                                    mSoundPool.play(ball_s, 0.05f, 0.05f, 1, 0, 1f)
                                    level.score += level.ballArr[i].cost
                                }
                            }
                           // if (level.ballArr[i].type!=GameBody.BallType.Bomb) {
                             //   level.score += level.ballArr[i].cost
                            //}
                            //else if (level.score>50) {
                              //  level.score += level.ballArr[i].cost
                            //}

                            level.ballArr.remove(level.ballArr[i])

                            gameView.animationList.animation.add(some)
                            break
                        }
                        else{
                            val some = level.ballArr[i].getTouchAnimation()
                            gameView.animationList.animation.add(some)
                            break
                        }

                    }

                }

            }

        }
    }
    class Spamer(val ball_arr: Level.Balloons,val gameView: GameView)
    {
        var cycles=0
        var count = 0
        private val timer1 = Timer()
        private val timer2= Timer()
        private val initTmr = Timer()
        var initCompl = false
        var stopTimer1 = false
        var stopTimer2 = true
        var intime = false
        var now = false
        private val task2 = object : TimerTask() {
            override fun run() {
                if (!gameView.stopped) {
                    if (!stopTimer2) {
                        Log.d("MyTask", "in task 2")
                        intime = true
                        cycles += 1
                    }
                }

            }

        }
        private val task1 = object: TimerTask(){
            override fun run() {
                if (!gameView.stopped) {
                    if (!stopTimer1) {
                        Log.d("MyTask", "in task 1")
                        intime = true
                    }
                }
            }

        }
        private val timerTask = object :TimerTask(){
            override fun run() {
                if (!gameView.stopped) {
                    if (count == 2) {
                        initCompl = true
                        addTasks()
                        this.cancel()

                    } else {
                        Log.d("Mytag", "avelacav" + count)
                        synchronized(ball_arr) {
                            now = true
                            count += 1
                        }
                    }
                }
            }

        }

        init {
            initTmr.schedule(timerTask,500,500)
        }

        fun work(){
            if (initCompl) {
                if (intime) {
                    intime = false
                    if (ball_arr.size < 5)
                        ball_arr.addBalloons(1)
                }
                if (cycles > 1) {
                    cycles = 0
                    stopTimer1 = false
                    stopTimer2 = true

                }
                if (ball_arr.size == 0) {
                    ball_arr.addBalloons(1)
                    stopTimer1 = true
                    stopTimer2 = false

                }
            }
            else{
                if (now) {
                    now = false
                    ball_arr.addBalloons(1)
                }
            }
        }
        private fun addTasks() {
            timer1.schedule(task1,5000,5000)
            timer2.schedule(task2,2000,2000)
        }
    }
}