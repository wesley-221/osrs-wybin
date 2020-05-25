package com.example.osrswybin.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.osrswybin.ui.authentication.LoginActivity
import com.example.osrswybin.R

class HomeFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false);
        val btnAuthentication: Button = root.findViewById(R.id.btnAuthentication)

        btnAuthentication.setOnClickListener {
            val intent = Intent(this@HomeFragment.context, LoginActivity::class.java)
            startActivity(intent);
        }

        return root
    }
}
