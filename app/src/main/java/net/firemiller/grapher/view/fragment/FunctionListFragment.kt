package net.firemiller.grapher.view.fragment

import android.app.ListFragment
import android.os.Bundle
import net.firemiller.grapher.view.activity.MainActivity
import net.firemiller.grapher.view.adapter.FunctionListAdapter

class FunctionListFragment : ListFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    listAdapter = FunctionListAdapter(activity, (activity as MainActivity).grapherCore.functions)

  }
}