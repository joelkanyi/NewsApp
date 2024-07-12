// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        debug.set(true)
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        additionalEditorconfig.set(
            mapOf(
                "ktlint_code_style" to "intellij_idea",
                "ktlint_standard_string-template-indent" to "disabled",
                "ktlint_standard_trailing-comma-on-call-site" to "disabled",
                "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                "insert_final_newline" to "true",
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_multiline-expression-wrapping" to "disabled",
                "ktlint_standard_comment-wrapping" to "disabled",
                "ktlint_standard_parameter-list-wrapping" to "disabled",
                "ktlint_standard_argument-list-wrapping" to "disabled",
                "ktlint_standard_wrapping" to "disabled",
            ),
        )
        enableExperimentalRules.set(true)
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        parallel = true
        buildUponDefaultConfig = true
        allRules = true
        config.setFrom("$rootDir/config/detekt/detekt.yml")
    }
}
