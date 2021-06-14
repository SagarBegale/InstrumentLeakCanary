package com.deliveryhero.performance.test.leak

import com.interview.android.leaktest.LeakHeapAnalyzedListener
import leakcanary.FailTestOnLeakRunListener
import leakcanary.LeakCanary
import org.junit.runner.Description

class FailAnnotatedTestLeakListener : FailTestOnLeakRunListener() {

    override fun skipLeakDetectionReason(description: Description): String? {
        // Check if test method is annotated with our custom annotation
        val result = if (description.getAnnotation(CheckMemoryLeaks::class.java) == null) {
            "Test is not annotated with @CheckMemoryLeaks"
        } else null


        if (result == null) {
            // If method is annotated enable LeakCanary for tests
            LeakCanary.config = LeakCanary.config.copy(
                // Allow canary to dump heap
                dumpHeap = true,
                // Provide custom Heap analyser
                onHeapAnalyzedListener = LeakHeapAnalyzedListener()
            )
        }

        return result
    }
}
//    override fun onAnalysisPerformed(heapAnalysis: HeapAnalysis) {
//        when (heapAnalysis) {
//            is HeapAnalysisFailure -> super.onAnalysisPerformed(heapAnalysis)
//            is HeapAnalysisSuccess -> {
//                val analysis = heapAnalysis.suppressKnownLeaks()
//                writeAnalysesReport(analysis)
//                super.onAnalysisPerformed(analysis)
//            }
//        }
//    }
//
//    private fun writeAnalysesReport(analysisSuccess: HeapAnalysisSuccess) {
//        PerformanceReportWriter.writeTextReport(
//            fileName = "final-test-leak-report",
//            reportType = ReportType.MEMORY_LEAK,
//            report = analysisSuccess.toString()
//        )
//    }
//}
