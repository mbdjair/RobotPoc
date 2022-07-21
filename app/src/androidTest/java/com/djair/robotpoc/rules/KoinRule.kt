package com.djair.robotpoc.rules

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import java.lang.reflect.Modifier

@VisibleForTesting(otherwise = Modifier.PRIVATE)
class KoinRule<T : Activity>(
    private val activityClass: Class<T>
) : TestRule, KoinTest {

    private val ruleChain = RuleChain.emptyRuleChain()

    private var setupPutArgumentsActivity: Bundle = Bundle()
    private var activityScenario: ActivityScenario<T>? = null

    override fun apply(base: Statement?, description: Description?): Statement =
        ruleChain.around(StartKoinRule())
            .apply(base, description)

    fun setModules(blockModule: () -> Module) =
        apply { loadKoinModules(blockModule()) }

    fun putArguments(blockBundle: Bundle.() -> Unit) =
        apply { blockBundle(setupPutArgumentsActivity) }

    fun launchActivity(blockActivity: T.() -> Unit = {}) {
        Intent(ApplicationProvider.getApplicationContext(), activityClass)
            .putExtras(setupPutArgumentsActivity)
            .also {
                activityScenario = ActivityScenario.launch(it)
                activityScenario?.onActivity { activity -> blockActivity(activity) }
            }
    }

    private inner class StartKoinRule : ExternalResource() {

        override fun before() {
            super.before()
            stopKoin()
            startKoin { androidContext(ApplicationProvider.getApplicationContext()) }
        }

        override fun after() {
            activityScenario?.close()
            super.after()
            stopKoin()
        }
    }
}
