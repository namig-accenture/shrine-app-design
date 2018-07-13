package com.example.namigtahmazli.shrineappnative

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_next.apply {
            background = dropShadow(mShape = CutCornersShape(8 * density),
                    backgroundColor = color(R.color.colorPrimaryDark),
                    ripple = true,
                    rippleColor = color(android.R.color.white),
                    elevationGravity = Gravity.BOTTOM)
            setOnClickListener {
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            }
        }
    }
}
