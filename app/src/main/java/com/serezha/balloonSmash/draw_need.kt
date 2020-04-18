package com.serezha.balloonSmash

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import kotlin.math.min
import kotlin.properties.Delegates

class Draws  {

    fun draw(paint: Paint, canvas: Canvas) {
            for (dr in drawArr)
                dr.draw(paint, canvas)

    }
    fun add(some:GameBody.MyDrawable){
        drawArr.add(some)
    }

    fun rem(i: GameBody.MyDrawable)
    {
        drawArr.remove(i)
    }

    fun addArr(some:ArrayList<Balloon>)
    {
        for(i in some)
            drawArr.add(i)
    }
    private val drawArr = ArrayList<GameBody.MyDrawable>()

}
class AnimationList {
    private var animationSet = emptyArray<Animation>()
    val animation =  ArrayList<Animation>()

    fun draw(paint: Paint, canvas: Canvas)
    {
        val list = ArrayList<Animation>()
        for (some in animation)
        {
            if(!some.nextDraw(paint,canvas))
                list.add(some)

        }
        for(some in list)
            animation.remove(some)
    }
    fun addExample(animation: Animation)
    {
        animationSet+=animation
    }
    interface Animable{
        fun drawFrame(paint: Paint, canvas: Canvas, x:Int, y:Int)
    }
    class Frame(context: Context,id:Int,size:Float):Animable
    {
       // val d = ResourcesCompat.getDrawable(context.resources,id, null) as BitmapDrawable
        //private val cBitmap = d.bitmap!!
       // val d = ResourcesCompat.getDrawable(context.resources, id, null)
        //var cBitmap = GameBody.drawableToBitmap(d!!)
       // val cBitmap = BitmapFactory.decodeResource(context.resources,id)!!
        private val min = min(GameView.unitW,GameView.unitH)
        val balance = 9f
        var prop_w:Float = 0F //=//cBitmap.width.toFloat()/cBitmap.height.toFloat()*balance
        var width:Int = 0 //= (prop_w*size*min).toInt()
        var height = 0 //(balance*size*min ).toInt()
        private var bitmap:Bitmap?=null //= Bitmap.createScaledBitmap(
           // cBitmap, width , height, false
        //)
        init {
               val options = BitmapFactory.Options()
               options.inJustDecodeBounds = true
               BitmapFactory.decodeResource(context.resources, id, options)
               prop_w =options.outWidth.toFloat()/options.outHeight.toFloat()*balance
               val width = (prop_w*size*min).toInt()
               val height = (balance*size*min).toInt()
               val inSample = GameBody.calculateInSampleSize(options,width,height)
               options.inSampleSize = inSample
               // Decode bitmap with inSampleSize set
               options.inJustDecodeBounds = false;
               bitmap = BitmapFactory.decodeResource(context.resources, id, options)
               bitmap = Bitmap.createScaledBitmap(bitmap!!,width,height,false)
        }
        override fun drawFrame(paint: Paint, canvas: Canvas, x: Int, y: Int) {
            canvas.drawBitmap(bitmap!!, (x.toFloat()- bitmap!!.width/2), (y.toFloat()- bitmap!!.height/2),paint)
        }

    }



    class Animation(var x:Int, var y:Int) {
        var body:AnimBody = AnimBody()
            get() = field
            set(value) {
            size = value.anim_arr.size
            field = value
        }
        var pointer:Int = 0
        var size :Int = 0

        class AnimBody {
            val anim_arr = ArrayList<Animable>()
            fun addFrame(some: Animable) {
                anim_arr.add(some)
            }
        }
        fun nextDraw(paint: Paint, canvas: Canvas): Boolean {
            return if (size != 0) {
                val i = body.anim_arr[pointer]
                i.drawFrame(paint, canvas, x, y)
                pointer+=1
                size-=1
                true
            } else false
        }
            companion object {
                fun overlay(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
                    val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
                    val canvas = Canvas(bmOverlay)
                    canvas.drawBitmap(bmp1, Matrix(), null)
                    canvas.drawBitmap(bmp2, 0f, 0f, null)
                    return bmOverlay
                }
            }
        }
    }


