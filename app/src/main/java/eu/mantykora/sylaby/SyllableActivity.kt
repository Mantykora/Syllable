package eu.mantykora.sylaby

import android.animation.ObjectAnimator
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_syllable.*

class SyllableActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility =  View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        supportActionBar?.hide()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllable)
      //  val layout: ConstraintLayout = layoutInflater.inflate(R.layout.activity_syllable, null) as ConstraintLayout;

//        window.decorView.systemUiVisibility =  View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                View.SYSTEM_UI_FLAG_FULLSCREEN or
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        supportActionBar?.hide()

        val numbers: IntArray = intArrayOf(0, 0)
        val numbers3: IntArray = intArrayOf(0, 0)

        var clickedButton: Boolean = false;

        button.viewTreeObserver.addOnGlobalLayoutListener( object: ViewTreeObserver.OnGlobalLayoutListener {
               override fun onGlobalLayout() {
                    button.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    button.getLocationInWindow(numbers);
                    button3.getLocationInWindow(numbers3)
                    Log.d("syllable", numbers.get(0).toString());

                   button3.setOnClickListener(object : View.OnClickListener {
                       override fun onClick(v: View?) {

//                ObjectAnimator.ofFloat(v, "translationX", 200f).apply {
//                    duration = 2000
//
//                    start()
//                }
                           if (clickedButton == false) {
                               clickedButton = true;
                               val float: Float = numbers.get(0).toFloat();
                               val float2: Float = numbers.get(1).toFloat();
                               v!!.animate().x(float).y(float2)
                           } else {
                               clickedButton = false;
                               v!!.animate().x(numbers3.get(0).toFloat()).y(numbers3.get(1).toFloat())

                           }
                       }
                   })

                }

        })
      //  button.getLocationInWindow(numbers);

      //  Log.d("syllable", numbers.get(0).toString());



    }
}

