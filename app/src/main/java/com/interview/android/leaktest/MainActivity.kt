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

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.interview.android.guesscount.R
import kotlinx.android.synthetic.main.activity_main.*

private const val DEFAULT_COUNT = 10

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    editTextCount.setText(DEFAULT_COUNT.toString())
    buttonStart.setOnClickListener {
      startCounting()
    }
  }

  private fun startCounting() {
    val count = editTextCount.text.toString()
    if (count.isBlank()) {
      showCountIsRequiredError()
      return
    }
    hideCountIsRequiredError()

    val intent = Intent(this, CountActivity::class.java).apply {
      putExtra(EXTRA_COUNT, count.toInt())
    }
    startActivity(intent)
  }

  private fun showCountIsRequiredError() {
    layoutTextCount.error = getString(R.string.main_enter_value)
    layoutTextCount.isErrorEnabled = true
  }

  private fun hideCountIsRequiredError() {
    layoutTextCount.error = ""
    layoutTextCount.isErrorEnabled = false
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
