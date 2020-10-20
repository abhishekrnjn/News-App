package com.ranjanabhishek.newsapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listData = ArrayList<Data>()
    var pageNumber =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arr = arrayOf("kotlin","c++","python","java")
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,arr)
        var mListView = findViewById<ListView>(R.id.listView)
//        mListView.adapter = arrayAdapter

    }

    inner class MyAsyncTask: AsyncTask<String,Void,ArrayList<Data>>(){
        override fun onPostExecute(result: ArrayList<Data>?) {
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: String?): ArrayList<Data> {
            val url = createUrl(params[0])
            var jsonResponse: String? = ""
            try {
                jsonResponse = makeHttpRequest(url)
            } catch (e: IOException) {
                Log.e("MainActivity", "Problem in making HTTP request $e")
            }
            val data = extractFeatureFromResponse(jsonResponse)
            return data
        }
    }

    fun makeHttpRequest(stringUrl:URL?):String?{
        var jsonResponse:String? =""
        var urlConnection:HttpURLConnection?=null
        var inputStream: InputStream?=null

        try{
            urlConnection= stringUrl?.openConnection() as HttpURLConnection
            urlConnection.requestMethod="GET"
            urlConnection.setRequestProperty("Accept","application/json")
            urlConnection.setRequestProperty("api-key","272d54c4-f02a-4dc5-acf0-7125c9284ffc")
            urlConnection.readTimeout =15000
            urlConnection.connectTimeout = 15000
            urlConnection.connect()

            if(urlConnection.responseCode==200) {
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            }
            else{
                Log.i("MainActivity","The code is: ${urlConnection.responseCode}")
            }
                urlConnection.disconnect()
        }
        catch (e:IOException){
            Log.e("MainActivity","Error Response Code ${urlConnection?.responseCode}")
        }

        return jsonResponse
    }



    fun extractFeatureFromResponse(guradianJson: String?): ArrayList<Data>{
        try {
            val baseJsonResponse = JSONObject(guradianJson)
            val response = baseJsonResponse.getJSONObject("response")
            val newsArray = response.getJSONArray("results")

            for (i in 0..9) {
                val item = newsArray.getJSONObject(i)
                val sectionName = item.getString("sectionName")
                val webTitle = item.getString("webTitle")
                val webUrl = item.getString("webUrl")

                val data = Data(sectionName, webTitle, webUrl)
                listData.add(data)
            }
        }
        catch(e: JSONException){
            Log.e("MainActivity","Problem parsing Json Response $e")
        }
        return listData
    }

    fun readFromStream(inputStream: InputStream?):String?{
        val output = StringBuilder()
        val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
        val reader = BufferedReader(inputStreamReader)
        var line : String? = reader.readLine()
        while(line!=null){
            output.append(line)
            line = reader.readLine()
        }
        return output.toString()
    }


    fun createUrl(stringUrl:String?):URL?{
        val url: URL?
        try{
            url = URL(stringUrl)
        }
        catch (e:MalformedURLException){
            Log.e("MainActivity","Error with creating URL $e")
            return null
        }
        return url
    }

    fun searchWord(view: View){
        pageNumber=1
        val stringUrl = "https://content.guardianapis.com/search?q=${edit_text.text}&tag=politics/politics&page=$pageNumber"
        listData.clear()

        var myAsyncTask = MyAsyncTask()
        myAsyncTask.execute(stringUrl)

    }
}
