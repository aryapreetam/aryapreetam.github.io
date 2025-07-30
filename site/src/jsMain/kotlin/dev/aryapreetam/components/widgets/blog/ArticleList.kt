package dev.aryapreetam.components.widgets.blog

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.aryapreetam.pages.blog.ArticleEntry
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ArticleList(entries: List<ArticleEntry>) {
  var colorMode by ColorMode.currentState

  if (entries.isEmpty()) {
    P(
      attrs = {
        style {
          color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
          property("text-align", "center")
        }
      }
    ) {
      Text("No posts available yet.")
        }
    } else {
      Div(
        attrs = {
          style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            gap(20.px)
          }
        }
      ) {
        entries.forEach { entry ->
          ArticleSummary(entry)
        }
        }
    }
}

@Composable
private fun ArticleSummary(entry: ArticleEntry) {
  var colorMode by ColorMode.currentState

  Article(
    attrs = {
      style {
        padding(24.px)
        border(
          1.px,
          LineStyle.Solid,
          if (colorMode.isLight) Color("#e2e8f0") else Color("#4a5568")
        )
        borderRadius(12.px)
        backgroundColor(if (colorMode.isLight) Color.white else Color("#2d3748"))
        property("transition", "all 0.2s ease-in-out")
      }
    }
  ) {
    // Article title as link
    H3(
      attrs = {
        style {
          marginBottom(12.px)
          margin(0.px, 0.px, 12.px, 0.px)
        }
      }
    ) {
      Link(
        path = entry.path,
        modifier = Modifier
          .textDecorationLine(TextDecorationLine.None)
          .color(if (colorMode.isLight) Color("#1a202c") else Color("#f7fafc"))
          .fontSize(20.px)
          .fontWeight(FontWeight.SemiBold)
      ) {
        Text(entry.title)
      }
    }

    // Date and author
    Div(
      attrs = {
        style {
          color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
          fontSize(14.px)
          marginBottom(16.px)
          display(DisplayStyle.Flex)
          alignItems(AlignItems.Center)
          gap(8.px)
        }
      }
    ) {
      Span { Text(entry.date) }
      Span { Text("•") }
      Span { Text(entry.author) }
    }

    // Description
    P(
      attrs = {
        style {
          lineHeight("1.6")
          color(if (colorMode.isLight) Color("#4a5568") else Color("#cbd5e0"))
          margin(0.px, 0.px, 16.px, 0.px)
          fontSize(16.px)
        }
      }
    ) {
      Text(entry.desc)
    }

    // Tags
    if (entry.tags.isNotEmpty()) {
      Div(
        attrs = {
          style {
            display(DisplayStyle.Flex)
            flexWrap(FlexWrap.Wrap)
            gap(8.px)
          }
        }
      ) {
        entry.tags.forEach { tag ->
          Span(
            attrs = {
              style {
                fontSize(12.px)
                padding(4.px, 8.px)
                backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
                color(if (colorMode.isLight) Color("#718096") else Color("#e2e8f0"))
                borderRadius(6.px)
                fontWeight("500")
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