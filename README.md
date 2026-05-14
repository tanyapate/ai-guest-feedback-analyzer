# AI Guest Feedback Analyzer

A Spring Boot application that analyzes guest feedback with OpenAI, returning sentiment, key themes, and actionable insights from review text. It includes a REST API, configurable OpenAI model settings, tests, and a safe example properties file for local setup.

## Tech Stack

- Java
- Spring Boot
- Maven
- OpenAI API

## Setup

Copy `src/main/resources/application-example.properties` to `src/main/resources/application.properties`, then set your `OPENAI_API_KEY` environment variable.

Run the tests:

```bash
mvn test
```
