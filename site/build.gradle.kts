import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import com.varabyte.kobwebx.gradle.markdown.handlers.MarkdownHandlers
import com.varabyte.kobwebx.gradle.markdown.ext.kobwebcall.KobwebCall
import kotlinx.html.script
import kotlinx.html.link
import kotlinx.html.meta

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
            description.set("Preetam's Blog - Software Engineer focused on cross-platform app development using Compose/Kotlin Multiplatform. High-leverage engineering insights and development tutorials.")
            
            head.add {
                // SEO Meta Tags
                meta {
                    name = "description"
                    content = "Preetam's Blog - Software Engineer focused on cross-platform app development using Compose/Kotlin Multiplatform. High-leverage engineering insights and development tutorials."
                }
                meta {
                    name = "keywords"
                    content = "Kotlin Multiplatform, Compose, Android Development, Cross-platform, Software Engineering, High-leverage Engineering"
                }
                meta {
                    name = "author"
                    content = "Arya Preetam"
                }
                meta {
                    name = "robots"
                    content = "index, follow"
                }
                meta {
                    name = "googlebot"
                    content = "index, follow"
                }
                
                // Open Graph Tags
                meta {
                    attributes["property"] = "og:title"
                    content = "Preetam's Blog - Software Engineering & Kotlin Multiplatform"
                }
                meta {
                    attributes["property"] = "og:description"
                    content = "Software Engineer focused on cross-platform app development. High-leverage engineering insights and Kotlin Multiplatform tutorials."
                }
                meta {
                    attributes["property"] = "og:type"
                    content = "website"
                }
                meta {
                    attributes["property"] = "og:url"
                    content = "https://aryapreetam.github.io"
                }
                meta {
                    attributes["property"] = "og:site_name"
                    content = "Preetam's Blog"
                }
                
                // Twitter Card Tags
                meta {
                    name = "twitter:card"
                    content = "summary"
                }
                meta {
                    name = "twitter:site"
                    content = "@preetambhosle"
                }
                meta {
                    name = "twitter:creator"
                    content = "@preetambhosle"
                }
                meta {
                    name = "twitter:title"
                    content = "Preetam's Blog - Software Engineering & Kotlin Multiplatform"
                }
                meta {
                    name = "twitter:description"
                    content = "Software Engineer focused on cross-platform app development. High-leverage engineering insights and tutorials."
                }
                
                // Mobile and PWA
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1, shrink-to-fit=no"
                }
                meta {
                    name = "theme-color"
                    content = "#1a202c"
                }
                meta {
                    name = "mobile-web-app-capable"
                    content = "yes"
                }
                meta {
                    name = "apple-mobile-web-app-capable"
                    content = "yes"
                }
                meta {
                    name = "apple-mobile-web-app-status-bar-style"
                    content = "default"
                }
                
                // Preconnect for Performance
                link {
                    rel = "preconnect"
                    href = "https://fonts.googleapis.com"
                }
                link {
                    rel = "preconnect"
                    href = "https://fonts.gstatic.com"
                    attributes["crossorigin"] = ""
                }
                link {
                    rel = "dns-prefetch"
                    href = "https://cdnjs.cloudflare.com"
                }
                
                // Optimized Font Loading
                link {
                    rel = "preload"
                    href = "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap"
                    attributes["as"] = "style"
                    attributes["onload"] = "this.onload=null;this.rel='stylesheet'"
                }

                // Web App Manifest
                link {
                    rel = "manifest"
                    href = "/manifest.webmanifest"
                }

                // Optimized Favicon
                link {
                    rel = "icon"
                    type = "image/svg+xml"
                    href = "/favicon.svg"
                }
                link {
                    rel = "icon"
                    type = "image/png"
                    sizes = "32x32"
                    href = "/favicon-32x32.png"
                }
                link {
                    rel = "icon"
                    type = "image/png"
                    sizes = "16x16"
                    href = "/favicon-16x16.png"
                }
                link {
                    rel = "apple-touch-icon"
                    sizes = "180x180"
                    href = "/apple-touch-icon.png"
                }

                // Structured Data (JSON-LD for better SEO) - Add via external file
                script {
                    type = "application/ld+json"
                    src = "/schema.json"
                }
                
                // Performance and Security Headers (for static hosting)
                meta {
                    attributes["http-equiv"] = "X-Content-Type-Options"
                    content = "nosniff"
                }
                meta {
                    attributes["http-equiv"] = "X-Frame-Options"
                    content = "SAMEORIGIN"
                }
                meta {
                    attributes["http-equiv"] = "X-XSS-Protection"
                    content = "1; mode=block"
                }

                // Load Prism.js for syntax highlighting (.kts and more reliable integration)
                script {
                    src = "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
                    attributes["crossorigin"] = "anonymous"
                }
                script {
                    src =
                        "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
                    attributes["crossorigin"] = "anonymous"
                }
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
