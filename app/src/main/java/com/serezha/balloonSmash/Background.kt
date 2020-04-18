package com.serezha.balloonSmash


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

import android.util.Log
import kotlin.math.max




class Background(context: Context): GameBody(context),GameBody.MyDrawable {

    override val body: MyBody = BackBody.getInstance(context)!!

    override fun draw(paint: Paint, canvas: Canvas) {
        canvas.drawBitmap(body.bitmap, (canvas.width.toFloat()/2f- body.width.toFloat()/2f)/*GameView.unitW*/, (canvas.height.toFloat()/2f- body.height.toFloat()/2f)/*GameView.unitH*/, paint)
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    class BackBody private constructor(context: Context) : GameBody.MyBody() {
        private val bitmapId: Int = R.drawable.bgr3
        override var size: Float = 2.1f * 1.2f//2.6
        override val bitmap: Bitmap
        override val height: Int
        override val width: Int

        init {

            //  val d = context.resources.getDrawable(bitmapId)

            //val d = ResourcesCompat.getDrawable(context.resources, bitmapId, null) as BitmapDrawable
            //val cBitmap = d.bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, bitmapId, options)
            //val cBitmap = BitmapFactory.decodeResource(context.resources,bitmapId)

            Log.d("stugum","bitmp"+options.outWidth)
            val max = max(GameView.unitW, GameView.unitH)
            Log.d("stugum","max"+max)
            val balance = 9f
            val prop_w =options.outWidth.toFloat()/options.outHeight.toFloat()*balance
            //!!!!!!!!!
            //!!!
            //ogtagorci verevi qomentn u sizov dzi chapn
            //!!!
            //!!!!!!!!!


            val metrics = context.resources.displayMetrics
            val dWidth = metrics.widthPixels
            val dHeight = metrics.heightPixels
            size = culcSize(dWidth,dHeight,options)
            width = (prop_w*size*max).toInt()//2.1*max*width
            height = (balance *size*max).toInt()
            //Log.d("stugum","scale width"+width)
            val inSample = calculateInSampleSize(options,width,height)
            options.inSampleSize = inSample
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            val cBitmap = BitmapFactory.decodeResource(context.resources, bitmapId, options);
            bitmap = Bitmap.createScaledBitmap(cBitmap!!, width , height, false)
        }

        private fun culcSize(dWidth: Int, dHeight: Int,options:BitmapFactory.Options):Float {
            val max = max(GameView.unitW, GameView.unitH)
            Log.d("stugum","max"+max)
            val balance = 9f
            val prop_w =options.outWidth.toFloat()/options.outHeight.toFloat()*balance
            var size=1.0f
            while ((prop_w*size*max).toInt()<dWidth || ((balance *size*max).toInt()<dHeight)){
                size+=0.1f
            }
            return size
        }

        companion object{
            private var instance:BackBody ?= null
            fun getInstance(context: Context): BackBody?
            {
                return if(instance == null){
                    instance = BackBody(context)
                    return instance
                } else instance
            }
        }
    }
}