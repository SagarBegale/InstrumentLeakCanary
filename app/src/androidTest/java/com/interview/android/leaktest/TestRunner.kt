package com.interview.android.leaktest

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.deliveryhero.performance.test.leak.FailAnnotatedTestLeakListener

@Suppress("unused")
open class TestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        val listeners = listOfNotNull(
            arguments.getCharSequence("listener"),
            FailAnnotatedTestLeakListener::class.qualifiedName,
        ).joinToString(",")

        arguments.putCharSequence("listener", listeners)
        super.onCreate(arguments)
    }
}

//    @Throws(
//        InstantiationException::class,
//        IllegalAccessException::class,
//        ClassNotFoundException::class
//    )
//    override fun newApplication(
//        cl: ClassLoader,
//        className: String,
//        context: Context
//    ): Application {
//
//        return super.newApplication(cl, TestPandoraApplication::class.java.getName(), context)
//    }
//}
