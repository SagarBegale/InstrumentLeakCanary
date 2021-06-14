/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.interview.android.leaktest

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.interview.android.guesscount.R
import kotlinx.android.synthetic.main.activity_count.*

const val EXTRA_COUNT = "EXTRA_COUNT"

class CountActivity : AppCompatActivity() {

  private val userCount: Int by lazy {
    intent.getIntExtra(EXTRA_COUNT, 0)
  }
  private var startTimestamp = 0L
  private var stopTimestamp = 0L
  private val timeoutHandler = Handler()
  private val timeoutRunnable = object : Runnable {
    override fun run() {
      this@CountActivity.stopCounting()
      this@CountActivity.showTimeoutResults()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_count)
    setFinishOnTouchOutside(false)

    buttonGuess.setOnClickListener {
      stopCounting()
      showDifferenceResults()
    }

    prepareToStartCounting()
    startCounting()
  }

  private fun startCounting() {
    startTimestamp = System.currentTimeMillis()
    val timeoutMillis = userCount * 1000 + 10000L
    timeoutHandler.postDelayed(timeoutRunnable, timeoutMillis)
  }

  private fun stopCounting() {
    stopTimestamp = System.currentTimeMillis()
    timeoutHandler.removeCallbacksAndMessages(null)
  }

  private fun showDifferenceResults() {
    prepareToShowResults()

    val difference = (stopTimestamp - startTimestamp) / 1000
    val diffWithCount = Math.abs(userCount - difference.toInt())
    when {
      diffWithCount > 5 -> textViewGuessTitle.text = getString(R.string.count_result_bad)
      diffWithCount == 0 -> textViewGuessTitle.text = getString(R.string.count_result_excellent)
      else -> textViewGuessTitle.text = getString(R.string.count_result_good)
    }
    textViewGuessSubtitle.text = getString(R.string.count_result_difference, diffWithCount)
  }

  private fun showTimeoutResults() {
    prepareToShowResults()

    textViewGuessTitle.text = getString(R.string.count_result_timeout)
    textViewGuessSubtitle.text = getString(R.string.count_result_timeout_difference, userCount)
  }

  private fun prepareToShowResults() {
    textViewGuessTitle.visibility = View.VISIBLE
    textViewGuessSubtitle.visibility = View.VISIBLE
    buttonGuess.visibility = View.GONE
    progressBar.visibility = View.GONE
  }

  private fun prepareToStartCounting() {
    textViewGuessTitle.visibility = View.GONE
    textViewGuessSubtitle.visibility = View.GONE
    buttonGuess.visibility = View.VISIBLE
    progressBar.visibility = View.VISIBLE
  }

  fun startAsyncWork() {
    // This runnable is an anonymous class and therefore has a hidden reference to the outer
    // class MainActivity. If the activity gets destroyed before the thread finishes (e.g. rotation),
    // the activity instance will leak.
    val work = Runnable { // Do some slow work in background
      SystemClock.sleep(20000)
    }
    Thread(work).start()
  }
}
