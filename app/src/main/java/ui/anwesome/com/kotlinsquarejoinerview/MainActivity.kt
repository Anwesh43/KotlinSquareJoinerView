package ui.anwesome.com.kotlinsquarejoinerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.squarejoinerview.SquareJoinerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = SquareJoinerView.create(this)
        view.addOnSquareJoinListener {
            Toast.makeText(this, "square joined",Toast.LENGTH_SHORT).show()
        }
        fullScreen()
    }
}
fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
