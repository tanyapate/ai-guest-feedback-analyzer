# AI Guest Feedback Analyzer

A Spring Boot REST API that analyzes hotel guest reviews using OpenAI's GPT model. Submit a guest review and get back a structured summary, sentiment, and list of actionable issues.

## Example

**Request:**
```json
POST /api/analyze
{
  "text": "The room was clean but the staff was very rude and check-in took over an hour."
}
```

**Response:**
```
Summary:
The guest found the room clean but experienced significant delays and rude staff during check-in.

Sentiment:
Negative, due to the rudeness of the staff and prolonged check-in time.

Key Issues:
- Staff behavior was perceived as rude
- Check-in process was excessively slow
```

## Tech Stack

- Java 21
- Spring Boot 3
- OpenAI API (GPT-4o)
- Maven

## Getting Started

### Prerequisites
- Java 21
- Maven
- An OpenAI API key with available credits ([platform.openai.com](https://platform.openai.com))

### Setup

1. Clone the repo
```
git clone https://github.com/your-username/ai-guest-feedback-analyzer.git
```

2. Add your OpenAI API key to `src/main/resources/application.properties`
```properties
openai.api.key=your-api-key-here
openai.model=gpt-4o
```

3. Run the app
```
./mvnw spring-boot:run
```

4. Test it with Postman or curl by sending a POST request to:
```
http://localhost:8080/api/analyze
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/analyze` | Analyzes a guest review and returns structured feedback |

## Notes
- Never commit your real API key to GitHub
- See `application-example.properties` for the expected configuration format
