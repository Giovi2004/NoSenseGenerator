---
title: Manual
layout: home
nav_order: 1
permalink: /
---

# No Sense Generator Documentation

- [No Sense Generator Documentation](#no-sense-generator-documentation)
  - [About The Project](#about-the-project)
  - [Used Libraries](#used-libraries)
  - [Used APIs](#used-apis)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
      - [Install Java](#install-java)
      - [Install Maven](#install-maven)
      - [Install Graphviz](#install-graphviz)
      - [Get API Key](#get-api-key)
    - [Configuration](#configuration)
    - [Building](#building)
    - [Running](#running)
    - [Access the application](#access-the-application-in-a-browser-at)
  - [Development](#development)
    - [Testing](#testing)
    - [Documentation](#documentation)
  - [Usage](#usage)
    - [Basic Usage](#basic-usage)
      - [Basic Operations](#basic-operations)
    - [Understanding the Results](#understanding-the-results)
      - [Dependency Graph](#dependency-graph)
      - [Toxicity Analysis](#toxicity-analysis)
      - [Tips](#tips)

## About The Project

Random Nonsense Sentence Generator is a project developed, by [Leonardo Erta](https://github.com/Leo-04-e), [Giovanni Panziera](https://github.com/Giovi2004) and [Iacopo Carretta](https://github.com/itsIaky)  to explore natural language processing and web application development. The application allows users to generate sentences and analyze them using the Google Cloud Natural Language API. It provides features such as sentence structure analysis, nonsense sentence generation, and toxicity detection, all accessible through a simple web interface. The project demonstrates integration of cloud-based NLP services, Java backend development with Spring Boot, and a modern frontend using Bootstrap.

## Used Libraries

| Library | Version | Used Features |
|-------------------|---------|---------------|
| Spring Boot | 3.4.5 | - Web application framework<br>- RESTful endpoints<br>- Template engine integration |
| Bootstrap | 5.3.6 | - Responsive layout<br>- UI components |
| Graphviz Java | 0.18.1 | - .dot file parsing<br>- Rendering graph to image |
| Maven | Latest | - Project build automation<br>- Dependency management<br>- Test reporting |
| Maven Surefire Report Plugin | 3.5.3 | - Generates HTML reports of unit tests|
| Thymeleaf | Latest | - Server-side templating<br>- Dynamic HTML rendering |
| JUnit | Latest | - Unit testing framework<br>- Test reporting |
| Mockito | 5.2.0 | - Mocking framework for unit tests<br>- Enable testing of classes not testable through Junit |
| org.json | 20230618 | - JSON data processing<br>- API response handling |
| Jekyll | Latest | - Documentation site generation<br>- Markdown processing<br>- Documentation templating |

## Used APIs

| Provider | API | Purpose | Features Used |
|----------|-----|---------|---------------|
| Google Cloud | analyzeSyntax | Sentence structure analysis | - Syntactic analysis<br>- Dependency parsing<br>- Part-of-speech tagging |
| Google Cloud | moderateText | Content Moderation | - Toxicity detection |


## Getting Started

Follow these instructions to get a copy of this project up and running on your local machine for development and testing purposes. See [deployment](#deployment) for notes on deploying the project on a live system.

### Prerequisites

#### Install Java
- Install Java (version 21), you can get it [here](https://www.oracle.com/java/technologies/downloads/#java21)
- Or install via package manager:

```shell
# macOS (Homebrew)
brew install openjdk@21

# Ubuntu/Debian
sudo apt-get update
sudo apt-get install openjdk-21-jdk

# Fedora
sudo dnf install java-21-openjdk

# Arch Linux
sudo pacman -S jdk-openjdk
```

#### Install Maven
Install Maven, installation instructions [here](https://maven.apache.org/install.html)

#### Install Graphviz
Install Graphviz (version 12.2.1), installation instructions [here](https://graphviz.org/download/)

#### Get API Key
This project uses Google Cloud Natural Language AI for text analysis. Get an API key [here](https://cloud.google.com/natural-language?hl=en)

### Configuration

After cloning the repository, enter your API key in `application.properties` found in the `src\resources` directory:

```properties
GOOGLE_API_KEY=ENTER YOUR API KEY
```

### Building

To build the project, run in the root folder:
```shell
./mvnw clean package
```

Skip tests during build:
```shell
./mvnw clean package -DskipTests
```

### Running

Start the application:
```shell
./mvnw spring-boot:run
```

Or run the JAR directly (after building):
```shell
java -jar nosense-0.0.1-SNAPSHOT.jar
```

The jar file is found in the `target` directory.

### Access the application in a browser at:
```
http://localhost:8080
```

## Development

### Testing
To generate test reports, run:
```shell
mvn site
```
This generates HTML reports in `target/site` directory. Open `surefire.html` in browser.

View the test report [here](test-report/surefire.html)

### Documentation
Assuming [Jekyll] and [Bundler] are installed on your computer, you can build and preview documentation locally

1. Change directory to pages
2. Run `bundle install`
3. Run `bundle exec jekyll serve`
4. Preview at `localhost:4000`

    The built site is stored in the directory `docs/pages/_site`.

Note: Put images in images folder


## Usage

### Basic Usage
The Random Nonsense Sentence Generator provides a web interface with the following features:

![Home Page][index-screenshot]

#### Basic Operations
1. **Input Sentence Analysis**
   - Enter your sentence in the "Your Sentence" text area
   - Optionally check "Request syntactic tree" to visualize the sentence structure
   - Click "Analyze sentence" to process your input
   - The analysis will show:
     - Extracted nouns
     - Extracted verbs
     - Extracted adjectives
     - Dependency graph (if requested)

2. **Sentence Generation**
   - After analyzing a sentence, click "Generate template" to create a template
   - Choose the tense (Present, Past or Future) from the dropdown
   - Click "Generate sentence" to create a new nonsense sentence based on your input

3. **Additional Features**
   - Click "Save Terms" to store the analyzed words for future use
   - Use "Analyze Toxicity" to check the generated sentence for potentially harmful content

### Understanding the Results

#### Dependency Graph

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

#### Toxicity Analysis

![Toxicity Analysis][toxicity-analysis]

**Shows confidence scores for different toxicity categories**

#### Tips
- For best results use complete sentences with proper grammar
- You can generate multiple variations by changing the tense

[index-screenshot]: images/index-screenshot.png
[dependency-graph]: images/dependency-graph.png
[toxicity-analysis]: images/toxicity-analysis.png
[Jekyll]: https://jekyllrb.com
[Bundler]: https://bundler.io