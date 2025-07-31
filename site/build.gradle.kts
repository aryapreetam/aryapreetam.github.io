import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import com.varabyte.kobwebx.gradle.markdown.handlers.MarkdownHandlers
import com.varabyte.kobwebx.gradle.markdown.ext.kobwebcall.KobwebCall
import kotlinx.html.script
import kotlinx.html.link

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

// Blog entry data class for build-time processing
data class BlogEntry(
  val route: String,
  val author: String,
  val date: String,
  val title: String,
  val desc: String,
  val tags: List<String>
) {
  private fun String.escapeQuotes() = this.replace("\"", "\\\"")
  fun toArticleEntry() =
    """ArticleEntry("$route", "$author", "$date", "${title.escapeQuotes()}", "${desc.escapeQuotes()}", tags = listOf(${tags.joinToString { "\"$it\"" }}))"""
}

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
          head.add {
            // Prism.js for syntax highlighting (more reliable than highlight.js)
            script {
              src = "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
            }
            script {
              src = "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
            }
            // Note: Theme will be loaded dynamically based on color mode in CodeBlock component
          }
        }
    }

  markdown {
    handlers {
      val AR_WGT = "dev.aryapreetam.components.widgets"

      code.set { code ->
        val info = code.info.takeIf { it.isNotBlank() } ?: ""
        val (lang, filename) = if (info.contains(":")) {
          val parts = info.split(":", limit = 2)
          parts[0] to parts[1]
        } else {
          info to null
        }

        val langParam = if (lang.isNotBlank()) "\"$lang\"" else "null"
        val filenameParam = if (filename?.isNotBlank() == true) "\"$filename\"" else "null"

        "$AR_WGT.code.CodeBlock(\"\"\"${code.literal.escapeTripleQuotedText()}\"\"\", lang = $langParam, filename = $filenameParam)"
      }

      inlineCode.set { code ->
        "$AR_WGT.code.InlineCode(\"\"\"${code.literal.escapeTripleQuotedText()}\"\"\")"
      }
    }

    // Enable automatic blog data generation
    process.set { markdownEntries ->
      val requiredFields = listOf("title", "description", "date")
      val blogEntries = markdownEntries.mapNotNull { markdownEntry ->
        val fm = markdownEntry.frontMatter
        val (title, desc, date) = requiredFields
          .map { key -> fm[key]?.singleOrNull() }
          .takeIf { values -> values.all { it != null } }
          ?.requireNoNulls()
          ?: return@mapNotNull null

        val author = fm["author"]?.singleOrNull() ?: "Arya Preetam"
        val tags = fm["tags"] ?: emptyList()
        BlogEntry(markdownEntry.route, author, date, title, desc, tags)
      }

      // Generate the blog data file
      val blogPackage = "dev.aryapreetam.pages.blog"
      val blogPath = "${blogPackage.replace('.', '/')}/GeneratedBlogData.kt"
      
      generateKotlin(blogPath, """
// This file is generated. Modify the build script if you need to change it.
package $blogPackage

data class ArticleEntry(
    val path: String, 
    val author: String, 
    val date: String, 
    val title: String, 
    val desc: String,
    val tags: List<String> = emptyList()
)

object GeneratedBlogData {
    val entries = listOf(
        ${blogEntries.sortedByDescending { it.date }.joinToString(",\n        ") { it.toArticleEntry() }}
    )
}
""".trimIndent())
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
