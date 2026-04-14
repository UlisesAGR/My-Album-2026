// ktlint.gradle.kts
// Copyright (c) 2026. All rights reserved
@file:Suppress("DEPRECATION")

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.49.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val ktlinCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        listOf(
            "--disabled_rules=no-wildcard-imports," +
                    "import-ordering,trailing-comma-on-call-site," +
                    "trailing-comma-on-declaration-site," +
                    "trailing-comma-on-call-site," +
                    "no-empty-first-line-in-class-body," +
                    "no-empty-first-line-in-method-block," +
                    "argument-list-wrapping," +
                    "multiline-if-else," +
                    "spacing-between-declarations-with-comments," +
                    "indent," +
                    "annotation," +
                    "annotation-spacing," +
                    "modifier-list-spacing," +
                    "spacing-between-declarations-with-annotations," +
                    "wrapping," +
                    "comment-wrapping," +
                    "spacing-around-angle-brackets," +
                    "no-unused-imports," +
                    "no-trailing-spaces," +
                    "class-naming," +
                    "package-name," +
                    "paren-spacing," +
                    "function-signature," +
                    "block-comment-initial-star-alignment," +
                    "chain-wrapping," +
                    "no-blank-line-before-rbrace," +
                    "chain-wrapping," +
                    "no-consecutive-blank-lines," +
                    "enum-entry-name-case," +
                    "filename," +
                    "function-return-type-spacing",
            "**/src/**/*.kt",
            "**.kts",
            "!**/build/**",
            "--reporter=plain",
            "--color",
            "--reporter=checkstyle,output=$buildDir/reports/ktlint/ktlint-result.xml",
        ),
    )
    inputs.files(
        fileTree(mapOf("dir" to "src", "include" to "**/*.kt", "exclude" to "**/build/**")),
    )
    outputs.file(file("$buildDir/reports/ktlint-result.xml"))

    doLast {
        val outputFile = file("${project.buildDir}/reports/ktlint/ktlint-result.xml")

        if (outputFile.exists()) {
            println("Ktlint check completed. View detailed XML report at: $buildDir/reports/ktlint-result.xml")
            println("Saving Ktlint result to: ${outputFile.absolutePath}")
            println("Ktlint check results:")
            outputFile.forEachLine { println(it) }
        }
    }
}

tasks.named("preBuild") {
    dependsOn(ktlinCheck)
}
