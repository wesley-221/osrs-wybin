package com.example.osrswybin.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.osrswybin.R
import com.example.osrswybin.database.account.AccountRepository
import com.example.osrswybin.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_track_new_user.*
import kotlinx.android.synthetic.main.content_logged_in.view.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.content_login.view.*
import kotlinx.android.synthetic.main.content_login.view.tbUsername
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var authToken: String? = null
    private var username: String? = null
    private lateinit var root: View
    private lateinit var accountRepository: AccountRepository

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        authToken = Credentials.getAccessToken(requireActivity())
        accountRepository = AccountRepository(requireActivity())

        if(authToken == null) {
            root = inflater.inflate(R.layout.content_login, container, false)

            // Signup button
            root.btnSignUp.setOnClickListener {
                val intent = Intent(this@HomeFragment.context, RegisterActivity::class.java)
                startActivity(intent)
            }

            root.btnLogin.setOnClickListener {
                if(root.tbUsername.text.isBlank() || root.tbPassword.text.isBlank()) {
                    Toast.makeText(this@HomeFragment.context, "You have to fill in a username and password.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Authentication.login(root.tbUsername.text.toString(), root.tbPassword.text.toString(), object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            // Invalid login
                            if(response.code == 401) {
                                mainScope.launch {
                                    Toast.makeText(this@HomeFragment.context, "Invalid username or password provided.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else if(response.isSuccessful) {
                                mainScope.launch {
                                    val authenticationToken = response.headers["Authorization"]

                                    Credentials.saveCredentials(requireContext(), authenticationToken, tbUsername.text.toString())

                                    Authentication.getTrackedUsers(authenticationToken.toString(), object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            e.printStackTrace()
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            if(response.isSuccessful) {
                                                val responseBody = response.body
                                                val content = Gson().fromJson(responseBody?.string(), ArrayList::class.java)

                                                for(username in content) {
                                                    Hiscores.getHiscoresFromUser(username as String, object : Callback {
                                                        override fun onFailure(call: Call, e: IOException) {
                                                            e.printStackTrace()
                                                        }

                                                        override fun onResponse(call: Call,response: Response) {
                                                            if(response.isSuccessful) {
                                                                val hiscoreResponseBody = response.body
                                                                val hiscoreContent = hiscoreResponseBody?.string()

                                                                val skills: ArrayList<Skill> = Hiscores.getSkillsFromResponse(hiscoreContent)
                                                                val activities: ArrayList<Activity> = Hiscores.getActivitiesFromResponse(hiscoreContent)

                                                                val osrsAccount = OSRSAccount(0, username, skills, activities, Calendar.getInstance().time)

                                                                mainScope.launch {
                                                                    withContext(Dispatchers.IO) {
                                                                        accountRepository.insertAccount(osrsAccount)
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    })

                                    Toast.makeText(this@HomeFragment.context, "Successfully logged in, welcome ${root.tbUsername.text}!", Toast.LENGTH_SHORT).show()
                                    reloadFragment()
                                }
                            }
                        }
                    })
                }
            }
        }
        else {
            root = inflater.inflate(R.layout.content_logged_in, container, false)

            username = Credentials.getUsername(requireContext())
            root.tvLoggedInAs.text = getString(R.string.logged_in_as, username)

            root.btnLogout.setOnClickListener {
                Credentials.logOut(requireContext())
                Toast.makeText(this@HomeFragment.context, "Successfully logged out!", Toast.LENGTH_SHORT).show()

                reloadFragment()
            }
        }

        return root
    }

    fun reloadFragment() {
        val ft: FragmentManager = FragmentActivity().supportFragmentManager
        val fragment = ft.findFragmentByTag("test_tag")
        ft.beginTransaction()

        fragment?.let {
            ft.beginTransaction().detach(it).commitNowAllowingStateLoss()
            ft.beginTransaction().attach(it).commitNowAllowingStateLoss()
        }
    }
}
