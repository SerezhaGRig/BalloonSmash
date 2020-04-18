package com.serezha.balloonSmash

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import kotlin.math.min
import android.graphics.BitmapFactory




abstract class GameBody(protected val context: Context) {


    var x = 1
    var y = 1
    enum class BallType(private val id:Int,private val size: Float,private val cost:Int,private val lifeTime:Long,private val life:Int){
    Balloon(R.drawable.red_ball_3,0.7f*1.309f,10,8000,1),Bomb(R.drawable.red_bomb,0.58f*1.62f,-50,1300,1),
        Golden(R.drawable.good_king2,1.1f*0.77f,70,1000,1),Bronz(R.drawable.bronz_ball,1.1f*1.03f,40,8000,3);
        fun getId() = id
        fun getCost() = cost
        fun getlifeTm()= lifeTime
        fun getLife() = life
        fun getSize() = size
    }

    abstract val body:MyBody
    abstract fun draw(paint: Paint, canvas: Canvas)
    abstract fun update()


    abstract class MyBody{
        //abstract val bitmapId :Int
        abstract val size:Float
        abstract val bitmap: Bitmap
        abstract val height: Int
        abstract val width: Int
    }


    interface MyDrawable{
        fun draw(paint: Paint, canvas: Canvas)
    }


    companion object {
        fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }
        fun drawableToBitmap(drawable: Drawable): Bitmap {

            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            val bitmap =
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        fun singlInit(context: Context, bitmapId:Int, size:Float):Bitmap
        {
           // val d = ResourcesCompat.getDrawable(context.resources, bitmapId, null);
            //val cBitmap = drawableToBitmap(d!!)
            //val d = ResourcesCompat.getDrawable(context.resources, bitmapId, null) as BitmapDrawable
            //val cBitmap = d.bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, bitmapId, options)
            val balance = 9f
            Log.d("stugum","type size"+size)
            val prop_w =options.outWidth.toFloat()/options.outHeight.toFloat()*balance
            //val cBitmap = BitmapFactory.decodeResource(context.resources,bitmapId)
            val min = min(GameView.unitW,GameView.unitH)
            val width = (prop_w*size*min).toInt()
            val height = (balance*size*min).toInt()
            val inSample = GameBody.calculateInSampleSize(options,width,height)
            Log.d("MyLog","insample"+inSample)
            options.inSampleSize = inSample
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            val cBitmap =  BitmapFactory.decodeResource(context.resources, bitmapId, options)
            return Bitmap.createScaledBitmap(
                cBitmap, width , height, false
            )
        }
    }
    class TimeEnd(
        private val gameView: GameView
    ) : MyBody(),MyDrawable {
        override fun draw(paint: Paint, canvas: Canvas) {
            val x = gameView.holder.surfaceFrame.width()/2
                Log.d("Logot",""+x)
            val y = gameView.holder.surfaceFrame.height()/2
            canvas.drawBitmap(bitmap, x.toFloat()-width/2, y.toFloat()-height/2,paint)
        }

        override val size: Float = 5f*0.4f
        override val bitmap: Bitmap
        private val bId = R.drawable.time_ended3
        override val height: Int
            get() = bitmap.height
        override val width: Int
            get() = bitmap.height
        init {
            bitmap = singlInit(gameView.context, bId, size)
        }

    }

}
