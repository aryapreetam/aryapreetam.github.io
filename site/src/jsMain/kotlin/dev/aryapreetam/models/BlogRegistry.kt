package dev.aryapreetam.models

data class BlogPostMetadata(
  val slug: String,
  val title: String,
  val description: String,
  val date: String,
  val tags: List<String>,
  val route: String
)