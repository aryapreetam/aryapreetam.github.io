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

// Simple escape function for code content
fun String.escapeForKotlinString(): String {
  return this.replace("\\", "\\\\")  // Escape backslashes first
    .replace("\"", "\\\"")         // Escape quotes
    .replace("\n", "\\n")          // Escape newlines
    .replace("\r", "\\r")          // Escape carriage returns
    .replace("\t", "\\t")          // Escape tabs
    .replace("$", "\\$")           // Escape dollar signs to prevent string interpolation
}

class BlogEntry(
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

      // Temporarily disable custom code handlers to fix compilation issues
      // TODO: Re-enable after fixing escaping
      // code.set { code ->
      //   "$AR_WGT.code.CodeBlock(\"${code.literal.escapeForKotlinString()}\", lang = ${
      //     code.info.takeIf { it.isNotBlank() }?.let { "\"$it\"" }
      //   })"
      // }

      // inlineCode.set { code ->
      //   "$AR_WGT.code.InlineCode(\"${code.literal.escapeForKotlinString()}\")"
      // }
    }

    process.set { markdownEntries ->
      val requiredFields = listOf("title", "description", "date")
      val blogEntries = markdownEntries.mapNotNull { markdownEntry ->
        val fm = markdownEntry.frontMatter
        val (title, desc, date) = requiredFields
          .map { key -> fm[key]?.singleOrNull() }
          .takeIf { values -> values.all { it != null } }
          ?.requireNoNulls()
          ?: run {
            println("Not adding \"${markdownEntry.filePath}\" into the listing file as it is missing required frontmatter fields (one of $requiredFields)")
            return@mapNotNull null
          }

        val author = fm["author"]?.singleOrNull() ?: "Arya Preetam" // Default author
        val tags = fm["tags"] ?: emptyList()
        BlogEntry(markdownEntry.route, author, date, title, desc, tags)
      }

      val blogPackage = "dev.aryapreetam.pages.blog"
      val blogPath = "${blogPackage.replace('.', '/')}/GeneratedBlogData.kt"
      generateKotlin(blogPath, buildString {
        appendLine(
          """
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
                    """.trimIndent()
        )

        blogEntries.sortedByDescending { it.date }.forEach { entry ->
          appendLine("            ${entry.toArticleEntry()},")
        }

        appendLine(
          """
                        )
                    }
                    """.trimIndent()
        )
      })
      println("Generated blog listing data at \"$blogPath\".")
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
