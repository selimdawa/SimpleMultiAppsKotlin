package com.flatcode.simplemultiapps.Wordpress.Sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.flatcode.simplemultiapps.Wordpress.Model.Post

class PostDB private constructor(context: Context) {

    object PostItem : BaseColumns {
        const val TABLE_NAME = "post"
        const val COLNAME_POSTID = "postID"
        const val COLNAME_TITLE = "title"
        const val COLNAME_EXCERPT = "excerpt"
        const val COLNAME_ISFAV = "isFavorite"
    }

    private val dbHelper = TodoItemDbHelper(context.applicationContext)

    inner class TodoItemDbHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }

    val allDbPosts: List<Post>
        get() {
            val postList = ArrayList<Post>()
            val db = dbHelper.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                PostItem.COLNAME_POSTID,
                PostItem.COLNAME_TITLE,
                PostItem.COLNAME_EXCERPT,
                PostItem.COLNAME_ISFAV
            )

            db.query(PostItem.TABLE_NAME, projection, null, null, null, null, null).use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID)
                val wpIdIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_POSTID)
                val titleIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_TITLE)
                val excerptIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_EXCERPT)
                val isFavIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_ISFAV)

                while (cursor.moveToNext()) {
                    val tmpPost = Post(
                        cursor.getInt(idIndex),
                        cursor.getInt(wpIdIndex),
                        cursor.getString(titleIndex),
                        cursor.getString(excerptIndex),
                        cursor.getInt(isFavIndex)
                    )
                    postList.add(tmpPost)
                }
            }
            return postList
        }

    fun getDbPostIsFav(postID: Int): Boolean {
        val db = dbHelper.readableDatabase
        var isFavorite = false

        val projection = arrayOf(PostItem.COLNAME_TITLE, PostItem.COLNAME_ISFAV)
        val selection = "${PostItem.COLNAME_POSTID} = ?"
        val selectionArgs = arrayOf(postID.toString())

        db.query(PostItem.TABLE_NAME, projection, selection, selectionArgs, null, null, null).use { cursor ->
            if (cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_TITLE)
                val isFavIndex = cursor.getColumnIndexOrThrow(PostItem.COLNAME_ISFAV)

                val title = cursor.getString(titleIndex)
                isFavorite = cursor.getInt(isFavIndex) == 1

                Log.d("SelectedItem", title.orEmpty())
            }
        }
        return isFavorite
    }

    fun insert(wpPostID: Int, wpTitle: String?, wpExcerpt: String?, isFavorite: Boolean): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PostItem.COLNAME_POSTID, wpPostID)
            put(PostItem.COLNAME_TITLE, wpTitle)
            put(PostItem.COLNAME_EXCERPT, wpExcerpt)
            put(PostItem.COLNAME_ISFAV, if (isFavorite) 1 else 0)
        }
        return db.insert(PostItem.TABLE_NAME, null, values)
    }

    fun update(post: Post): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PostItem.COLNAME_TITLE, post.wpTitle)
            put(PostItem.COLNAME_EXCERPT, post.wpExcerpt)
            put(PostItem.COLNAME_ISFAV, post.isFavorite)
        }

        val whereClause = "${BaseColumns._ID} = ?"
        val whereArgs = arrayOf(post.id.toString())
        return db.update(PostItem.TABLE_NAME, values, whereClause, whereArgs)
    }

    fun delete(postID: Int): Int {
        val db = dbHelper.writableDatabase
        val whereClause = "${PostItem.COLNAME_POSTID} = ?"
        val whereArgs = arrayOf(postID.toString())
        return db.delete(PostItem.TABLE_NAME, whereClause, whereArgs)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Post.db"

        @Volatile
        private var myInstance: PostDB? = null

        fun getInstance(context: Context?): PostDB? {
            if (context == null) return null
            return myInstance ?: synchronized(this) {
                myInstance ?: PostDB(context).also { myInstance = it }
            }
        }

        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${PostItem.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${PostItem.COLNAME_POSTID} INTEGER," +
                "${PostItem.COLNAME_TITLE} TEXT," +
                "${PostItem.COLNAME_EXCERPT} TEXT," +
                "${PostItem.COLNAME_ISFAV} INTEGER DEFAULT 0)"
    }
}