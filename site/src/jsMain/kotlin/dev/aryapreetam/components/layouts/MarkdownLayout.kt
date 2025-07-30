package dev.aryapreetam.components.layouts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@InitSilk
fun initHighlightJs(ctx: InitSilkContext) {
  // Tweaks to make output from highlight.js look softer / better
  ctx.stylesheet.registerStyleBase("code.hljs") { Modifier.borderRadius(8.px) }
}

/**
 * Layout specifically for markdown pages
 * This builds on top of PageLayout to provide styling optimized for markdown content
 */
@Layout
@Composable
fun MarkdownLayout(content: @Composable () -> Unit) {
  var colorMode by ColorMode.currentState

  // Load appropriate CSS for syntax highlighting based on theme
  LaunchedEffect(colorMode) {
    var styleElement = document.querySelector("""link[title="hljs-style"]""")
    if (styleElement == null) {
      styleElement = document.createElement("link").apply {
        setAttribute("type", "text/css")
        setAttribute("rel", "stylesheet")
        setAttribute("title", "hljs-style")
      }.also { document.head!!.appendChild(it) }
    }
    styleElement.setAttribute("href", "/highlight.js/styles/a11y-${colorMode.name.lowercase()}.min.css")
  }

  // Trigger syntax highlighting when content changes
  LaunchedEffect(content) {
    // Initialize highlight.js
    js("hljs.highlightAll()")
  }

  PageLayout {
    // Container for markdown content with proper styling
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .maxWidth(800.px)
        .margin(0.px)
        .alignSelf(AlignSelf.Center)
    ) {
      // Add markdown-specific styles with JetBrains Mono font
      Style {
        val textColor = if (colorMode.isLight) "#2d3748" else "#e2e8f0"
        val headingColor = if (colorMode.isLight) "#1a202c" else "#f7fafc"
        val linkColor = if (colorMode.isLight) "#3182ce" else "#63b3ed"
        val codeBackground = if (colorMode.isLight) "#f7fafc" else "#2d3748"
        val codeTextColor = if (colorMode.isLight) "#e53e3e" else "#fc8181"
        val borderColor = if (colorMode.isLight) "#e2e8f0" else "#4a5568"
        val blockquoteBackground = if (colorMode.isLight) "#f8fafc" else "#374151"

        // Global markdown styles with JetBrains Mono for code
        """
          .markdown-content {
              line-height: 1.6;
              color: $textColor;
              font-size: 16px;
              font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
          }
          
          .markdown-content h1, .markdown-content h2, .markdown-content h3, 
          .markdown-content h4, .markdown-content h5, .markdown-content h6 {
              color: $headingColor;
              font-weight: 600;
              margin-top: 2rem;
              margin-bottom: 1rem;
              line-height: 1.25;
              font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
          }
          
          .markdown-content h1 { 
              font-size: 2rem; 
              margin-top: 0;
              font-weight: 700;
          }
          .markdown-content h2 { font-size: 1.5rem; font-weight: 600; }
          .markdown-content h3 { font-size: 1.25rem; font-weight: 600; }
          .markdown-content h4 { font-size: 1rem; font-weight: 600; }
          
          .markdown-content p {
              margin-bottom: 1rem;
              line-height: 1.6;
          }
          
          .markdown-content a {
              color: $linkColor;
              text-decoration: underline;
              text-decoration-color: rgba(49, 130, 206, 0.4);
              text-underline-offset: 2px;
          }
          
          .markdown-content a:hover {
              text-decoration-color: $linkColor;
          }
          
          .markdown-content code {
              background-color: $codeBackground;
              color: $codeTextColor;
              padding: 0.125rem 0.25rem;
              border-radius: 0.25rem;
              font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
              font-size: 0.875em;
              font-weight: 500;
          }
          
          .markdown-content pre {
              margin: 1.5rem 0;
              overflow-x: auto;
              line-height: 1.5;
              border-radius: 8px;
          }
          
          .markdown-content pre code {
              background: none;
              color: inherit;
              padding: 1rem;
              font-size: 0.875rem;
              font-weight: 400;
              font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
              display: block;
          }
          
          .markdown-content ul, .markdown-content ol {
              margin: 1rem 0;
              padding-left: 1.5rem;
          }
          
          .markdown-content li {
              margin-bottom: 0.25rem;
              line-height: 1.6;
          }
          
          .markdown-content blockquote {
              background-color: $blockquoteBackground;
              border-left: 4px solid $linkColor;
              margin: 1.5rem 0;
              padding: 1rem 1.5rem;
              border-radius: 0 0.25rem 0.25rem 0;
          }
          
          .markdown-content blockquote p {
              margin: 0;
          }
          
          .markdown-content hr {
              border: none;
              border-top: 1px solid $borderColor;
              margin: 2rem 0;
          }
          
          .markdown-content strong {
              font-weight: 600;
              color: $headingColor;
          }
          
          .markdown-content em {
              font-style: italic;
          }
          """.trimIndent()
      }

      // Wrapper div for markdown content styling
      Div(
        attrs = {
          classes("markdown-content")
        }
      ) {
        content()
      }
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
        .maxWidth(800.px)
        .margin(0.px)
        .alignSelf(AlignSelf.Center)
    ) {
      content()
    }
  }
}