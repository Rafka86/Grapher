package net.firemiller.grapher.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.firemiller.grapher.R
import net.firemiller.grapher.model.system.GrapherCore

class MainActivity : AppCompatActivity() {
  val gc = GrapherCore()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}