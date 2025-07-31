# Preetam's Technical Blog

A modern, professional technical blog built with [Kobweb](https://github.com/varabyte/kobweb) - showcasing the power of
Kotlin for web development.

🌐 **Live Site**: [https://aryapreetam.github.io](https://aryapreetam.github.io)

## ✨ Features

### 🚀 **Dynamic Blog System**

- **Auto-discovery**: Blog posts automatically appear on the home page when markdown files are added
- **Frontmatter Support**: Rich metadata (title, description, date, tags) for each post
- **SEO-Friendly URLs**: Clean, readable URLs for all blog posts
- **User-Friendly Dates**: Displays dates as "Jan 31, 2025" instead of "2025-01-31"

### 💻 **Professional Code Highlighting**

- **Syntax Highlighting**: 10+ languages including Kotlin Script (.kts)
- **Interactive Features**: Copy buttons with visual feedback
- **Filename Tabs**: Elegant tabs showing file names
- **Theme Integration**: Seamlessly switches between light/dark themes
- **Typography**: JetBrains Mono for code, system fonts for UI

### 🎨 **Modern Design System**

- **Dynamic Theming**: Light/dark mode with localStorage persistence
- **Responsive Design**: Optimized for all screen sizes
- **Social Integration**: Custom SVG icons with hover effects
- **Professional Typography**: Optimized font loading and fallback chains

### ⚡ **Performance & Deployment**

- **Static Export**: Optimized for GitHub Pages
- **GitHub Actions**: Automated CI/CD pipeline
- **Build Optimization**: Minified assets and caching strategies
- **Font Preloading**: Fast loading with graceful fallbacks

## 🛠️ Technology Stack

- **Framework**: [Kobweb](https://kobweb.varabyte.com/) (Kotlin + Compose for Web)
- **Styling**: CSS-in-Kotlin with theme system
- **Code Highlighting**: Prism.js with autoloader
- **Typography**: JetBrains Mono + System fonts
- **Deployment**: GitHub Pages with Actions
- **Build System**: Gradle with custom tasks

## 📁 Project Structure

```
blog/
├── site/
│   ├── src/jsMain/kotlin/dev/aryapreetam/
│   │   ├── components/
│   │   │   ├── layouts/
│   │   │   │   ├── PageLayout.kt           # Main layout with theme toggle
│   │   │   │   └── MarkdownLayout.kt       # Markdown-specific styling
│   │   │   └── widgets/
│   │   │       ├── code/CodeBlock.kt       # Professional code highlighting
│   │   │       ├── SocialIcons.kt          # Social media integration
│   │   │       └── blog/ArticleList.kt     # Blog post listing
│   │   ├── pages/
│   │   │   ├── Index.kt                    # Dynamic home page
│   │   │   └── blog/GeneratedBlogData.kt   # Auto-generated blog data
│   │   └── resources/markdown/             # Blog posts (Markdown)
│   ├── build.gradle.kts                    # Build config with blog discovery
│   └── .kobweb/conf.yaml                   # Kobweb configuration
├── .github/workflows/
│   └── export-and-deploy-site.yml          # GitHub Pages deployment
└── dev-logs/                               # Development documentation
    └── how-to-blog-setup.md                # Complete technical guide
```

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (JDK)
- **Git**

### Local Development

1. **Clone the repository**:
   ```bash
   git clone https://github.com/aryapreetam/blog.git
   cd blog
   ```

2. **Start the development server**:
   ```bash
   cd site
   kobweb run
   ```

3. **Open your browser**:
   Navigate to [http://localhost:8080](http://localhost:8080)

4. **Start writing**:
   Add new blog posts by creating `.md` files in `site/src/jsMain/resources/markdown/`

### Adding New Blog Posts

Create a new markdown file in `site/src/jsMain/resources/markdown/` with frontmatter:

```markdown
---
title: "Your Post Title"
description: "A brief description of your post"
date: "2025-01-31"
tags: ["kotlin", "web-development"]
draft: false
layout: "dev.aryapreetam.components.layouts.MarkdownLayout"
---

# Your Post Title

Your content here...
```

The post will automatically appear on the home page after the next build!

## 🎯 Key Features Deep Dive

### Dynamic Blog Discovery

Unlike static site generators, this blog uses a custom Gradle task to automatically discover and generate metadata for
all markdown files:

```kotlin
// Auto-generated from markdown files
@Page
@Composable
fun HomePage() {
    ArticleList(GeneratedBlogData.entries) // ← Automatically updated
}
```

### Advanced Code Blocks

Professional code highlighting with interactive features:

```kotlin
// Example with filename tab and copy button
```kotlin:example.kt
fun main() {
    println("Hello, Kobweb!")
}
```

### Theme System

Seamless light/dark mode switching with proper persistence:

- **Theme Toggle**: Fixed position toggle in top-right
- **Code Highlighting**: Automatically switches themes (Prism vs Prism Okaidia)
- **Persistence**: Remembers your preference using localStorage

## 📚 Blog Posts

The blog features comprehensive technical content including:

1. **[Building This Blog with Kobweb](https://aryapreetam.github.io/markdown/BuildingThisBlogWithKobweb)** - Complete
   technical journey
2. **[Building Components with Silk](https://aryapreetam.github.io/markdown/SecondPost)** - UI component development
3. **[First Post](https://aryapreetam.github.io/markdown/FirstPost)** - Welcome and introduction

## 🔧 Development

### Available Commands

```bash
# Development server with live reload
kobweb run

# Export for production
kobweb export --layout static

# Run exported site locally
kobweb run --env prod

# Generate blog metadata (automatic during build)
./gradlew generateBlogData
```

### Customization

#### Adding New Components

1. Create components in `site/src/jsMain/kotlin/dev/aryapreetam/components/`
2. Follow Kobweb/Compose patterns
3. Use the theme system for consistent styling

#### Modifying Styles

- **Global styles**: Update `PageLayout.kt` or `MarkdownLayout.kt`
- **Component styles**: Use CSS-in-Kotlin with ColorMode
- **Code highlighting**: Customize Prism.js themes

#### Build Customization

- **Blog discovery**: Modify `generateBlogData` task in `build.gradle.kts`
- **Optimization**: Adjust webpack settings in build configuration

## 🚢 Deployment

### Automatic Deployment (GitHub Pages)

The blog automatically deploys to GitHub Pages when you push to the `main` branch:

1. **Repository Setup**: Name your repo `[username].github.io`
2. **GitHub Pages**: Enable Pages with "GitHub Actions" source
3. **Push Changes**: Automatic deployment via GitHub Actions

### Manual Deployment

```bash
# Export the site
cd site
kobweb export --layout static

# Deploy the contents of site/.kobweb/site/ to your hosting provider
```

## 📈 Performance

- **Lighthouse Score**: 95+ on all metrics
- **Font Loading**: Optimized with preload and fallbacks
- **Code Highlighting**: Lazy-loaded languages
- **Static Export**: Fast loading with minimal JavaScript
- **Responsive Images**: Optimized for all screen sizes

## 🤝 Contributing

This blog serves as both a personal blog and a demonstration of Kobweb's capabilities. Feel free to:

- **Fork the repository** to create your own blog
- **Open issues** for bugs or suggestions
- **Submit PRs** for improvements
- **Star the repo** if you find it useful!

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- **[Kobweb Team](https://kobweb.varabyte.com/)** - For creating an excellent Kotlin web framework
- **[bitspittle](https://github.com/bitspittle/kobweb-ghp-demo)** - For inspiration and GitHub Pages deployment setup
- **[Prism.js](https://prismjs.com/)** - For reliable code syntax highlighting
- **Kotlin & Compose Teams** - For the underlying technology

## 📞 Connect

- **GitHub**: [@aryapreetam](https://github.com/aryapreetam)
- **LinkedIn**: [preetambhosle](https://www.linkedin.com/in/preetambhosle)
- **X (Twitter)**: [@preetambhosle](https://x.com/preetambhosle)
- **Email**: [preetb123@gmail.com](mailto:preetb123@gmail.com)

---