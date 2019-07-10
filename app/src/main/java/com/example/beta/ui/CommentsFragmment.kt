package com.example.beta.ui


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.beta.R
import com.example.beta.data.CommentsData
import com.example.beta.others.HttpRequest
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_comments_fragmment.*
import org.json.JSONObject
import java.net.URL

class CommentsFragmment : Fragment() {

    private var url: String? = null
    private var method: String? = null
    private var json: JSONObject? = null
    private var mSubmitComment:AsyncTaskSubmitFeedback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_fragmment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val comment = fragment_comments_com_text
        val rating = fragment_comments_rating

        comment.setBackgroundColor(Color.TRANSPARENT)

        fragment_comments_submit.setOnClickListener {

            submitComment()
        }

        json = JSONObject()
    }

    fun submitComment(){

        url = "https://turisnova.appspot.com/rest/comments/getComments"
        method = "POST"

        mSubmitComment = AsyncTaskSubmitFeedback()
        mSubmitComment!!.execute()
        val result = mSubmitComment!!.get()
        val code = result[0]

        when(code){

        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskSubmitFeedback internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {
            if (false) {
                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {
            return HttpRequest().doHTTP(URL(url), json!!, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mSubmitComment = null
        }

        override fun onCancelled() {
            mSubmitComment = null
        }
    }
}
