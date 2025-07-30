package dev.aryapreetam.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.aryapreetam.components.layouts.PageLayout
import dev.aryapreetam.components.widgets.blog.ArticleList
import dev.aryapreetam.pages.blog.GeneratedBlogData
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Page
@Layout("dev.aryapreetam.components.layouts.PageLayout")
@Composable
fun HomePage() {
    var colorMode by ColorMode.currentState

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Introduction section
        Section(
            attrs = {
                style {
                    marginBottom(32.px)
                }
            }
        ) {
            P(
                attrs = {
                    style {
                        fontSize(24.px)
                        lineHeight("1.4")
                        color(if (colorMode.isLight) Color("#1a202c") else Color("#f7fafc"))
                        marginBottom(16.px)
                        fontWeight(600)
                    }
                }
            ) {
                Text("Hello 👋,")
            }

            P(
                attrs = {
                    style {
                        fontSize(16.px)
                        lineHeight("1.6")
                        color(if (colorMode.isLight) Color("#4a5568") else Color("#cbd5e0"))
                        marginBottom(12.px)
                    }
                }
            ) {
                Text("I am a Software Engineer specializing in cross-platform apps development primarily with Compose/Kotlin Multiplatform.")
            }

            P(
                attrs = {
                    style {
                        fontSize(16.px)
                        lineHeight("1.6")
                        color(if (colorMode.isLight) Color("#4a5568") else Color("#cbd5e0"))
                        marginBottom(12.px)
                    }
                }
            ) {
                Text("I like to do high impact work with minimal efforts. Constantly in quest for technologies that simplify dev and end user experience.")
            }

            P(
                attrs = {
                    style {
                        fontSize(14.px)
                        color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
                        marginBottom(12.px)
                    }
                }
            ) {
                Text("Contact me: ")
                A(
                    href = "mailto:preetb123@gmail.com",
                    attrs = {
                        style {
                            color(if (colorMode.isLight) Color("#3182ce") else Color("#63b3ed"))
                            textDecoration("underline")
                        }
                    }
                ) {
                    Text("preetb123@gmail.com")
                }
            }
        }

        // Posts section
        Section {
            H2(
                attrs = {
                    style {
                        fontSize(24.px)
                        fontWeight(700)
                        color(if (colorMode.isLight) Color("#1a202c") else Color("#f7fafc"))
                        marginBottom(24.px)
                    }
                }
            ) {
                Text("Posts")
            }

            // Use generated blog data - completely dynamic!
            ArticleList(GeneratedBlogData.entries)
        }
    }
}
