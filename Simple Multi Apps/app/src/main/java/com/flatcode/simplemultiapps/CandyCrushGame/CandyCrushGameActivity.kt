package com.flatcode.simplemultiapps.CandyCrushGame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityCandyCrashGameBinding
import java.util.Arrays
import kotlin.math.floor

class CandyCrushGameActivity : AppCompatActivity() {

    private var binding: ActivityCandyCrashGameBinding? = null
    private val context: Context = this@CandyCrushGameActivity
    var candies = intArrayOf(
        R.drawable.bluecandy, R.drawable.greencandy, R.drawable.redcandy,
        R.drawable.orangecandy, R.drawable.yellowcandy, R.drawable.purplecandy
    )
    var widthOfBlock = 0
    var noOfBlocks = 8
    var widthOfScreen = 0
    var candy = ArrayList<ImageView>()
    var candyToBeDragged = 0
    var candyToBeReplaced = 0
    var notCandy = R.drawable.transparent
    var mHandler = Handler()
    var interval = 100
    var score = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityCandyCrashGameBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = getString(R.string.candy_crush_game)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthOfScreen = displayMetrics.widthPixels
        val hightOfScreen = displayMetrics.heightPixels
        widthOfBlock = widthOfScreen / noOfBlocks

        createBoard()
        for (imageView in candy) {
            imageView.setOnTouchListener(object : OnSwipeListener(context) {
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - 1
                    candyInterChange()
                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + 1
                    candyInterChange()
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlocks
                    candyInterChange()
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlocks
                    candyInterChange()
                }
            })
        }
        mHandler = Handler()
        startRepeat()
    }

    private fun checkRowForThree() {
        for (i in 0..61) {
            val chooseCandy = candy[i].tag as Int
            val isBlank = candy[i].tag as Int == notCandy
            val notValid = arrayOf(6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55)
            val list = listOf(*notValid)
            if (!list.contains(i)) {
                var x = i
                if (candy[x++].tag as Int == chooseCandy && !isBlank && candy[x++].tag as Int == chooseCandy && candy[x].tag as Int == chooseCandy) {
                    score += 3
                    binding!!.toolbarScore.scoreList.text = score.toString()
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                    x--
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                    x--
                    candy[x].setImageResource(notCandy)
                    candy[x].tag = notCandy
                }
            }
        }
        moveDownCandies()
    }

    private fun checkColumnForThree() {
        for (i in 0..46) {
            val choosedCandy = candy[i].tag as Int
            val isBlank = candy[i].tag as Int == notCandy
            var x = i
            if (candy[x].tag as Int == choosedCandy && !isBlank && candy[x + noOfBlocks].tag as Int == choosedCandy && candy[x + 2 * noOfBlocks].tag as Int == choosedCandy) {
                score += 3
                binding!!.toolbarScore.scoreList.text = score.toString()
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
                x += noOfBlocks
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
                x += noOfBlocks
                candy[x].setImageResource(notCandy)
                candy[x].tag = notCandy
            }
        }
        moveDownCandies()
    }

    private fun moveDownCandies() {
        val firstRow = arrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        val list = Arrays.asList(*firstRow)
        for (i in 55 downTo 0) {
            if (candy[i + noOfBlocks].tag as Int == notCandy) {
                candy[i + noOfBlocks].setImageResource(candy[i].tag as Int)
                candy[i + noOfBlocks].tag = candy[i].tag
                candy[i].setImageResource(notCandy)
                candy[i].tag = notCandy
                if (list.contains(i) && candy[i].tag as Int == notCandy) {
                    val randomColor = floor(Math.random() * candies.size).toInt()
                    candy[i].setImageResource(candies[randomColor])
                    candy[i].tag = candies[randomColor]
                }
            }
        }
        for (i in 0..-1) {
            if (candy[i].tag as Int == notCandy) {
                val randomColor = floor(Math.random() * candies.size).toInt()
                candy[i].setImageResource(candies[randomColor])
                candy[i].tag = candies[randomColor]
            }
        }
    }

    var repeatChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownCandies()
            } finally {
                mHandler.postDelayed(this, interval.toLong())
            }
        }
    }

    fun startRepeat() {
        repeatChecker.run()
    }

    private fun candyInterChange() {
        val background = candy[candyToBeReplaced].tag as Int
        val background2 = candy[candyToBeDragged].tag as Int
        candy[candyToBeDragged].setImageResource(background)
        candy[candyToBeReplaced].setImageResource(background2)
        candy[candyToBeDragged].tag = background
        candy[candyToBeReplaced].tag = background2
    }

    private fun createBoard() {
        binding!!.board.rowCount = noOfBlocks
        binding!!.board.columnCount = noOfBlocks
        binding!!.board.layoutParams.width = widthOfScreen
        binding!!.board.layoutParams.height = widthOfScreen
        for (i in 0 until noOfBlocks * noOfBlocks) {
            val imageView = ImageView(context)
            imageView.id = i
            imageView.layoutParams = ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)
            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock
            val randomCandy = floor(Math.random() * candies.size).toInt()
            imageView.setImageResource(candies[randomCandy])
            imageView.tag = candies[randomCandy]
            candy.add(imageView)
            binding!!.board.addView(imageView)
        }
    }
}