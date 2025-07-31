package dev.aryapreetam.components.layouts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

// JavaScript interop for highlight.js
external object hljs {
  fun highlightAll()
  fun configure(options: dynamic)
}

@InitSilk
fun initHighlightJs(ctx: InitSilkContext) {
  // Tweaks to make output from highlight.js look softer / better
  ctx.stylesheet.registerStyleBase("code.hljs") { Modifier.borderRadius(8.px) }
}

@InitSilk
fun initMarkdownStyles(ctx: InitSilkContext) {
  // Add minimal markdown-specific styles that work with the main theme
  ctx.stylesheet.registerStyleBase("pre") {
    Modifier
      .margin(16.px, 0.px)
      .borderRadius(8.px)
      .overflow(Overflow.Auto)
      .fontSize(14.px)
      .lineHeight(1.5)
  }

  ctx.stylesheet.registerStyleBase("h1, h2, h3, h4, h5, h6") {
    Modifier.margin(32.px, 0.px, 16.px, 0.px)
  }

  ctx.stylesheet.registerStyleBase("h1") {
    Modifier.margin(0.px, 0.px, 24.px, 0.px)
  }

  ctx.stylesheet.registerStyleBase("p") {
    Modifier.margin(0.px, 0.px, 16.px, 0.px)
  }

  ctx.stylesheet.registerStyleBase("ul, ol") {
    Modifier.margin(16.px, 0.px).padding(left = 24.px)
  }

  ctx.stylesheet.registerStyleBase("li") {
    Modifier.margin(0.px, 0.px, 4.px, 0.px)
  }

  // Style horizontal rules to be clean without extra padding
  ctx.stylesheet.registerStyleBase("hr") {
    Modifier
      .margin(24.px, 0.px)
      .padding(0.px)
      .border(0.px)
      .height(1.px)
      .width(100.percent)
      .backgroundColor(Color.currentColor)
      .opacity(0.3)
      .display(DisplayStyle.Block)
      .outline(0.px)
  }

  // Style code blocks to use JetBrains Mono font with better styling
  ctx.stylesheet.registerStyleBase("code") {
    Modifier.fontFamily(
      "'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace"
    )
      .fontSize(14.px)
      .fontWeight(500)
  }

  ctx.stylesheet.registerStyleBase("pre code") {
    Modifier
      .fontFamily(
        "'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace"
      )
      .fontSize(14.px)
      .lineHeight(1.5)
      .fontWeight(400)
  }

  // Ensure JSON syntax highlighting works
  ctx.stylesheet.registerStyleBase("code.language-json, pre code.language-json") {
    Modifier
      .fontFamily(
        "'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace"
      )
      .fontSize(14.px)
  }
}

/**
 * Layout specifically for markdown pages
 * This builds on top of PageLayout to provide styling optimized for markdown content
 */
@Layout
@Composable
fun MarkdownLayout(content: @Composable () -> Unit) {
  var colorMode by ColorMode.currentState

  // Apply syntax highlighting after content renders
  LaunchedEffect(content, colorMode) {
    // Small delay to ensure DOM is fully rendered
    window.setTimeout({
      try {
        if (js("typeof hljs !== 'undefined'") as Boolean) {
          hljs.configure(js("{ ignoreUnescapedHTML: true }"))
          hljs.highlightAll()
        }
      } catch (e: Exception) {
        console.log("Highlight.js error:", e)
      }
    }, 100)
    }

  PageLayout {
    // Apply line height styling to the content without wrapping in Box
    // This prevents interference with PageLayout's centering and max-width
    Div(attrs = {
      style {
        lineHeight("1.6")
      }
    }) {
      content()
    }
  }
}

/**
 * Alternative simplified markdown layout without the container box
 */
@Layout
@Composable
fun SimpleMarkdownLayout(content: @Composable () -> Unit) {
  PageLayout {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .maxWidth(1024.px)
        .margin(0.px)
        .alignSelf(AlignSelf.Center)
    ) {
      content()
    }
  }
}