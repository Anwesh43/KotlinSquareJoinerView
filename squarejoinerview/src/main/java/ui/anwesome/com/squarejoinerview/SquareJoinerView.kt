package ui.anwesome.com.squarejoinerview

/**
 * Created by anweshmishra on 17/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*

class SquareJoinerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class State(var j:Int = 0, var prevScale:Float = 0f, var jDir:Int = 0, var dir:Float = 0f) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update(stopcb: (Float) -> Unit) {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += jDir
                if(j == scales.size || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale  = scales[j]
                    dir = 0f
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            if(dir == 0f) {
                dir = 1 - 2*prevScale
                startcb()
            }
        }
    }
    data class Animator(var view:SquareJoinerView, var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex: Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class SquareJoiner(var x:Float, var y:Float, var w:Float) {
        val state = State()
        fun draw(canvas:Canvas, paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i*90f)
                canvas.save()
                canvas.translate(-w/2, -w/2)
                for(j in 0..2) {
                    canvas.save()
                    canvas.translate(j*w/2,0f)
                    canvas.drawRect(RectF(-w/10, -w/10, w/10, w/10), paint)
                    if(j < 2) {
                        canvas.drawLine(0f, 0f, (w / 2) * state.scales[j], 0f, paint)
                    }
                    canvas.restore()
                }
                canvas.restore()
                canvas.save()
                canvas.translate(w/2, -w/2)
                canvas.drawLine(0f, 0f , -w/2 * state.scales[2] , w/2 * state.scales[2], paint)
                canvas.restore()
                canvas.restore()
            }
            canvas.drawRect(RectF(-w/10, -w/10, w/10, w/10),paint)
            canvas.restore()
        }
        fun update(stopcb: (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb: () -> Unit) {
            state.startUpdating(startcb)
        }
    }
}