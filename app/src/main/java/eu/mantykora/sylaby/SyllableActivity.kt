package eu.mantykora.sylaby

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import eu.mantykora.sylaby.model.Placeholder
import eu.mantykora.sylaby.model.Syllable
import kotlinx.android.synthetic.main.activity_syllable.*

class SyllableActivity : AppCompatActivity() {
    val placeDimensions1: IntArray = intArrayOf(0, 0)
    val placeDimensions2: IntArray = intArrayOf(0, 0)

    val booleanArray = arrayOf(false, false, false, false)
    val isFreeBooleanArray = arrayOf(true, true)
    val placeDimensionsIntArray: Array<IntArray> = arrayOf(placeDimensions1, placeDimensions2)
    val syll1 = mutableListOf<Syllable>()
    val place1 = mutableListOf<Placeholder>()

    var placeIndex: Int = 0

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

                syll1.addAll(
                        listOf(
                                Syllable(content = "ma", buttonIndex = 0, buttonDimensionX = syllableDimensions1.get(0), buttonDimensionY = syllableDimensions1.get(1)),
                                Syllable(content = "ta", buttonIndex = 1, buttonDimensionX = syllableDimensions2.get(0), buttonDimensionY = syllableDimensions2.get(1)),
                                Syllable(content = "ma", buttonIndex = 2, buttonDimensionX = syllableDimensions3.get(0), buttonDimensionY = syllableDimensions3.get(1)),
                                Syllable(content = "ba", buttonIndex = 3, buttonDimensionX = syllableDimensions4.get(0), buttonDimensionY = syllableDimensions4.get(1))
                        )
                )

                place1.addAll(
                        listOf(
                               Placeholder(0, placeDimensions1.get(0), placeDimensions1.get(1)),
                                Placeholder(1, placeDimensions2.get(0), placeDimensions2.get(1))
                        )
                )

                val clickListener: View.OnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                       // R.id.syllable1 -> moveButton(0, placeDimensions1, view, syllableDimensions1)
//                        R.id.syllable2 -> moveButton(1, placeDimensions1, view, syllableDimensions2)
//                        R.id.syllable3 -> moveButton(2, placeDimensions1, view, syllableDimensions3)
//                        R.id.syllable4 -> moveButton(3, placeDimensions1, view, syllableDimensions4)

                        R.id.syllable1 -> moveButton1(0, view)
                        R.id.syllable2 -> moveButton1(1, view)
                        R.id.syllable3 -> moveButton1(2, view)
                        R.id.syllable4 -> moveButton1(3, view)



                    }


                }
                syllable1.setOnClickListener(clickListener)
                syllable2.setOnClickListener(clickListener)
                syllable3.setOnClickListener(clickListener)
                syllable4.setOnClickListener(clickListener)


            }

        })

    }


    private fun moveButton1(index: Int, v: View?)  {

        //search for first free placeholder


//        if (placeIndex == -1) {
//
//        }



        //placeholder.copy(isFree = false)


        //val placeholder = place1[placeIndex]


        val syllable = syll1[index];
        if (!syllable.isMoved) {

            syll1[index] = syllable.copy(isMoved = true)

            placeIndex = place1.indexOfFirst {
                it.isFree
            }

            val placeholder = place1[placeIndex]
            place1[placeIndex] = placeholder.copy(isFree = false)

            syll1[index] = syllable.copy(isMoved = true, placeholderIndex = placeIndex)

            v!!.animate().x(placeholder.placeholderDimensionX.toFloat()).y(placeholder.placeholderDimensionY.toFloat())

        } else {
            syll1[index] = syllable.copy(isMoved = false)


            val returnPlaceholderIndex: Int = syllable.placeholderIndex

            val placeholder = place1[returnPlaceholderIndex]


            place1[returnPlaceholderIndex] = placeholder.copy(isFree = true)

            v!!.animate().x(syllable.buttonDimensionX.toFloat()).y(syllable.buttonDimensionY.toFloat())



        }


    }

//    private fun moveButton(index: Int, placeDimensions: IntArray, v: View?, syllableDimensions: IntArray) {
//
//        val placeIndex = goToRightPlace()
//
//
//        if (!booleanArray.get(index)) {
//
//            booleanArray.set(index, true)
//            val place: IntArray = placeDimensionsIntArray.get(placeIndex)
//            val float: Float = place.get(0).toFloat();
//            val float2: Float = place.get(1).toFloat();
//            v!!.animate().x(float).y(float2)
//        } else {
//            booleanArray.set(index, false)
//            isFreeBooleanArray.set(placeIndex, true)
//            v!!.animate().x(syllableDimensions.get(0).toFloat()).y(syllableDimensions.get(1).toFloat())
//
//        }
//    }
//
//    private fun goToRightPlace(): Int {
//        for (i in isFreeBooleanArray.indices) {
//            if (i == isFreeBooleanArray.size - 1 && isFreeBooleanArray.get(i) == true) {
//                //last iteration
//                Log.d("lastIteration", "message")
//
//            }
//            if (isFreeBooleanArray.get(i) == true) {
//                isFreeBooleanArray.set(i, false)
//
//                return i
//            }
//
//
//        }
//
//        for (i in isFreeBooleanArray.indices) {
//            isFreeBooleanArray.set(i, true)
//
//        }
//        return 0
//    }


//    private fun foToRightPlace1():


}


