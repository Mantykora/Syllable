package eu.mantykora.sylaby

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_syllable.*

class SyllableActivity : AppCompatActivity() {
    val placeDimensions1: IntArray = intArrayOf(0, 0)
    val placeDimensions2: IntArray = intArrayOf(0, 0)

    val booleanArray = arrayOf(false, false, false, false)
    val isFreeBooleanArray = arrayOf(true, true)
    val placeDimensionsIntArray: Array<IntArray> = arrayOf(placeDimensions1, placeDimensions2)

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
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

        val syllableDimensions1: IntArray = intArrayOf(0, 0)
        val syllableDimensions2: IntArray = intArrayOf(0, 0)
        val syllableDimensions3: IntArray = intArrayOf(0, 0)
        val syllableDimensions4: IntArray = intArrayOf(0, 0)

        constraint_syllable.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                constraint_syllable.viewTreeObserver.removeOnGlobalLayoutListener(this)

                placeHolder1.getLocationInWindow(placeDimensions1)
                placeHolder2.getLocationInWindow(placeDimensions2)

                syllable1.getLocationInWindow(syllableDimensions1)
                syllable2.getLocationInWindow(syllableDimensions2)
                syllable3.getLocationInWindow(syllableDimensions3)
                syllable4.getLocationInWindow(syllableDimensions4)

                val clickListener: View.OnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.syllable1 -> moveButton(0, placeDimensions1, view, syllableDimensions1)
                        R.id.syllable2 -> moveButton(1, placeDimensions1, view, syllableDimensions2)
                        R.id.syllable3 -> moveButton(2, placeDimensions1, view, syllableDimensions3)
                        R.id.syllable4 -> moveButton(3, placeDimensions1, view, syllableDimensions4)


                    }


                }
                syllable1.setOnClickListener(clickListener)
                syllable2.setOnClickListener(clickListener)
                syllable3.setOnClickListener(clickListener)
                syllable4.setOnClickListener(clickListener)


            }

        })

    }

    private fun moveButton(index: Int, placeDimensions: IntArray, v: View?, syllableDimensions: IntArray) {
//        for (i in isFreeBooleanArray.indices) {
//            if (isFreeBooleanArray.get(i) == true) {
//                isFreeBooleanArray.set(i, false)
//
//                return placeDimensionsIntArray(i)
//            }
//        }

        val placeIndex = goToRightPlace()

        if (!booleanArray.get(index)) {
            booleanArray.set(index, true)
            val place: IntArray = placeDimensionsIntArray.get(placeIndex)
            val float: Float = place.get(0).toFloat();
            val float2: Float = place.get(1).toFloat();
            v!!.animate().x(float).y(float2)
        } else {
            booleanArray.set(index, false)
            isFreeBooleanArray.set(placeIndex, true)
            v!!.animate().x(syllableDimensions.get(0).toFloat()).y(syllableDimensions.get(1).toFloat())

        }
    }

    private fun goToRightPlace(): Int {
        for (i in isFreeBooleanArray.indices) {
            if (isFreeBooleanArray.get(i) == true) {
                isFreeBooleanArray.set(i, false)

                return i
            }
        }

        for (i in isFreeBooleanArray.indices) {
            isFreeBooleanArray.set(i, true)

        }
        return 0
    }
}


