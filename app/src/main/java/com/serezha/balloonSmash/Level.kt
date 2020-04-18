package com.serezha.balloonSmash

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import kotlin.collections.ArrayList
import kotlin.random.Random
import com.serezha.balloonSmash.GameBody.BallType

class Level(context: GameView) {

    private val gameView = context
    private val background = Background(gameView.context)
    private val draws = Draws()
    var score = 1
    private val logic = Logic(gameView)
    val ballArr: Balloons = Balloons(ArrayList<Balloon>(), draws, gameView)

    init {
        draws.add(background)
        ballArr.addBalloons(1,true)
    }

    fun draw(paint: Paint, canvas: Canvas) {
        draws.draw(paint, canvas)


    }

    fun update() {
        logic.work(ballArr)
    }

    class Balloons(val balls: ArrayList<Balloon>, val draws: Draws, val gameView: GameView) {
        init {
            Balloon(gameView.context, GameView.Cord(),BallType.Balloon)
        }
        val count = 0
        val size: Int
        get() {
            return balls.size
        }
        fun add(some: Balloon) {
            balls.add(some)
            draws.add(some)
        }

        operator fun get(index: Int): Balloon {
            return balls[index]
        }



        fun remove(b: Balloon) {
            draws.rem(b)
            balls.remove(b)
        }

        fun addBalloons(n: Int) {
            Log.d("mytag","mtav add")
            val randArr = randomCords(n, gameView)
            val randomTypes = RandomTypes(0.65f,0.16f,0.02f,0.17f)
            val randType = randomTypes.randomTypes(n)
            for (i in 0 until n) {
                val ball = Balloon(gameView.context, randArr[i],randType[i])
                balls.add(ball)
                draws.add(ball)
            }
        }
        fun addBalloons(n: Int,no_type:Boolean) {
            if(no_type) {
                Log.d("mytag", "mtav add")
                val randArr = randomCords(n, gameView)
                for (i in 0 until n) {
                    val ball = Balloon(gameView.context, randArr[i], BallType.Balloon)
                    balls.add(ball)
                    draws.add(ball)
                }
            }
        }
        class RandomTypes(red:Float,grey:Float,orange:Float,bomb:Float) {
            private val red_pr = (red*100f).toInt()
            private val grey_pr = (grey*100f).toInt()
            private val orange_pr = (orange*100f).toInt()
            private val bomb_pr = (bomb*100f).toInt()


            fun randomTypes(i: Int): ArrayList<BallType> {
                val arr = ArrayList<BallType>()
                var type = BallType.Balloon
                var temp:Int = 0
                val goldPr = (0 until temp + orange_pr)
                temp += orange_pr
                val bombPr = (temp until temp + bomb_pr)
                temp += bomb_pr
                val bronzPr = (temp until temp + grey_pr)
                temp += grey_pr
                val ballPr = (temp until temp + red_pr)
                for (j in (0 until i)) {
                    when (Random.nextInt(goldPr.first, ballPr.last)) {
                        in bombPr -> type = BallType.Bomb
                        in ballPr -> type = BallType.Balloon
                        in goldPr -> type = BallType.Golden
                        in bronzPr -> type = BallType.Bronz
                    }
                    arr.add(type)
                }
                return arr

            }
        }

        companion object {

            fun randomCords(n: Int, gameView: GameView): ArrayList<GameView.Cord> {
                val ball = Balloon(gameView.context, GameView.Cord(),BallType.Balloon)

                val width = gameView.holder.surfaceFrame.width()
                val height = gameView.holder.surfaceFrame.height()
                val corArray = ArrayList<GameView.Cord>()
                for (i in 0 until n) {

                    val x = Random.nextInt(ball.body.width / 2, (width - ball.body.width / 2))
                    val y = Random.nextInt(ball.body.height / 2, (height - ball.body.height / 2))
                    corArray.add(GameView.Cord(x, y))
                }
                return corArray
            }
        }
    }
}
