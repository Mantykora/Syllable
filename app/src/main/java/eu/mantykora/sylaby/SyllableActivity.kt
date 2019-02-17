package eu.mantykora.sylaby

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.mantykora.sylaby.model.Level
import eu.mantykora.sylaby.model.Placeholder
import eu.mantykora.sylaby.model.Syllable
import kotlinx.android.synthetic.main.activity_syllable.*
import java.io.BufferedReader
import java.io.InputStreamReader
import android.speech.tts.TextToSpeech
import java.util.*


class SyllableActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    val placeDimensions1: IntArray = intArrayOf(0, 0)
    val placeDimensions2: IntArray = intArrayOf(0, 0)
    val placeDimensions3: IntArray = intArrayOf(0, 0)

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

    var levelList: ArrayList<Level> = ArrayList()

    var levelNumber = 0

    private var tts: TextToSpeech? = null

    private var difficulty = 0



    override fun onResume() {
        super.onResume()



        Log.d("lifecycle", "onResume")

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        supportActionBar?.hide()


    }

//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                View.SYSTEM_UI_FLAG_FULLSCREEN or
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        supportActionBar?.hide()
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllable)

        Log.d("lifecycle", "onCreate")

        val sharedPref = applicationContext?.getSharedPreferences("level", Context.MODE_PRIVATE)
        levelNumber = sharedPref?.getInt("levelInt", 0)!!

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        supportActionBar?.hide()


        val syllableDimensions1: IntArray = intArrayOf(0, 0)
        val syllableDimensions2: IntArray = intArrayOf(0, 0)
        val syllableDimensions3: IntArray = intArrayOf(0, 0)
        val syllableDimensions4: IntArray = intArrayOf(0, 0)


        //read data from json file and save it to levelList
        difficulty = intent.getIntExtra("difficultyLevel", 0)
        var rawInt = 0
        when (difficulty) {
            1 -> rawInt = R.raw.level_data1
            2 -> rawInt = R.raw.level_data2
            3 -> Log.d("syllableActivity", "3")
        }

        val gson = Gson()
        val br = BufferedReader(InputStreamReader(this.resources.openRawResource(rawInt)))

        val listType = object: TypeToken<List<Level>>() {}.type
        levelList = gson.fromJson(br, listType)




        val res = getResources()
        val drawable = levelList[levelNumber!!].imagePath
        val resID = res.getIdentifier(drawable , "drawable", getPackageName())
        level_image.setImageResource(resID)



        Log.d("levelString", levelList[0].toString())


        constraint_syllable.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                constraint_syllable.viewTreeObserver.removeOnGlobalLayoutListener(this)

                placeHolder1.getLocationInWindow(placeDimensions1)
                placeHolder2.getLocationInWindow(placeDimensions2)
                placeHolder3.getLocationInWindow(placeDimensions3)

                syllable1.getLocationInWindow(syllableDimensions1)
                syllable2.getLocationInWindow(syllableDimensions2)
                syllable3.getLocationInWindow(syllableDimensions3)
                syllable4.getLocationInWindow(syllableDimensions4)

                syllables.addAll(
                        listOf(
                                Syllable(levelList[levelNumber].syllables[0], buttonIndex = 0, buttonDimensionX = syllableDimensions1.get(0), buttonDimensionY = syllableDimensions1.get(1), id = R.id.syllable1),
                                Syllable(levelList[levelNumber].syllables[1], buttonIndex = 1, buttonDimensionX = syllableDimensions2.get(0), buttonDimensionY = syllableDimensions2.get(1), id = R.id.syllable2),
                                Syllable(levelList[levelNumber].syllables[2], buttonIndex = 2, buttonDimensionX = syllableDimensions3.get(0), buttonDimensionY = syllableDimensions3.get(1), id = R.id.syllable3),
                                Syllable(levelList[levelNumber].syllables[3], buttonIndex = 3, buttonDimensionX = syllableDimensions4.get(0), buttonDimensionY = syllableDimensions4.get(1), id = R.id.syllable4)
                        )
                )

                placeholders.addAll(
                        listOf(
                               Placeholder(0, placeDimensions1.get(0), placeDimensions1.get(1)),
                                Placeholder(1, placeDimensions2.get(0), placeDimensions2.get(1)),
                                Placeholder(2, placeDimensions3.get(0), placeDimensions3.get(1))
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

//        val level = Level()

//        val gson = GsonBuilder().setPrettyPrinting().create()
//        val string: String = gson.toJson(level)
//
//        Log.d("json", string)
        tts = TextToSpeech(this, this)


        if (levelList[levelNumber].result.size < 3) {
            placeHolder3.visibility = View.GONE
        }



    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts!!.setLanguage(Locale("pl-PL"))
            val resultArray = levelList[levelNumber].result
            var resString = ""
            resultArray.forEach {
                resString = resString.plus(levelList[levelNumber].syllables[it])
            }
            speak(resString)
        } else {
            Log.e("TTS", "Initilization Failed!")

        }
    }

    private fun speak(text: String) {

      //  val text = "hihihihi"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onStart() {
        Log.d("lifecycle", "onStart")

        super.onStart()
    }

    override fun onPause() {
        Log.d("lifecycle", "onPause")

        super.onPause()
    }

    override fun onDestroy() {
        Log.d("lifecycle", "onDestroy")
        if (tts != null) {

            tts!!.stop()
            tts!!.shutdown()
            Log.d("tts", "TTS Destroyed")

        }
        super.onDestroy()
    }

    override fun onStop() {
        Log.d("lifecycle", "onStop")

        super.onStop()
    }


    private fun moveButton1(index: Int, v: View?)  {

        soundPool!!.play(clickSoundId, 1F, 1F, 1, 0, 1F)

        var placeIndex: Int = 0

        if (levelList[levelNumber].result.size < 3 && placeholders.size > 2) {
            placeholders.removeAt(2)
            Log.d("placeholdersSze", placeholders.size.toString())
        }



        val syllable = syllables[index];
        if (!syllable.isMoved) {

            syllables[index] = syllable.copy(isMoved = true)

            //search for first free placeholder

            placeIndex = placeholders.indexOfFirst {
                it.isFree
            }

            if (placeIndex != -1) {

                speak(syllables[index].content)

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

        val resultList: ArrayList<Int>
        val reversedResultList: ArrayList<Int>
        val composedResult: ArrayList<Int> = ArrayList()


        resultList = levelList[levelNumber].result
        reversedResultList = levelList[levelNumber].resultReversed
        placeholders.get(0)



        placeholders.forEach {
           composedResult.add(it.syllableIndex)
        }




        if (resultList.equals(composedResult) || reversedResultList.equals(composedResult)) {

            soundPool!!.play(succcessSoundId, 1F, 1F, 1, 0, 1F)

            if (levelNumber == levelList.lastIndex) {
                Log.d("SyllableActivity", "last index")
                showEndOfLevelDialog()
            } else {
                showAlertDialog()

            }


            return

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

        val view = layoutInflater.inflate(R.layout.success_alert_dialog, null)
        builder.setView(view)

        val refreshButton = view.findViewById<ImageButton>(R.id.refresh_alert_button)
        val dialog: AlertDialog = builder.create()

        refreshButton.setOnClickListener(View.OnClickListener {

            recreate()
            dialog.dismiss()

            Log.d("click", "refresh")
        })


        val nextButton = view.findViewById<ImageButton>(R.id.next_alert_button)

        nextButton.setOnClickListener(View.OnClickListener {

            val sharedPref = applicationContext?.getSharedPreferences("level", Context.MODE_PRIVATE)
            sharedPref?.edit()?.putInt("levelInt", levelNumber+1)
                    ?.apply()
            levelNumber++
            recreate()
            dialog.dismiss()

            Log.d("click", "next") }
        )





        //TODO uncomment this lines, easier testing without
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        dialog.show()


        hide_dialog_bars(dialog)



    }

    private fun hide_dialog_bars(dialog: AlertDialog) {
        dialog.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        supportActionBar?.hide()
    }

    private fun showEndOfLevelDialog() {


        val builder = AlertDialog.Builder(this@SyllableActivity)

        val view = layoutInflater.inflate(R.layout.alert_dialog_level_end, null)
        builder.setView(view)

        val homeButton = view.findViewById<ImageButton>(R.id.end_home_bt)
        val dialog: AlertDialog = builder.create()

        homeButton.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@SyllableActivity, MainActivity::class.java)
            startActivity(intent)
        })

        dialog.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        dialog.show()

        hide_dialog_bars(dialog)
    }


}



