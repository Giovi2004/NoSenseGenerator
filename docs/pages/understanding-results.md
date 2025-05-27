---
title: Understanding the Results
layout: default
parent: Usage
nav_order: 2
---

# Understanding the Results

## Dependency Graph

![Dependency Graph][dependency-graph]

The dependency graph provides a visual representation of the sentence structure:

- **Node Structure**: Each word is represented by a box containing:
  - Top row (red): The dependency relationship with other words
  - Middle row: The actual word from the sentence
  - Bottom row: The part of speech (POS) tag (NOUN, VERB, ADJ, etc.)

- **Arrows and Connections**:
  - Colored arrows show relationships between words
  - Arrow direction indicates dependency flow (from modifier to modified word)
  - Different colors help distinguish multiple relationships

## Toxicity Analysis

![Toxicity Analysis][toxicity-analysis]

**Shows confidence scores for different toxicity categories**

## Tips
- For best results use complete sentences with proper grammar
- You can generate multiple variations by changing the tense

[dependency-graph]: /images/dependency-graph.png
[toxicity-analysis]: /images/toxicity-analysis.png