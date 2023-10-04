package com.example.translator_tests_lilprj

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var tospeak: Button? = null
    private var orgtext: EditText? = null
    private var listento: Button? = null
    private var translatedtext: TextView? = null
    private var flagf: TextView? = null
    private var flags: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tospeak = findViewById(R.id.say_it)
        orgtext = findViewById(R.id.original_text)
        translatedtext = findViewById(R.id.translated_text)
        flagf = findViewById(R.id.flagOfOrgTxt)
        flags = findViewById(R.id.flagOfTranslatedTxt)
        listento = findViewById(R.id.listent_to)


        tospeak!!.isEnabled = true

        // TextToSpeech(Context: this, OnInitListener: this)
        tts = TextToSpeech(this, this)

        tospeak!!.setOnClickListener { speakOut() }
        listento!!.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            startActivityForResult(intent, 10) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                10 -> {
                    val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    var pre_text = "${orgtext!!.text.toString()}"
                    pre_text+=text!![0] +".  "
                    orgtext!!.setText(pre_text)
                }
            }
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.ROOT)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                tospeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = orgtext!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


    fun changetranslations(view: View){
        var dump = ""
        dump = flagf!!.text.toString()
        flagf!!.text = flags!!.text.toString()
        flags!!.text = dump
        dump = ""

        dump = orgtext!!.text.toString()
        orgtext!!.setText(translatedtext!!.text.toString())
        translatedtext!!.text = dump
    }
}