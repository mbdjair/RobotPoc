package com.djair.robotpoc.rules

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

class KoinRule<T : Activity>(
    private val activityClass: Class<T>
) : TestRule, KoinTest {

    private val ruleChain = RuleChain.emptyRuleChain()

    private var setupBeforeActivityOpen: () -> Unit = {}
    private var setupBundleActivity: Bundle = Bundle()
    private var activityScenario: ActivityScenario<T>? = null

    override fun apply(base: Statement?, description: Description?): Statement =
        ruleChain.around(StartKoinRule())
            .apply(base, description)

    fun setupModule(vararg module: Module) {
        val modules = mutableListOf<Module>()
        for (item in module) {
            modules.add(item)
        }

        loadKoinModules(modules)
    }

    fun beforeActivityLaunch(block: () -> Unit) = apply {
        setupBeforeActivityOpen = block
    }

    fun putBundles(block: Bundle.() -> Unit) = apply {
        block(setupBundleActivity)
    }

    fun launchActivity() {
        Intent(ApplicationProvider.getApplicationContext(), activityClass)
            .putExtras(setupBundleActivity)
            .also { activityScenario = ActivityScenario.launch(it) }
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
