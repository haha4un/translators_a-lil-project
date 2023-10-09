/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.translator_fwathces.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Nullable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.translator_fwathces.R
import com.example.translator_fwathces.presentation.theme.Translator_fwathcesTheme
import java.util.Locale
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


const val API_KEY = "cf";
class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var orgtext: String? = ""
    private var trstext: String? = "";
    private var tospeak: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
        tts = TextToSpeech(this, this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                10 -> {
                    val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    var pre_text = "${orgtext}"
                    pre_text+=text!![0] +".  "
                    orgtext = pre_text
                }
            }
        }
    }
//    fun getdata(){
//        val url = "$API_KEY"
//        val queue = Volley.newRequestQueue(this)
//        val request = StringRequest(Request.Method.GET, url,
//            {result ->Toast.makeText(this, "it resulted! $result", Toast.LENGTH_SHORT).show()},
//            {error -> Toast.makeText(this, "it erred! $error", Toast.LENGTH_SHORT).show()}
//        )
//        queue.add(request)
//    }
//    fun parsedata(result: String){
//        val obj = JSONObject(result)
//    }
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
        val text = orgtext
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


    @Composable
    fun WearApp(greetingName: String) {
        Translator_fwathcesTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center
            ) {
                maingtid()
            }
        }
    }
    fun speakin(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, 10)
    }

    @Composable
    fun maingtid() {
        var org_t by remember {
            mutableStateOf(orgtext)
        }
        var trs_t by remember {
            mutableStateOf(trstext)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))){
            //
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = Color(0xFF2F2F2F)),
                onClick = {speakin()}) {
                Text(text = "\uD83C\uDFA4",)
            }

            Spacer(modifier = Modifier.height(5.dp))

            BasicTextField(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(alignment = Alignment.CenterHorizontally)
                .border(width = 3.dp, color = Color(0xFF8D8D8D))
                .background(color = Color(0xFFF7F7F7)),
                value = org_t!!, onValueChange = {text -> org_t = text}, textStyle = TextStyle(
                    color = Color(0xFF2F2F2F),
                    textAlign = TextAlign.Center))

            Row(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally,)) {
                Text(
                    text = "🇷🇺",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF000000),
                    ))
                Text(
                    modifier = Modifier
                        .padding(5.dp, 0.dp, 5.dp, 0.dp),
                    text = "->",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF2F2F2F),))
                Text(
                    text = "🇬🇧",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF000000),))
            }
            Text(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(alignment = Alignment.CenterHorizontally)
                .border(width = 3.dp, color = Color(0xFF8D8D8D))
                .background(color = Color(0xFFF7F7F7), shape = RoundedCornerShape(size = 0.dp)),
            text = trs_t!!
            )

            Spacer(modifier = Modifier.height(5.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(color = Color(0xFF2F2F2F)),
                onClick = { speakOut() }) {
                Text(text = "\uD83D\uDD0A",)
            }
            //
        }
    }

    @Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
    @Composable
    fun DefaultPreview() {
        WearApp("Preview Android")
    }
}



