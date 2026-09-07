package com.flatcode.simplemultiapps.multipledelete.adapter

import android.app.Activity
import android.content.Context
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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
import com.flatcode.simplemultiapps.multipledelete.model.MultiDelete
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ItemMultiDeleteBinding
import androidx.core.content.ContextCompat

class MultiDeleteAdapter(
    private val context: Context,
    private val activity: Activity?,
    var arrayList: ArrayList<String>,
    private val tvEmpty: TextView,
) : RecyclerView.Adapter<MultiDeleteAdapter.ViewHolder>() {

    private val mainViewModel: MultiDelete? by lazy {
        (activity as? FragmentActivity)?.let { ViewModelProvider(it)[MultiDelete::class.java] }
    }

    var isEnable = false
    var isSelectAll = false
    val selectList = ArrayList<String>()

    class ViewHolder(val binding: ItemMultiDeleteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultiDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemText = arrayList[position]

        with(holder.binding) {
            text.text = itemText

            if (isSelectAll || selectList.contains(itemText)) {
                checkBox.isVisible = true
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_light))
            } else {
                checkBox.isGone = true
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            }
        }

        holder.itemView.setOnLongClickListener { view ->
            if (!isEnable) {
                val callback = object : ActionMode.Callback {
                    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        actionMode.menuInflater.inflate(R.menu.multi_delete_menu, menu)
                        return true
                    }

                    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                        isEnable = true
                        clickItem(holder)

                        (activity as? LifecycleOwner)?.let { owner ->
                            mainViewModel?.text?.observe(owner) { s ->
                                actionMode.title = context.getString(R.string.selected, s)
                            }
                        }
                        return true
                    }

                    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
                        when (menuItem.itemId) {
                            R.id.menu_delete -> {
                                selectList.forEach { selectedItem ->
                                    val index = arrayList.indexOf(selectedItem)
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
                (view.context as? AppCompatActivity)?.startActionMode(callback)
            } else {
                clickItem(holder)
            }
            true
        }

        holder.itemView.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener

            if (isEnable) {
                clickItem(holder)
            } else {
                Toast.makeText(context, context.getString(R.string.you_clicked, arrayList[currentPos]), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size

    private fun clickItem(holder: ViewHolder) {
        val currentPos = holder.bindingAdapterPosition
        if (currentPos == RecyclerView.NO_POSITION) return

        val itemText = arrayList[currentPos]

        with(holder.binding) {
            if (checkBox.isGone) {
                checkBox.isVisible = true
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_light))
                selectList.add(itemText)
            } else {
                checkBox.isGone = true
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
                selectList.remove(itemText)
            }
        }
        mainViewModel?.setText(selectList.size.toString())
    }
}