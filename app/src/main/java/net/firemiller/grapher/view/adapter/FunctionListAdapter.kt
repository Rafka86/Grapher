package net.firemiller.grapher.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import net.firemiller.grapher.R
import net.firemiller.grapher.controller.util.Function

class FunctionListAdapter(context: Context, functions: List<Function>) : BaseAdapter() {
  private val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  private val mFunctions = functions

  override fun getCount() = mFunctions.size

  override fun getItemId(position: Int) = mFunctions[position].id

  override fun getItem(position: Int) = mFunctions[position]

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view = convertView ?: mInflater.inflate(R.layout.function_list_item, parent, false)

    val checkBox = view.findViewById(R.id.litem_checkBox) as CheckBox
    checkBox.run {
      isChecked = mFunctions[position].visible
      tag = position
    }

    val editText = view.findViewById(R.id.litem_editText) as EditText
    editText.run {
      setText(mFunctions[position].rawExpression)
      tag = position
    }

    val deleteButton = view.findViewById(R.id.litem_imageButton) as ImageButton
    deleteButton.run {
      tag = position
    }

    return view
  }
}