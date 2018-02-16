package ui.anwesome.com.kotlinsquarejoinerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.squarejoinerview.SquareJoinerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SquareJoinerView.create(this)
    }
}
