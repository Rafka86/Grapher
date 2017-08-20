package net.firemiller.grapher.view.activity

import android.app.Activity
import android.os.Bundle
import net.firemiller.grapher.R
import net.firemiller.grapher.model.system.GrapherCore
import net.firemiller.grapher.view.widget.GraphSheet

class MainActivity : Activity() {
  val grapherCore = GrapherCore()
  private lateinit var mGrapheSheet: GraphSheet

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mGrapheSheet = findViewById(R.id.graphSheet) as GraphSheet
    mGrapheSheet.grapherCore = grapherCore
    grapherCore.addObserver(mGrapheSheet)
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    grapherCore.setViewSize(mGrapheSheet.width, mGrapheSheet.height)
  }
}