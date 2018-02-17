package ui.anwesome.com.squarejoinerview

/**
 * Created by anweshmishra on 17/02/18.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*

class SquareJoinerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    var squareJoinListener:SquareJoinListener ?= null
    fun addOnSquareJoinListener(onSquareJoinListener: () -> Unit) {
        squareJoinListener = SquareJoinListener(onSquareJoinListener)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var j:Int = 0, var prevScale:Float = 0f, var jDir:Int = 1, var dir:Float = 0f) {
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
    data class Renderer(var view: SquareJoinerView, var time: Int = 0) {
        val animator = Animator(view)
        var joiner:SquareJoiner ?= null
        fun render(canvas:Canvas, paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                joiner = SquareJoiner(w/2, h/2, 2*Math.min(w,h)/3)
                paint.color = Color.parseColor("#1A237E")
                paint.strokeWidth = Math.min(w,h)/45
                paint.strokeCap = Paint.Cap.ROUND
            }
            canvas.drawColor(Color.parseColor("#212121"))
            joiner?.draw(canvas,paint)
            time++
            animator.animate {
                joiner?.update {
                    animator.stop()
                    when(it) {
                        1f -> view.squareJoinListener?.onSquareJoinListener?.invoke()
                    }
                }
            }
        }
        fun handleTap() {
            joiner?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):SquareJoinerView {
            val view = SquareJoinerView(activity)
            activity.setContentView(view)
            return view
        }
    }
    data class SquareJoinListener(var onSquareJoinListener:() -> Unit)
}