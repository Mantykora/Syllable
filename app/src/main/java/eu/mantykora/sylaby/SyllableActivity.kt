package eu.mantykora.sylaby

import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageButton
import eu.mantykora.sylaby.model.Placeholder
import eu.mantykora.sylaby.model.Syllable
import kotlinx.android.synthetic.main.activity_syllable.*
import kotlinx.android.synthetic.main.success_alert_dialog.*

class SyllableActivity : AppCompatActivity() {
    val placeDimensions1: IntArray = intArrayOf(0, 0)
    val placeDimensions2: IntArray = intArrayOf(0, 0)

    val booleanArray = arrayOf(false, false, false, false)
    val isFreeBooleanArray = arrayOf(true, true)
    val placeDimensionsIntArray: Array<IntArray> = arrayOf(placeDimensions1, placeDimensions2)
    val syllables = mutableListOf<Syllable>()
    val placeholders = mutableListOf<Placeholder>()


    var attributes: AudioAttributes? = null
    var soundPool: SoundPool? = null
    var succcessSoundId: Int = 0
    var errorSoundId: Int = 0
    var clickSoundId: Int = 0


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

                syllables.addAll(
                        listOf(
                                Syllable(content = "ma", buttonIndex = 0, buttonDimensionX = syllableDimensions1.get(0), buttonDimensionY = syllableDimensions1.get(1), id = R.id.syllable1),
                                Syllable(content = "ta", buttonIndex = 1, buttonDimensionX = syllableDimensions2.get(0), buttonDimensionY = syllableDimensions2.get(1), id = R.id.syllable2),
                                Syllable(content = "ma", buttonIndex = 2, buttonDimensionX = syllableDimensions3.get(0), buttonDimensionY = syllableDimensions3.get(1), id = R.id.syllable3),
                                Syllable(content = "ba", buttonIndex = 3, buttonDimensionX = syllableDimensions4.get(0), buttonDimensionY = syllableDimensions4.get(1), id = R.id.syllable4)
                        )
                )

                placeholders.addAll(
                        listOf(
                               Placeholder(0, placeDimensions1.get(0), placeDimensions1.get(1)),
                                Placeholder(1, placeDimensions2.get(0), placeDimensions2.get(1))
                        )
                )

                syllable1.setText(syllables[0].content)
                syllable2.setText(syllables[1].content)
                syllable3.setText(syllables[2].content)
                syllable4.setText(syllables[3].content)

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

        attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

         soundPool = SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build()

        if (soundPool != null) {
            succcessSoundId = soundPool!!.load(applicationContext, R.raw.success, 1)
            errorSoundId = soundPool!!.load(applicationContext, R.raw.error, 1)
            clickSoundId = soundPool!!.load(applicationContext, R.raw.click, 1)


            //releae SoundPool resources after use
            //soundPool.release()
        }
    }


    private fun moveButton1(index: Int, v: View?)  {

        soundPool!!.play(clickSoundId, 1F, 1F, 1, 0, 1F)

        var placeIndex: Int = 0



        val syllable = syllables[index];
        if (!syllable.isMoved) {

            syllables[index] = syllable.copy(isMoved = true)

            //search for first free placeholder

            placeIndex = placeholders.indexOfFirst {
                it.isFree
            }

            if (placeIndex != -1) {

                val placeholder = placeholders[placeIndex]
                placeholders[placeIndex] = placeholder.copy(isFree = false, syllableIndex = index)

                syllables[index] = syllable.copy(isMoved = true, placeholderIndex = placeIndex)

                v!!.animate().x(placeholder.placeholderDimensionX.toFloat()).y(placeholder.placeholderDimensionY.toFloat()).withEndAction(Runnable {


                    if (placeIndex == placeholders.size - 1) {


                        Handler().postDelayed({

                            evaluateWord(v)
                            Log.d("last", placeIndex.toString())
                            //doSomethingHere()
                        }, 500)


                    }
                })


            }
        } else {
            syllables[index] = syllable.copy(isMoved = false)

            val returnPlaceholderIndex: Int = syllable.placeholderIndex

            val placeholder = placeholders[returnPlaceholderIndex]


            placeholders[returnPlaceholderIndex] = placeholder.copy(isFree = true)

            v!!.animate().x(syllable.buttonDimensionX.toFloat()).y(syllable.buttonDimensionY.toFloat())



        }


    }


    private fun evaluateWord(v: View?) {

        val word: String = "mama"
        var composedWord: String = ""

        placeholders.get(0)

        placeholders.forEach {
           composedWord += syllables[it.syllableIndex].content
        }

        if (word == composedWord) {

            soundPool!!.play(succcessSoundId, 1F, 1F, 1, 0, 1F)

           showAlertDialog()

            return

            //TODO show success popup (block touchscreen)
        } else {

            placeholders.forEach {
                val syllable = syllables[it.syllableIndex];
                findViewById<Button>(syllables[it.syllableIndex].id).animate().x(syllables[it.syllableIndex].buttonDimensionX.toFloat()).y(syllables[it.syllableIndex].buttonDimensionY.toFloat())

//                syllables[it.syllableIndex] = syllables[it.syllableIndex].copy(isMoved = false)
                syllables.set(it.syllableIndex, syllable.copy(isMoved = false))
                val c = 0

                it.isFree = true


                soundPool!!.play(errorSoundId, 1F, 1F, 1, 0, 1F)

                // placeholders.set(is)
//                val placeholder = placeholders[placeIndex]
//                it = placeholder.copy(isFree = true)



            }

            //TODO return all buttons


        }

        Log.d("tag", word + composedWord)



    }

    private fun showAlertDialog() {

        val builder = AlertDialog.Builder(this@SyllableActivity)
        builder.setTitle("BRAWO!")

       val view = layoutInflater.inflate(R.layout.success_alert_dialog, null)
        builder.setView(view)

        val refreshButton = view.findViewById<ImageButton>(R.id.refresh_alert_button)

        refreshButton.setOnClickListener(View.OnClickListener {

            Log.d("click", "refresh")
        })

        val nextButton = view.findViewById<ImageButton>(R.id.next_alert_button)

        nextButton.setOnClickListener(View.OnClickListener {
            Log.d("click", "next")
        })




        val dialog: AlertDialog = builder.create()

        dialog.show()


    }


}

private fun ImageButton.setOnClickListener(clickListener: DialogInterface.OnClickListener) {
   Log.d("click", "refresh")

    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}


//TODO odglosy