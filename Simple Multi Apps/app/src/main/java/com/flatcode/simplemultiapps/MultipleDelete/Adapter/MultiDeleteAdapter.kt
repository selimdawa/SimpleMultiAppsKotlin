package com.flatcode.simplemultiapps.MultipleDelete.Adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.MultipleDelete.Model.MultiDelete
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ItemMultiDeleteBinding

class MultiDeleteAdapter(
    private val context: Context,
    var activity: Activity?,
    var arrayList: ArrayList<String>,
    var tvEmpty: TextView,
) : RecyclerView.Adapter<MultiDeleteAdapter.ViewHolder>() {

    var mainViewModel: MultiDelete? = null
    var isEnable = false
    var isSelectAll = false
    var selectList = ArrayList<String>()

    class ViewHolder(val binding: ItemMultiDeleteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultiDeleteBinding.inflate(LayoutInflater.from(context), parent, false)
        val fragmentActivity = activity as? FragmentActivity
        if (fragmentActivity != null) {
            mainViewModel = ViewModelProvider(fragmentActivity)[MultiDelete::class.java]
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.text.text = arrayList[position]

        holder.itemView.setOnLongClickListener { v: View ->
            if (!isEnable) {
                val callback: ActionMode.Callback = object : ActionMode.Callback {
                    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        actionMode.menuInflater.inflate(R.menu.multi_delete_menu, menu)
                        return true
                    }

                    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        isEnable = true
                        clickItem(holder)
                        val lifecycleOwner = activity as? LifecycleOwner
                        lifecycleOwner?.let { owner ->
                            mainViewModel?.text?.observe(owner) { s ->
                                actionMode.title = java.lang.String.format("%s Selected", s)
                            }
                        }
                        return true
                    }

                    override fun onActionItemClicked(
                        actionMode: ActionMode,
                        menuItem: MenuItem
                    ): Boolean {
                        when (menuItem.itemId) {
                            R.id.menu_delete -> {
                                val iterator = selectList.iterator()
                                while (iterator.hasNext()) {
                                    val s = iterator.next()
                                    val index = arrayList.indexOf(s)
                                    if (index != -1) {
                                        arrayList.removeAt(index)
                                        notifyItemRemoved(index)
                                    }
                                }
                                selectList.clear()
                                if (arrayList.isEmpty()) {
                                    tvEmpty.isVisible = true
                                }
                                actionMode.finish()
                            }

                            R.id.menu_select_all -> {
                                if (selectList.size == arrayList.size) {
                                    isSelectAll = false
                                    selectList.clear()
                                } else {
                                    isSelectAll = true
                                    selectList.clear()
                                    selectList.addAll(arrayList)
                                }
                                mainViewModel?.setText(selectList.size.toString())
                                notifyItemRangeChanged(0, arrayList.size)
                            }
                        }
                        return true
                    }

                    override fun onDestroyActionMode(actionMode: ActionMode) {
                        isEnable = false
                        isSelectAll = false
                        selectList.clear()
                        notifyItemRangeChanged(0, arrayList.size)
                    }
                }
                (v.context as AppCompatActivity).startActionMode(callback)
            } else {
                clickItem(holder)
            }
            true
        }

        holder.itemView.setOnClickListener {
            if (isEnable) {
                clickItem(holder)
            } else {
                Toast.makeText(
                    activity, "You Clicked " + arrayList[holder.bindingAdapterPosition], Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (isSelectAll) {
            binding.checkBox.isVisible = true
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            binding.checkBox.isGone = true
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    private fun clickItem(holder: ViewHolder) {
        val s = arrayList[holder.bindingAdapterPosition]
        val binding = holder.binding

        if (binding.checkBox.isGone) {
            binding.checkBox.isVisible = true
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            selectList.add(s)
        } else {
            binding.checkBox.isGone = true
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            selectList.remove(s)
        }
        mainViewModel?.setText(selectList.size.toString())
    }
}