package com.example.beta.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.CoursePhotosRecyclerAdapter
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.converter.ListString
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.view_model.CoursesViewModel
import com.google.gson.Gson
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.beta.adapters.CommentsRecyclerAdapter
import com.example.beta.data.CommentsData
import com.example.beta.database.dao.CategoriesDao
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.ConverterForUI
import com.example.beta.others.HttpRequest
import kotlinx.android.synthetic.main.fragment_course.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class CourseFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel

    lateinit var listFromServ: ArrayList<CommentsData>

    private var district: JSONObject? = null
    private var method: String? = null
    private var url: String? = null

    private var mCommentsTask: AsyncTaskComments? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var coursentity:CoursesEnt? = null

        viewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        val course = arguments?.getString("course")!!
        val location = arguments?.getString("location")!!
        val difficulty = arguments?.getInt("difficulty")!!
        val category = arguments?.getString("category")!!
        val description = arguments?.getString("description")!!
        val waypoints = arguments?.getString("waypoints")!!
        val start = arguments?.getString("start")!!
        val finish = arguments?.getString("finish")!!
        val wMarkers = arguments?.getStringArrayList("markers")!!
        val wDescriptions = arguments?.getStringArrayList("descriptions")!!

        /*
        viewModel.getCourse(course)

        viewModel.singleCourse.observe(this, Observer {

            coursentity = it
        })*/

        fragment_course_name.text = course
        fragment_course_difficulty.text = "Difficulty: $difficulty"
        fragment_course_catefory1.text = category
        fragment_course_location.text = "Location: $location"
        fragment_course_description.text = description
        fragment_courses_activities.setOnClickListener {

            val bundle = Bundle()
            bundle.putStringArrayList("descriptions", wDescriptions)
            Navigation.findNavController(view).navigate(R.id.action_courseFragment_to_locationsListFragment, bundle)
        }

        fragment_course_button_course.setOnClickListener {

            val bundle = Bundle()
            bundle.putBoolean("course", true)
            bundle.putStringArrayList("waypoints", wMarkers)
            bundle.putString("finish", finish)
            bundle.putString("start", start)
            Navigation.findNavController(view).navigate(R.id.action_courseFragment_to_mapMenuFragment, bundle)
        }
        fragment_course_button_iniciate.setOnClickListener {

            val bundle = Bundle()
            bundle.putBoolean("initiate", true)
            bundle.putStringArrayList("waypoints", wMarkers)
            bundle.putString("finish", finish)
            bundle.putString("start", start)
            bundle.putString("title", course)
            Navigation.findNavController(view).navigate(R.id.action_courseFragment_to_mapMenuFragment, bundle)
        }
        fragment_course_go_comments.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("title", course)
            Navigation.findNavController(view).navigate(R.id.action_courseFragment_to_commentsFragmment, bundle)
        }

        district = JSONObject()
        district!!.accumulate("title", course)

        listFromServ = getComments()

        val adapter = CommentsRecyclerAdapter(context!!)
        adapter.setListComments(listFromServ)

        fragment_course_comments.layoutManager = LinearLayoutManager(context!!)
        fragment_course_comments.adapter = adapter
    }

    fun getComments(): ArrayList<CommentsData>{

        url = "https://turisnova.appspot.com/rest/comments/getComments"
        method = "POST"

        val list = arrayListOf<CommentsData>()

        mCommentsTask = AsyncTaskComments()
        mCommentsTask!!.execute()
        val result = mCommentsTask!!.get()
        val code = result[0]

        when(code){
            "200" -> {
                val comments = JSONArray(result[1])
                for(i in 0 until comments.length()) {

                    val propMap = comments.getJSONObject(i).getJSONObject("propertyMap")
                    val username = propMap.getString("comment_username")
                    val rate = propMap.getInt("comment_rate")
                    val comment = propMap.getString("comment_comment")

                    list.add(CommentsData(username, rate, comment))
                }
            }
            else -> {}
        }

        return list
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskComments internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {
            if (false) {
                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {
            return HttpRequest().doHTTP(URL(url), district!!, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mCommentsTask = null
        }

        override fun onCancelled() {
            mCommentsTask = null
        }
    }
}