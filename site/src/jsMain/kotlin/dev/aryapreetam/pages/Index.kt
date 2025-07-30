package dev.aryapreetam.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.aryapreetam.components.layouts.PageLayout
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

            // Blog posts list
            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Column)
                        gap(16.px)
                    }
                }
            ) {
                // Second post (newest first)
                Article(
                    attrs = {
                        style {
                            padding(16.px)
                            border(1.px, LineStyle.Solid, if (colorMode.isLight) Color("#e2e8f0") else Color("#4a5568"))
                            borderRadius(8.px)
                            backgroundColor(if (colorMode.isLight) Color.white else Color("#2d3748"))
                        }
                    }
                ) {
                    H3(
                        attrs = {
                            style {
                                marginBottom(8.px)
                                margin(0.px)
                            }
                        }
                    ) {
                        A(
                            href = "/second-post",
                            attrs = {
                                style {
                                    textDecoration("none")
                                    color(if (colorMode.isLight) Color("#1a202c") else Color("#f7fafc"))
                                    fontSize(18.px)
                                    fontWeight(600)
                                }
                            }
                        ) {
                            Text("Building Components with Silk")
                        }
                    }

                    P(
                        attrs = {
                            style {
                                color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
                                fontSize(12.px)
                                margin(0.px, 0.px, 8.px, 0.px)
                            }
                        }
                    ) {
                        Text("2024-01-20")
                    }

                    P(
                        attrs = {
                            style {
                                lineHeight("1.6")
                                color(if (colorMode.isLight) Color("#4a5568") else Color("#cbd5e0"))
                                margin(0.px, 0.px, 12.px, 0.px)
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Text("Learn how to create reusable UI components using Kobweb's Silk UI framework")
                    }

                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                flexWrap(FlexWrap.Wrap)
                                gap(6.px)
                            }
                        }
                    ) {
                        listOf("kotlin", "kobweb", "silk", "ui-components").forEach { tag ->
                            Span(
                                attrs = {
                                    style {
                                        fontSize(11.px)
                                        padding(2.px, 6.px)
                                        backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
                                        color(if (colorMode.isLight) Color("#718096") else Color("#e2e8f0"))
                                        borderRadius(4.px)
                                    }
                                }
                            ) {
                                Text(tag)
                            }
                        }
                    }
                }

                // First post
                Article(
                    attrs = {
                        style {
                            padding(16.px)
                            border(1.px, LineStyle.Solid, if (colorMode.isLight) Color("#e2e8f0") else Color("#4a5568"))
                            borderRadius(8.px)
                            backgroundColor(if (colorMode.isLight) Color.white else Color("#2d3748"))
                        }
                    }
                ) {
                    H3(
                        attrs = {
                            style {
                                marginBottom(8.px)
                                margin(0.px)
                            }
                        }
                    ) {
                        A(
                            href = "/first-post",
                            attrs = {
                                style {
                                    textDecoration("none")
                                    color(if (colorMode.isLight) Color("#1a202c") else Color("#f7fafc"))
                                    fontSize(18.px)
                                    fontWeight(600)
                                }
                            }
                        ) {
                            Text("My First Blog Post")
                        }
                    }

                    P(
                        attrs = {
                            style {
                                color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
                                fontSize(12.px)
                                margin(0.px, 0.px, 8.px, 0.px)
                            }
                        }
                    ) {
                        Text("2024-01-15")
                    }

                    P(
                        attrs = {
                            style {
                                lineHeight("1.6")
                                color(if (colorMode.isLight) Color("#4a5568") else Color("#cbd5e0"))
                                margin(0.px, 0.px, 12.px, 0.px)
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Text("This is my first blog post about Kobweb and Kotlin development")
                    }

                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                flexWrap(FlexWrap.Wrap)
                                gap(6.px)
                            }
                        }
                    ) {
                        listOf("kotlin", "kobweb", "web-development").forEach { tag ->
                            Span(
                                attrs = {
                                    style {
                                        fontSize(11.px)
                                        padding(2.px, 6.px)
                                        backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
                                        color(if (colorMode.isLight) Color("#718096") else Color("#e2e8f0"))
                                        borderRadius(4.px)
                                    }
                                }
                            ) {
                                Text(tag)
                            }
                        }
                    }
                }
            }
        }
    }
}
