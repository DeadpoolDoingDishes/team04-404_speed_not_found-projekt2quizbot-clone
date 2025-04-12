# team04-404_speed_not_found-projekt2quizbot

## run backend
```cd backend/```
```gradle wrapper```
```./gradlew build```
```./gradlew bootRun```

## run frontend
```cd frontend/quizbot```
```npm start```
___________________________________________________________________

# UML Diagrams

## Class Diagram
```mermaid
classDiagram
    class FlashcardController {
        -flashcardService
        +generateFlashcards()
        +getAllFlashcards()
        +markFlashcard()
    }
    class PointsController {
        -pointsService
        +getPoints()
        +addPoints()
    }
    class FlashcardService {
        -repository
        -apiService
        -pointsService
        +generateFlashcards()
        +getAllFlashcards()
        +markFlashcard()
    }
    class PointsService {
        -repository
        +getPoints()
        +addPoints()
    }
    class APIService {
        -apiKey
        -httpClient
        +generateFlashcards()
    }
    class APIRequest {
        -url
        -headers
        -body
        +buildRequest()
        +execute()
    }
    class APIResponse {
        -statusCode
        -responseBody
        +parseContent()
        +isSuccessful()
    }
    class Flashcard {
        -id
        -question
        -answer
        -isCorrect
        +getQuestion()
        +getAnswer()
        +isCorrect()
        +setCorrect()
    }
    class Points {
        -id
        -points
        +getPoints()
        +setPoints()
        +addPoints()
    }
    class FlashcardRepository {
        <<interface>>
    }
    class PointsRepository {
        <<interface>>
    }
    FlashcardController --> FlashcardService
    PointsController --> PointsService
    FlashcardService --> FlashcardRepository
    FlashcardService --> APIService: uses
    FlashcardService --> PointsService: uses
    PointsService --> PointsRepository
    FlashcardRepository --> Flashcard
    PointsRepository --> Points
    APIService --> APIRequest
    APIService --> APIResponse: processes
    APIService --> Flashcard: creates
```

## Architecture Diagram
```mermaid
flowchart TB
    subgraph "Client Side"
        browser["Web Browser"]
        react["React Frontend (localhost:3000)"]
    end

    subgraph "Server Side"
        subgraph "Spring Boot Application: localhost:8080"
            controller["Controllers:<br>FlashcardController<br>PointsController"]
            service["Services<br>FlashcardService<br>PointsService<br>APIService"]
            repository["Repositories:<br>FlashcardRepository<br>PointsRepository"]
            config["Configuration:<br>ConfigLoader<br>WebConfig"]
            models["Models<br>Flashcard<br>Points"]
            db[(Database:<br>h2-console)]
        end

        aiml_api["AIML API (api.aimlapi.com)"]
    end

    browser -->|HTTP Requests| react
    react -->|API Calls| controller
    controller -->|Request Processing| service
    service -->|Data Access| repository
    repository -->|ORM| db
    config -->|Configure| controller
    service -->|External API Call| aiml_api
    aiml_api -->|JSON Response| service
```

