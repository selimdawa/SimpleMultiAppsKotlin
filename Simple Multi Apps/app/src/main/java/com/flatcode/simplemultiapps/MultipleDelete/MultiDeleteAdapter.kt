package com.flatcode.simplemultiapps.MultipleDelete

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ItemMultiDeleteBinding

//Create Constructor
class MultiDeleteAdapter(
    private val context: Context, var activity: Activity?,
    var arrayList: ArrayList<String>, var tvEmpty: TextView,
) : RecyclerView.Adapter<MultiDeleteAdapter.ViewHolder>() {

    private var binding: ItemMultiDeleteBinding? = null
    var mainViewModel: MultiDelete? = null
    var isEnable = false
    var isSelectAll = false
    var selectList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Initialize view
        binding = ItemMultiDeleteBinding.inflate(LayoutInflater.from(context), parent, false)
        //Initialize view model
        mainViewModel =
            ViewModelProviders.of((activity as FragmentActivity?)!!)[MultiDelete::class.java]
        //Return view
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Set text on text view
        holder.textView.text = arrayList[position]
        holder.itemView.setOnLongClickListener { v: View ->
            //Check condition
            if (!isEnable) {
                //When action mode is not enable
                //Initialize action mode
                val callback: ActionMode.Callback = object : ActionMode.Callback {
                    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        //Initialize menu inflate
                        val menuInflater = actionMode.menuInflater
                        //Inflate menu
                        menuInflater.inflate(R.menu.multi_delete_menu, menu)
                        //Return true
                        return true
                    }

                    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        //When action mode is prepare
                        //Set isEnable true
                        isEnable = true
                        //Create method
                        ClickItem(holder)

                        //Set observer on get text method
                        mainViewModel!!.text.observe((activity as LifecycleOwner?)!!) { s ->
                            //When text change
                            //Set text on action mode title
                            actionMode.title = java.lang.String.format("%s Selected", s)
                        }
                        //Return true
                        return true
                    }

                    override fun onActionItemClicked(
                        actionMode: ActionMode, menuItem: MenuItem,
                    ): Boolean {
                        //When click on action mode item
                        //Get item id
                        val id = menuItem.itemId
                        when (id) {
                            R.id.menu_delete -> {
                                //When click on delete
                                //User for loop
                                for (s in selectList) {
                                    //Remove selected item from array list
                                    arrayList.remove(s)
                                }
                                //Check condition
                                if (arrayList.size == 0) {
                                    //When array list is empty
                                    //Visible text view
                                    tvEmpty.visibility = View.VISIBLE
                                }
                                //Finish action mode
                                actionMode.finish()
                            }

                            R.id.menu_select_all -> {
                                //When click on select all
                                //Check condition
                                if (selectList.size == arrayList.size) {
                                    //When all item selected
                                    //Set isSelectedAll false
                                    isSelectAll = false
                                    //Clear select array list
                                    selectList.clear()
                                } else {
                                    //When all item unselected
                                    //Set isSelectedAll true
                                    isSelectAll = true
                                    //Clear select array list
                                    selectList.clear()
                                    //Add all value in select array list
                                    selectList.addAll(arrayList)
                                }
                                //Set text on view model
                                mainViewModel!!.setText(selectList.size.toString())
                                //Notify adapter
                                notifyDataSetChanged()
                            }
                        }
                        //Return true
                        return true
                    }

                    override fun onDestroyActionMode(actionMode: ActionMode) {
                        //When action mode is destroy
                        //Set isEnable false
                        isEnable = false
                        //Set isSelectAll false
                        isSelectAll = false
                        //Clear select array list
                        selectList.clear()
                        //Notify adapter
                        notifyDataSetChanged()
                    }
                }
                //Start action mode
                (v.context as AppCompatActivity).startActionMode(callback)
            } else {
                //When action mode is already enable
                //Call method
                ClickItem(holder)
            }
            true
        }
        holder.itemView.setOnClickListener {
            //Check condition
            if (isEnable) {
                //When action mode is enable
                //Call method
                ClickItem(holder)
            } else {
                //When action mode is not enable
                //Display Toast
                Toast.makeText(
                    activity, "You Clicked " + arrayList[holder.adapterPosition], Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Check condition
        if (isSelectAll) {
            //When all value selected
            //Visible all check box image
            holder.ivCheckBox.visibility = View.VISIBLE
            //Set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            //When all value unselected
            //hide all check box image
            holder.ivCheckBox.visibility = View.GONE
            //Set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun ClickItem(holder: ViewHolder) {
        //Get selected item value
        val s = arrayList[holder.adapterPosition]
        //Check condition
        if (holder.ivCheckBox.visibility == View.GONE) {
            //When item not selected
            //Visible check box image
            holder.ivCheckBox.visibility = View.VISIBLE
            //Set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            //Add value in select array list
            selectList.add(s)
        } else {
            //When item selected
            //Hide check box image
            holder.ivCheckBox.visibility = View.GONE
            //Set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            //Remove value in select array list
            selectList.remove(s)
        }
        //Set text on view model
        mainViewModel!!.setText(selectList.size.toString())
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var ivCheckBox: ImageView

        init {
            textView = binding!!.text
            ivCheckBox = binding!!.checkBox
        }
    }
}