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
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
    private lateinit var json: JSONObject
    private var mSubmitComment:AsyncTaskSubmitFeedback? = null
    private var navController:NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_fragmment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")

        val comment = fragment_comments_com_text
        comment.setBackgroundColor(Color.TRANSPARENT)

        navController = Navigation.findNavController(view)

        fragment_comments_submit.setOnClickListener {

            val rating = fragment_comments_rating.text.toString().toInt()
            val text = comment.text.toString()

            submitComment(title!!, text, rating)
        }
        json = JSONObject()
    }

    fun submitComment(title:String, comment:String, rating:Int){

        url = "https://turisnova.appspot.com/rest/comments/postComment"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val username = settings.getString("username", null)

        json = JSONObject()
        json.accumulate("title", title)
        json.accumulate("username", username)
        json.accumulate("comment", comment)
        json.accumulate("rate", rating)

        mSubmitComment = AsyncTaskSubmitFeedback()
        mSubmitComment!!.execute()
        val result = mSubmitComment!!.get()
        val code = result[0]

        when(code){
            "200" -> {
                Toast.makeText(context, "Your comment has been saved", Toast.LENGTH_SHORT).show()
                navController!!.popBackStack()
            }
            else -> {
                Toast.makeText(context, "Your comment wasn't saved", Toast.LENGTH_SHORT).show()
            }
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
