import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import com.varabyte.kobwebx.gradle.markdown.handlers.MarkdownHandlers
import com.varabyte.kobwebx.gradle.markdown.ext.kobwebcall.KobwebCall
import kotlinx.html.script

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
}

group = "dev.aryapreetam"
version = "1.0-SNAPSHOT"

// Extension function to properly escape text for Kotlin triple-quoted strings
fun String.escapeTripleQuotedText(): String {
  return this.replace("\"\"\"", "\\\"\\\"\\\"")
}

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
          head.add {
            script {
              // Needed for syntax highlighting
              src = "/highlight.js/highlight.min.js"
            }
            }
        }
    }

  markdown {
    handlers {
      val AR_WGT = "dev.aryapreetam.components.widgets"

      code.set { code ->
        "$AR_WGT.code.CodeBlock(\"\"\"${code.literal.escapeTripleQuotedText()}\"\"\", lang = ${
          code.info.takeIf { it.isNotBlank() }?.let { "\"$it\"" }
        })"
      }

      inlineCode.set { code ->
        "$AR_WGT.code.InlineCode(\"\"\"${code.literal.escapeTripleQuotedText()}\"\"\")"
      }
    }
  }
}

kotlin {
    configAsKobwebApplication("aryapreetam")

    sourceSets {
      val commonMain by getting {
        dependencies {
          implementation(libs.compose.runtime)
        }
        }

      val jsMain by getting {
        dependencies {
          implementation(libs.compose.html.core)
          implementation(libs.kobweb.core)
          implementation(libs.kobweb.silk)
          implementation(libs.silk.icons.fa)
          implementation(libs.kobwebx.markdown)
        }
        }
    }
}
