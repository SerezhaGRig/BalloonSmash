package com.serezha.balloonSmash

import android.graphics.Bitmap
import android.content.Context

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import java.util.*


class Balloon(context: Context, place:GameView.Cord,val type: BallType) : GameBody(context), GameBody.MyDrawable {
    override fun draw(paint: Paint, canvas: Canvas) {
        body.drawFrame(paint, canvas,x,y)
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    private val lifeTmr = Timer()
    var lifeEnded = false
    var life = type.getLife()
    val cost = type.getCost()
    override val body:BalloonBody
    var covered = false
    fun touchInBalloon(cord:GameView.Cord):Boolean
    {

        val xLeft = x - body.width/2
        Log.d("MyLog","x_left"+xLeft)
        val xRight = x + body.width/2
        Log.d("MyLog","x_right"+xRight)
        val yLeft = y - body.height/2
        Log.d("MyLog","y_left"+yLeft)
        val yRight = y + body.height/4
        Log.d("MyLog","y_right"+yRight)
        return(cord.x > xLeft && cord.x < xRight && cord.y > yLeft && cord.y < yRight)
    }
    init{
        lifeTmr.schedule(object :TimerTask(){
            override fun run() {
                lifeEnded = true
            }

        },type.getlifeTm())
        x = place.x
        y = place.y
        body = BalloonBody.getInstance(context,type)
    }
    fun update(c: GameView.Cord) {
        y=c.y
        x=c.x
    }
    fun lifeHandler():Boolean
    {
        life-=1
        return life == 0

    }
    private companion object{
        var body1:AnimationList.Animation.AnimBody? = null
        var body2:AnimationList.Animation.AnimBody? = null
        var body3:AnimationList.Animation.AnimBody? = null
        var body4:AnimationList.Animation.AnimBody? = null
        var body5:AnimationList.Animation.AnimBody? = null
        var body6:AnimationList.Animation.AnimBody? = null
        var body7:AnimationList.Animation.AnimBody? = null
    }
    fun getAnimation(): AnimationList.Animation {
        val some = AnimationList.Animation(x,y)
        when (type) {

            BallType.Balloon, BallType.Bomb -> {
                if (body1 == null) {
                    body1 = AnimationList.Animation.AnimBody()
                    val frame0 = AnimationList.Frame(context, R.drawable.red_boom4_3, 0.66f * 1.37f)
                    val frame1 =
                        AnimationList.Frame(context, R.drawable.red_boom5_1_3, 0.76f * 1.37f)
                    body1?.addFrame(frame0)
                    body1?.addFrame(frame1)
                    body1?.addFrame(frame1)
                    some.body = body1!!
                } else
                    some.body = body1!!
            }
            BallType.Golden -> {
                if (body2 == null) {
                    body2 = AnimationList.Animation.AnimBody()
                    val frame0 = AnimationList.Frame(context, R.drawable.orang_boom5, 1.1f * 1.098f)
                    val frame1 = AnimationList.Frame(context, R.drawable.orang_boom7, 1.2f * 1.098f)
                    body2?.addFrame(frame0)
                    body2?.addFrame(frame1)
                    body2?.addFrame(frame1)
                    some.body = body2!!
                } else
                    some.body = body2!!
            }
            BallType.Bronz -> {
                if (body3 == null) {
                    body3 = AnimationList.Animation.AnimBody()
                    val frame0 = AnimationList.Frame(context, R.drawable.bronz_boom2, 1.2f * 0.84f)
                    val frame1 = AnimationList.Frame(context, R.drawable.bronz_boom3, 1.2f * 1.044f)
                    body3?.addFrame(frame0)
                    body3?.addFrame(frame1)
                    body3?.addFrame(frame1)
                    some.body = body3!!
                } else
                    some.body = body3!!

            }
        }
        return some

    }

    fun getBoom(): AnimationList.Animation {
        val some = AnimationList.Animation(x,y)
        when (type) {
            BallType.Bomb -> {
                if (body4 == null) {
                    body4 = AnimationList.Animation.AnimBody()
                    val frame0 =
                        AnimationList.Frame(context, R.drawable.bomb_effect1_2, 0.825f * 1.07f)
                    val frame1 =
                        AnimationList.Frame(context, R.drawable.bombot_effect2_2, 0.95f * 1f)

                    val frame2 = AnimationList.Frame(context, R.drawable.smok_boom, 0.5f * 1.41f)
                    //val frame3 = AnimationList.Frame(context,R.drawable.points1,2.5f)
                    //some = obj.getboom()
                    body4?.addFrame(frame0)
                    body4?.addFrame(frame1)
                    body4?.addFrame(frame1)
                    body4?.addFrame(frame2)
                    some.body = body4!!
                }
                else
                    some.body = body4!!

            }
            else -> return some
        }
        return some

    }
    fun getScoreAnimation():AnimationList.Animation
    {
        val y_n = y + (body.height/8)*3
        val some1 = AnimationList.Animation(x,y_n)
        when(type)
        {
            BallType.Bomb->{
                if (body5 == null) {
                    body5 = AnimationList.Animation.AnimBody()
                    val frame = AnimationList.Frame(context, R.drawable.points1, 1.8f * 0.088f)
                    body5?.addFrame(frame)
                    body5?.addFrame(frame)
                    body5?.addFrame(frame)
                    body5?.addFrame(frame)
                    body5?.addFrame(frame)
                    some1.body = body5!!
                }
                else some1.body = body5!!
            }
            BallType.Golden->{
                if (body6 == null) {
                    body6 = AnimationList.Animation.AnimBody()
                    val frame = AnimationList.Frame(context, R.drawable.points4, 1.8f * 0.099f)
                    body6?.addFrame(frame)
                    body6?.addFrame(frame)
                    body6?.addFrame(frame)
                    body6?.addFrame(frame)
                    body6?.addFrame(frame)
                    some1.body = body6!!
                }
                else
                    some1.body = body6!!
            }
            else->{}

        }
        return some1
    }
    fun getTouchAnimation():AnimationList.Animation
    {
        val some = AnimationList.Animation(x,y)
        when (type) {
            BallType.Bronz -> {
                if (body7 == null) {
                    body7 = AnimationList.Animation.AnimBody()
                    val frame1 =
                        AnimationList.Frame(context, R.drawable.bronz_ball, 1.2f * 1.027f)//resizing
                    val frame =
                        AnimationList.Frame(context, R.drawable.bronz_ball, 1.15f * 1.027f)//
                    body7?.addFrame(frame1)
                    body7?.addFrame(frame1)
                    body7?.addFrame(frame)
                    some.body = body7!!
                } else
                    some.body = body7!!
            }
            else -> return some
        }
        return some
    }

}
class BalloonBody: GameBody.MyBody,AnimationList.Animable
{

    override fun drawFrame(paint: Paint, canvas: Canvas, x:Int, y:Int) {

        canvas.drawBitmap(bitmap, (x.toFloat()- width/2)/*GameView.unitW*/, (y.toFloat()- height/2),paint)
    }
    var bitmapId =0
    override val size:Float
    override var bitmap: Bitmap
    override val height: Int
        get() = bitmap.height
    override val width: Int
        get() = bitmap.width



    private constructor(context: Context,type: GameBody.BallType)  {
        bitmapId = type.getId()
        size = type.getSize()
        bitmap = GameBody.singlInit(context, bitmapId, size)
    }


    companion object{
        private var instance1:BalloonBody ?= null
        private var instance2:BalloonBody ?= null
        private var instance3:BalloonBody ?= null
        private var instance4:BalloonBody ?= null
        private fun singl(inst:BalloonBody?, context: Context, type: GameBody.BallType):BalloonBody{
            return inst ?: return BalloonBody(context, type)
        }
        fun getInstance(context: Context,type: GameBody.BallType):BalloonBody
        {
            val ball:BalloonBody? = when(type){
                GameBody.BallType.Balloon-> singl(instance1,context,type)
                GameBody.BallType.Bomb-> singl(instance2,context,type)
                GameBody.BallType.Golden-> singl(instance3,context,type)
                GameBody.BallType.Bronz-> singl(instance4,context,type)
            }
            return ball!!
        }
    }


}