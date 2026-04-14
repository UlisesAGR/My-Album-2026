/*
 * Build.kt - Module buildSrc
 * Copyright (c) 2026. All rights reserved
 */
sealed class Build {

    open val isMinifyEnabled = false
    open val isShrinkResources = false
    open val enableUnitTestCoverage = false
    open val isDebuggable = false

    object Debug : Build() {
        override val isMinifyEnabled = false
        override val isShrinkResources = false
        override val isDebuggable = true
        override val enableUnitTestCoverage = true
    }

    object Release : Build() {
        override val isMinifyEnabled = true
        override val isShrinkResources = true
        override val isDebuggable = false
        override val enableUnitTestCoverage = false
    }
}
