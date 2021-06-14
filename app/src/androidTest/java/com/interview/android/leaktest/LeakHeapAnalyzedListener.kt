package com.interview.android.leaktest

import android.util.Log
import leakcanary.OnHeapAnalyzedListener
import shark.HeapAnalysis
import shark.HeapAnalysisFailure
import shark.HeapAnalysisSuccess

class LeakHeapAnalyzedListener : OnHeapAnalyzedListener {

    override fun onHeapAnalyzed(heapAnalysis: HeapAnalysis) {
        handleResult(heapAnalysis)
    }

    private fun handleResult(heapAnalysis: HeapAnalysis) {
        Log.d("TEST", "Start analysis")
        when (heapAnalysis) {
            is HeapAnalysisFailure -> {
                Log.e("TEST", "onHeapAnalyzed:failure info = $heapAnalysis")
            }
            is HeapAnalysisSuccess -> {
                val leakCount = heapAnalysis.allLeaks.count()
                if (leakCount > 0) {
                    finishWithFailure(heapAnalysis.toString())
                } else {
                    Log.d("TEST", "onHeapAnalyzed:success No leaks detected")
                }
            }
        }
    }

    private fun finishWithFailure(leakStackTrace: String) {
        Log.e("TEST","finishing with failure")
        Log.e("TEST", "onHeapAnalyzed: $leakStackTrace")

    }
}
