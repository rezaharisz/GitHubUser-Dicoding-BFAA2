package com.rezaharis.githubuserapp2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rezaharis.githubuserapp2.adapter.FollowingAdapter
import com.rezaharis.githubuserapp2.databinding.FragmentFollowingBinding
import com.rezaharis.githubuserapp2.data.Following
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val listFollowing = ArrayList<Following>()

    companion object{
        const val ARG_USERNAME = "username"

        private val TAG = FollowingFragment::class.java.simpleName

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        getUserFollowing(username!!)
    }

    private fun getUserFollowing(username:String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}/following"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(TAG, result)
                binding.progressBar.visibility = View.INVISIBLE

                try {
                    val jsonArray = JSONArray(result)
                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val followingUrl = jsonObject.getString("url")
                        getUserDetail(followingUrl)
                    }
                }
                catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                binding.progressBar.visibility = View.VISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserDetail(followingUrl: String){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")

        client.get(followingUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d(TAG, result)

                try {
                    getAdapter()
                    val jsonObject = JSONObject(result)
                    val following = Following()
                    following.login = jsonObject.getString("login")
                    following.avatar_url = jsonObject.getString("avatar_url")
                    following.name = jsonObject.getString("name")
                    following.company = jsonObject.getString("company")
                    listFollowing.add(following)

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAdapter(){
        val listFollowingAdapter = FollowingAdapter(listFollowing)
        binding.rvView.adapter = listFollowingAdapter
        binding.rvView.layoutManager = LinearLayoutManager(activity)
        binding.rvView.setHasFixedSize(true)
    }

}