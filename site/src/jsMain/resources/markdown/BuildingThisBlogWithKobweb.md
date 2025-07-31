---
title: "Building This Blog with Kobweb: A Complete Technical Journey"
description: "A detailed walkthrough of creating a modern technical blog using Kobweb, from initial setup to advanced
features like code highlighting and dynamic content generation."
date: "2025-01-31"
tags: ["kobweb", "kotlin", "web-development", "blog", "compose", "markdown"]
draft: false
layout: "dev.aryapreetam.components.layouts.MarkdownLayout"
---

# Building This Blog with Kobweb: A Complete Technical Journey

Having a technical blog where I can share all the development related experiences & experiments was a long overdue. 
Even though I was familiar with Javascript frameworks that are available(and are widely used), 
I was somehow reluctant to build my blog with it. Then came Kobweb. 
By default, Kobweb will build a blog like 
https://bitspittle.github.io/kobweb-ghp-demo. I needed some improvement to match my design taste. 

This post chronicles the complete technical journey of creating a modern, professional blog using Kobweb - from the
initial setup to advanced features like syntax highlighting and dynamic content generation.

---

## Why Kobweb?

As a Kotlin developer who loves Compose, Kobweb felt like a natural choice for building a technical blog. The promise of
using familiar Kotlin syntax with Compose-like APIs for web development was too compelling to ignore. Plus, the ability
to write blog posts in Markdown while having full programmatic control over the presentation was exactly what I needed.

## Phase 1: Foundation Setup

### Creating the Initial Project

The journey began with creating a new Kobweb project:

```bash
kobweb create blog
// it will ask some questions, i chose everything
cd blog
```

However, the initial setup wasn't smooth sailing. The newly generated project came with some version compatibility
issues that needed immediate attention.

### Version Compatibility Issues

The generated project used the latest versions of Kobweb and Kotlin, which had some compatibility issues. Here's what I
had to downgrade:

```kotlin
// gradle/libs.versions.toml - Before
kotlin = "2.2.0"
kobweb = "0.23.0"

// gradle/libs.versions.toml - After (stable versions)
kotlin = "2.1.21"
kobweb = "0.22.0"
```

The key lesson here: sometimes the bleeding edge isn't worth the pain. Stick with stable, well-tested versions for
production projects.

### Adding Markdown Support

The next step was enabling markdown(if not enabled already) support for blog posts:

```kotlin
// site/build.gradle.kts
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kobweb.application)
  alias(libs.plugins.kobwebx.markdown) // This was crucial
}

kobwebx {
  markdown {
    markdownPath.set("markdown")
  }
}
```

This configuration tells Kobweb to look for markdown files in `src/jsMain/resources/markdown/` and automatically
generate routes for them.

## Phase 2: Dynamic Blog System

One of my primary goals was to eliminate hardcoded blog entries. I wanted the home page to automatically discover and
list all blog posts from markdown files.

### Auto-Discovery of Blog Posts

The breakthrough came with understanding how Kobweb's markdown integration works. Here's the solution I implemented:

```kotlin
// pages/Index.kt
@Page
@Composable
fun HomePage() {
  var colorMode by ColorMode.currentState

  Column(modifier = Modifier.fillMaxWidth()) {
    // Introduction section
    IntroductionSection()
    
    // Posts section - completely dynamic!
    Section {
      H2 { Text("Posts") }
      ArticleList(GeneratedBlogData.entries)
    }
  }
}
```

The magic happens in `GeneratedBlogData.entries`, which is auto-generated from the markdown files using a custom Gradle
task:

```kotlin
// build.gradle.kts
tasks.register("generateBlogData") {
  val markdownDir = file("src/jsMain/resources/markdown")
  val outputFile = file("src/jsMain/kotlin/dev/aryapreetam/pages/blog/GeneratedBlogData.kt")
  
  doLast {
    val markdownFiles = markdownDir.listFiles()?.filter { it.extension == "md" } ?: emptyList()
    val blogEntries = markdownFiles.map { file ->
      val content = file.readText()
      val frontMatter = extractFrontMatter(content)
      
      BlogEntry(
        title = frontMatter["title"] as? String ?: "Untitled",
        description = frontMatter["description"] as? String ?: "",
        date = frontMatter["date"] as? String ?: "",
        route = "/markdown/${file.nameWithoutExtension}",
        tags = frontMatter["tags"] as? List<String> ?: emptyList()
      )
    }.sortedByDescending { it.date }
    
    generateKotlinFile(outputFile, blogEntries)
  }
}
```

This approach means I can simply drop a new markdown file into the `markdown/` directory, and it automatically appears
on the home page. No hardcoding required!

## Phase 3: Professional Code Highlighting

As a technical blog, proper syntax highlighting was non-negotiable. This turned out to be one of the most challenging
aspects of the project.

### The highlight.js Experiment

My first attempt used highlight.js:

```kotlin
// Initial approach with highlight.js
external object hljs {
  fun highlightElement(element: HTMLElement)
  fun configure(options: dynamic)
}

LaunchedEffect(Unit) {
  // Load language definitions
  val script = document.createElement("script").apply {
    src = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/languages/kotlin.min.js"
  }
  document.head?.appendChild(script)
  
  // This approach had race conditions and reliability issues
  window.setTimeout({
    hljs.highlightElement(codeElement)
  }, 100)
}
```

**Problems encountered:**

- Race conditions with language loading
- Inconsistent `.kts` file highlighting
- Complex language registration process
- Theme switching difficulties

### The Prism.js Solution

After fighting with highlight.js, I switched to Prism.js with much better results:

```kotlin
// External declarations for Prism.js
external object Prism {
  fun highlightElement(element: HTMLElement)
  val plugins: dynamic
}

@Composable
fun CodeBlock(
  code: String,
  language: String? = null,
  filename: String? = null
) {
  val codeRef = remember { mutableStateOf<HTMLElement?>(null) }
  var copied by remember { mutableStateOf(false) }
  var colorMode by ColorMode.currentState

  // Dynamic theme loading based on color mode
  LaunchedEffect(colorMode) {
    document.getElementById("prism-theme")?.remove()
    
    val themeUrl = if (colorMode.isLight) {
      "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css"
    } else {
      "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-okaidia.min.css"
    }
    
    val themeLink = document.createElement("link").apply {
      id = "prism-theme"
      setAttribute("rel", "stylesheet")
      setAttribute("href", themeUrl)
    }
    document.head?.appendChild(themeLink)
  }

  // Highlight code after component mounts
  LaunchedEffect(code, language) {
    codeRef.value?.let { element ->
      Prism.highlightElement(element)
    }
  }

  // Component JSX here...
}
```

**Key improvements with Prism.js:**

- Reliable autoloader plugin
- Better `.kts` file support (mapped to Kotlin)
- Smoother theme switching
- More consistent highlighting results

## Phase 4: Advanced UX Features

### Interactive Code Blocks

The final version of the code blocks includes several UX enhancements:

```kotlin
// Professional code block with filename tabs and copy functionality
Div(attrs = {
  style {
    position(Position.Relative)
    marginBottom(24.px)
  }
}) {
  // Filename tab (only shown when filename is provided)
  filename?.let {
    Div(attrs = {
      style {
        position(Position.Absolute)
        top((-2).px)
        left(0.px)
        zIndex(15)
        backgroundColor(if (colorMode.isLight) Color("#f8f9fa") else Color("#2d3748"))
        border(1.px, LineStyle.Solid, if (colorMode.isLight) Color("#e2e8f0") else Color("#4a5568"))
        borderRadius(6.px, 6.px, 0.px, 0.px)
        padding(4.px, 12.px)
        fontSize(12.px)
        fontFamily("system-ui", "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Helvetica", "Arial", "sans-serif")
        color(if (colorMode.isLight) Color("#4a5568") else Color("#a0aec0"))
      }
    }) {
      Text(it)
    }
  }
  
  // Main code container
  Div(attrs = {
    style {
      position(Position.Relative)
      backgroundColor(if (colorMode.isLight) Color("#f8f9fa") else Color("#2d3748"))
      border(1.px, LineStyle.Solid, if (colorMode.isLight) Color("#e2e8f0") else Color("#4a5568"))
      borderRadius(6.px)
      overflow("hidden")
    }
  }) {
    // Copy button with hover effects
    Button(
      onClick = {
        copyToClipboard(code)
        copied = true
        window.setTimeout({ copied = false }, 2000)
      },
      attrs = {
        style {
          position(Position.Absolute)
          top(8.px)
          right(8.px)
          zIndex(10)
          backgroundColor(Color.transparent)
          border(1.px, LineStyle.Solid, if (colorMode.isLight) Color("#cbd5e0") else Color("#4a5568"))
          borderRadius(4.px)
          padding(4.px, 8.px)
          fontSize(12.px)
          color(if (colorMode.isLight) Color("#4a5568") else Color("#a0aec0"))
          cursor("pointer")
        }
        onMouseEnter { /* hover effects */ }
        onMouseLeave { /* hover effects */ }
      }
    ) {
      Text(if (copied) "Copied!" else "Copy")
    }
    
    // The actual code element
    Pre(attrs = {
      style {
        margin(0.px)
        padding(16.px)
        overflow("auto")
        backgroundColor(Color.transparent)
      }
    }) {
      Code(
        attrs = {
          classes("language-${language ?: "text"}")
          ref { element ->
            codeRef.value = element
            onDispose { }
          }
          style {
            fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
            fontSize(14.px)
            lineHeight("1.5")
          }
        }
      ) {
        Text(code.trim())
      }
    }
  }
}
```

### Typography and Font Loading

Getting the typography right was crucial for a professional feel:

```kotlin
// Font loading strategy in PageLayout.kt
LaunchedEffect(Unit) {
  // 1. Preload JetBrains Mono for performance
  if (document.querySelector("link[href*='JetBrains+Mono']") == null) {
    val preloadLink = document.createElement("link").apply {
      setAttribute("rel", "preload")
      setAttribute("as", "font")
      setAttribute("type", "font/woff2")
      setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
      setAttribute("crossorigin", "")
    }
    document.head?.appendChild(preloadLink)

    // 2. Load the actual font stylesheet
    val linkElement = document.createElement("link").apply {
      setAttribute("rel", "stylesheet")
      setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
    }
    document.head?.appendChild(linkElement)
  }

  // 3. Ensure JetBrains Mono is applied to all code elements
  val globalCodeStyleId = "global-jetbrains-mono"
  if (document.getElementById(globalCodeStyleId) == null) {
    val style = document.createElement("style").apply {
      setAttribute("id", globalCodeStyleId)
      textContent = """
        code, pre code, .hljs, 
        code[class*="language-"], 
        pre[class*="language-"] code {
          font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important;
          font-feature-settings: 'liga' 0 !important;
          font-variant-ligatures: none !important;
        }
      """.trimIndent()
    }
    document.head?.appendChild(style)
  }
}
```

**Typography Strategy:**

- **Code**: JetBrains Mono (professional, readable)
- **UI Elements**: System fonts (SF Pro, Segoe UI, etc.)
- **Body Text**: System UI fonts for optimal readability
- **Fallback Chain**: Comprehensive fallbacks for all font categories

## Phase 5: Social Features and Polish

### Social Icons Component

No modern blog is complete without social links. I created a reusable social icons component:

```kotlin
@Composable
fun SocialIcons() {
  var colorMode by ColorMode.currentState
  
  val iconColor = if (colorMode.isLight) Color("#4a5568") else Color("#a0aec0")
  val hoverColor = if (colorMode.isLight) Color("#2d3748") else Color("#f7fafc")
  
  Row(modifier = Modifier.gap(16.px)) {
    // Email icon
    A(href = "mailto:mymail@gmail.com") {
      Svg(attrs = { 
        attr("width", "24")
        attr("height", "24") 
        attr("fill", "currentColor")
      }) {
        Path { d("M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z") }
      }
    }
    
    // LinkedIn, Twitter, GitHub icons...
  }
}
```

### Theme System

The blog supports both light and dark themes with smooth transitions:

```kotlin
// Theme toggle in PageLayout.kt
Button(
  onClick = {
    val newMode = colorMode.opposite
    colorMode = newMode
    localStorage.setItem("kobweb-color-mode", if (newMode.isDark) "dark" else "light")
  },
  modifier = Modifier
    .position(Position.Fixed)
    .top(16.px)
    .right(16.px)
    .borderRadius(50.percent)
    .backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
) {
  if (colorMode.isLight) FaMoon() else FaSun()
}
```

The theme preference is persisted to localStorage and automatically restored on subsequent visits.

## Major Challenges and Solutions

### 1. Kotlin Script (.kts) Highlighting

**Problem**: Despite loading kotlin.min.js, `.kts` files showed as plain text.

**Solution**: Switched to Prism.js and ensured proper language mapping:

```kotlin
// Map .kts files to kotlin language
val effectiveLanguage = when (language?.lowercase()) {
  "kts", "kotlin script" -> "kotlin"
  else -> language
}
```

### 2. Filename Tab Clipping

**Problem**: The filename tabs were getting clipped by the code container's `overflow: hidden`.

**Solution**: Position the filename tab outside the main container:

```kotlin
// Wrapper div contains both tab and container
Div(wrapper) {
  // Tab positioned absolutely within wrapper
  filename?.let { /* tab here */ }
  
  // Main container with overflow:hidden
  Div(container) { /* code content */ }
}
```

### 3. Theme Switching Race Conditions

**Problem**: Code highlighting themes weren't switching properly between light/dark modes.

**Solution**: Proper cleanup and sequential loading:

```kotlin
LaunchedEffect(colorMode) {
  // Remove old theme first
  document.getElementById("prism-theme")?.remove()
  
  // Add new theme
  val themeLink = document.createElement("link").apply {
    id = "prism-theme"
    setAttribute("rel", "stylesheet")
    setAttribute("href", getThemeUrl(colorMode))
  }
  document.head?.appendChild(themeLink)
}
```

### 4. Horizontal Rule Styling

**Problem**: Markdown horizontal rules (`---`) had unwanted padding that created excessive spacing.

**Solution**: Added custom CSS styling for `hr` elements:

```kotlin
// Style horizontal rules to be clean without extra padding
ctx.stylesheet.registerStyleBase("hr") {
  Modifier
    .margin(24.px, 0.px)
    .padding(0.px)
    .border(0.px)
    .height(1.px)
    .backgroundColor(Color.currentColor)
    .opacity(0.3)
}
```

## Performance Optimizations

### Font Loading Strategy

- **Preload**: Critical fonts loaded with `rel="preload"`
- **Font Display**: Using `font-display: swap` for graceful fallbacks
- **System Fonts**: Leveraging system fonts for UI elements reduces load time

### Code Highlighting

- **Lazy Loading**: Prism.js autoloader only loads required language modules
- **Theme Caching**: Prevent duplicate theme loading with proper cleanup
- **Minimal Bundle**: Only essential Prism.js components are loaded

### Build Optimizations

```kotlin
// Webpack optimizations for production
tasks.named<KobwebGenerateTask>("kobwebGenerate") {
  minify.set(true)
  sourceMaps.set(false)
}
```

## Deployment with GitHub Actions

The blog is automatically deployed to GitHub Pages using a custom workflow: (following contents are taken from https://github.com/bitspittle/kobweb-ghp-demo/tree/main/.github/workflows)

```yaml
# .github/workflows/export-and-deploy-site.yml
name: Deploy Kobweb site to Pages

on:
  push:
    branches: [master]

jobs:
  export:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - uses: gradle/actions/setup-gradle@v4
      
      - name: Fetch kobweb
        uses: robinraju/release-downloader@v1.10
        with:
          repository: "varabyte/kobweb-cli"
          tag: "v0.9.18"
          fileName: "kobweb-0.9.18.zip"
      
      - name: Run export
        run: |
          cd site
          ../kobweb-0.9.18/bin/kobweb export --notty --layout static
      
      - name: Upload artifact
        uses: actions/upload-pages-artifact@v3
        with:
          path: ./site/.kobweb/site

  deploy:
    needs: export
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to GitHub Pages
        uses: actions/deploy-pages@v4
```

## Final Feature Set

### ✅ Core Blog Features

- [x] Auto-generated blog post listing from markdown files
- [x] Dynamic routing for blog posts
- [x] Frontmatter support (title, description, date, tags)
- [x] SEO-friendly URLs
- [x] Responsive design

### ✅ Professional Code Blocks

- [x] Syntax highlighting for 10+ languages including Kotlin Script
- [x] Filename tabs with elegant positioning
- [x] Interactive copy buttons with visual feedback
- [x] Light/dark theme support
- [x] Hover effects and smooth animations
- [x] JetBrains Mono font for code, system fonts for UI

### ✅ Technical Infrastructure

- [x] Prism.js integration with autoloader
- [x] Dynamic theme switching with localStorage persistence
- [x] Professional typography system
- [x] Optimized font loading strategy
- [x] GitHub Actions deployment pipeline
- [x] Component-based architecture

## Lessons Learned

### 1. Library Selection Matters

The switch from highlight.js to Prism.js was a game-changer. Sometimes the more popular option isn't necessarily the
better fit for your specific use case.

### 2. CSS Positioning Can Be Tricky

Understanding the interaction between `position: absolute`, `overflow: hidden`, and container hierarchies is crucial for
complex layouts like the filename tabs.

### 3. Font Loading Strategy is Critical

A multi-pronged approach (preload + stylesheet + CSS fallbacks) ensures the best user experience across different
browsers and connection speeds.

### 4. Kobweb's Power Lies in Integration

The seamless integration between Kotlin code, Compose-like APIs, and web technologies makes Kobweb a powerful choice for
developers already in the Kotlin ecosystem.

## What's Next?

This blog setup is now production-ready and highly maintainable. Some potential future enhancements:

- Search functionality using client-side indexing
- Tag-based filtering for blog posts
- RSS feed generation for subscribers
- Comments system integration
- Performance analytics and monitoring
- Testing other markdown features like tables, footnotes, math expressions, and embedded media to ensure comprehensive
  markdown support

## Acknowledgments

This project wouldn't have been possible without the incredible work of the [Kobweb team](https://kobweb.varabyte.com/)
and the broader Kotlin community. Special thanks to:

- **[Kobweb Framework](https://kobweb.varabyte.com/)** - For creating such an elegant bridge between Kotlin and web
  development
- **[bitspittle's demo repository](https://github.com/bitspittle/kobweb-ghp-demo)** - For providing excellent examples
  and inspiration for structuring a Kobweb project
- The **Kotlin and Compose** teams for the underlying technology that makes this all possible

## Conclusion

Building this blog with Kobweb has been an incredibly rewarding experience. Despite some initial challenges with version
compatibility and code highlighting, the final result is a professional, performant, and maintainable blogging platform.

The combination of Kotlin's type safety, Compose's declarative UI paradigm, and web technologies creates a powerful
development experience. For Kotlin developers looking to build web applications, Kobweb offers a compelling alternative
to traditional JavaScript frameworks.

The source code for this blog is available on [GitHub](https://github.com/aryapreetam/blog), and I encourage you to
explore it, learn from it, and build upon it.

Happy blogging! 🚀


*This blog post itself is written in Markdown and automatically rendered using the very system it describes. Meta?
Absolutely. Cool? I think so.*