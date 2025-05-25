<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
***
*** Readme created with https://github.com/othneildrew/Best-README-Template
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
<h3 align="center">Random Nonsense Sentence Generator</h3>

  <p align="center">
    This is a web server built in Java that lets you generate and analyze sentences via Google Cloud Natural Language. <a href="https://cloud.google.com/natural-language/docs/">Learn more</a>
    <br />
    <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/github_username/repo_name">View Demo</a>
    &middot;
    <a href="https://github.com/github_username/repo_name/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    &middot;
    <a href="https://github.com/github_username/repo_name/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Random Nonsense Sentence Generator is a project developed, by [Leonardo Erta](https://github.com/Leo-04-e), [Giovanni Panziera](https://github.com/Giovi2004) and [Iacopo Carretta](https://github.com/itsIaky)  to explore natural language processing and web application development. The application allows users to generate and analyze sentences using the Google Cloud Natural Language API. It provides features such as sentence structure analysis, nonsense sentence generation, and toxicity detection, all accessible through a simple web interface. The project demonstrates integration of cloud-based NLP services, Java backend development with Spring Boot, and a modern frontend using Bootstrap.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![Java][Java]][Java-url]
* [![Bootstrap][Bootstrap]][Bootstrap-url]
* [![Spring Boot][Spring-Boot]][Spring-Boot-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

These instructions will give you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on deploying the project on a live system.



### Prerequisites

1. Install Java (version 21), you can get it [here](https://www.oracle.com/java/technologies/downloads/#java21).

* Or you can install via your package manager:
 
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

2. Install graphviz, you can find the installation instructions [here](https://graphviz.org/download/).



### Installation

1. Get an API Key at [Google Cloud](https://cloud.google.com/natural-language?hl=en)
2. Clone the repo
   ```shell
   git clone https://github.com/Giovi2004/NoSenseGenerator.git
   ```
3. Enter your API in `application.properties`
   ```properties
   GOOGLE_API_KEY= = 'ENTER YOUR API';
   ```
4. Build the project (if not already built)
    ``` shell
    ./mvnw clean package
    ```
    1. Skip tests execution during build
        ``` shell
        ./mvnw clean package -DskipTests
        ```
    2. Or to skip test compilation and execution
        ``` shell
        ./mvnw clean package -Dmaven.test.skip=true
        ```
5. Run the application
    ``` shell
    ./mvnw spring-boot:run
    ```
    1. If you prefer to run the JAR directly (after building)
        ``` shell
        java -jar target/nosense.jar
        ```



### Development

For the tests report run in the root folder of the project
   ``` shell
   mvn site
   ```

This will generate HTML reports in `target/site directory`. Open the `surefire-report.html` file in the browser. You will get an output like below image.

![Test Report HTML][test-screenshot]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

This is the main page where you can interact with Google APIs to analyze, and generate nonsense sentences. Follow the steps below to use the user interface:

![Home Page Screen Shot][index-screenshot]

**Example usage:**

1. Start the server:
    ```shell
    ./mvnw spring-boot:run
    ```
2. Open your browser and go to [http://localhost:8080](http://localhost:8080).
3. Enter a sentence in the "Your Sentence" box and click **Analyze sentence**.
4. Generate a template or a new nonsense sentence using the provided buttons.
5. Optionally, analyze the toxicity of the generated sentence.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

This software uses the following packages:

* [Google Cloud Natural Language API](https://cloud.google.com/natural-language)
* [Graphviz](https://graphviz.org/)
* [Graphviz-java](https://github.com/nidi3/graphviz-java)
* [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Bootstrap](https://getbootstrap.com/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt

[product-screenshot]: images/screenshot.png

[Bootstrap]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com

[Java]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.oracle.com/java/

[Spring-Boot]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Spring-Boot-url]: https://spring.io/

[index-screenshot]: images/index-screenshot.png
[test-screenshot]: images/test-screenshot.png