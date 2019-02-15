package eu.mantykora.sylaby

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*
import android.system.Os.shutdown
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_syllable.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (MainActivity.AUTO_HIDE) {
            delayedHide(MainActivity.AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    private var tts: TextToSpeech? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.getSharedPreferences("level", Context.MODE_PRIVATE).edit().clear().apply();


        // supportActionBar?.hide()

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
       // fullscreen_content.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
      //  dummy_button.setOnTouchListener(mDelayHideTouchListener)

        tts = TextToSpeech(this, this)

        hide()


        level1_cv.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SyllableActivity::class.java)
                startActivity(intent)

            }
        })


    }

    override fun onResume() {
        super.onResume()

        // Hide UI first
        supportActionBar?.hide()

        mVisible = false
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        //TODO z on create chowanie itd.




    }


    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
       // fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
       // mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }



    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts!!.setLanguage(Locale("pl-PL"))
            speak()
        } else {
            Log.e("TTS", "Initilization Failed!")

        }
    }

    private fun speak() {
        val text = "hihihihi"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {


        //Close the Text to Speech Library
        if (tts != null) {

            tts!!.stop()
            tts!!.shutdown()
            Log.d("tts", "TTS Destroyed")

        }

        super.onDestroy()
    }
}
