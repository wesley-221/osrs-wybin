package com.example.osrswybin.ui.tracking

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osrswybin.R
import com.example.osrswybin.adapters.OSRSAccountAdapter
import com.example.osrswybin.database.AccountRepository
import com.example.osrswybin.models.OSRSAccount
import kotlinx.android.synthetic.main.fragment_tracking.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TRACK_NEW_USER = 100

class TrackingFragment : Fragment() {
    private val osrsAccounts = arrayListOf<OSRSAccount>()
    private val osrsAccountsAdapter = OSRSAccountAdapter(osrsAccounts)

    private lateinit var accountRepository: AccountRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tracking, container, false)

        // Setup adapter
        root.rvOSRSAccounts.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        root.rvOSRSAccounts.adapter = osrsAccountsAdapter

        accountRepository = AccountRepository(this.requireContext())

        // Add itemTouchHelper
        createItemTouchHelper().attachToRecyclerView(root.rvOSRSAccounts)

        // Display all the accounts
        mainScope.launch {
            val accountList = withContext(Dispatchers.IO) {
                accountRepository.getAllAccounts()
            }

            osrsAccounts.clear()
            osrsAccounts.addAll(accountList)
            osrsAccountsAdapter.notifyDataSetChanged()
        }

        root.btnTracknewUser.setOnClickListener {
            val intent = Intent(this@TrackingFragment.context, TrackNewUserActivity::class.java)
            startActivityForResult(intent, TRACK_NEW_USER);
        }

        return root
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val selectedItem = osrsAccounts[position]

                AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage("Are you sure you want to stop tracking " + selectedItem.username + "?")
                    .setPositiveButton("Yes") { _, _ ->
                        mainScope.launch {
                            withContext(Dispatchers.IO) {
                                accountRepository.deleteAccount(osrsAccounts[position])
                            }

                            osrsAccounts.removeAt(position);
                            osrsAccountsAdapter.notifyDataSetChanged()
                        }
                    }
                    .setNegativeButton("No") { _, _ -> osrsAccountsAdapter.notifyItemChanged(position) }
                    .create()
                    .show()
            }
        }

        return ItemTouchHelper(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TRACK_NEW_USER -> {

                }
            }
        }
    }
}
