# team04-404_speed_not_found-projekt2quizbot

___________________________________________________________________

# Running the application locally
## run backend
```cd backend/```
```gradle wrapper```
```./gradlew build```
```./gradlew bootRun```

## run frontend
```cd frontend/quizbot```
```npm install```
```npm start```

## open in browser
```http://localhost:3000```
___________________________________________________________________

# UML Diagrams

## Class Diagram
```mermaid
classDiagram

    class FlashcardController {
        -flashcardService
        +generateFlashcards() ResponseEntity<List<Flashcard>>
        +getAllFlashcards() ResponseEntity<List<Flashcard>>
        +markFlashcard() ResponseEntity<Void>
        +getPoints() ResponseEntity<Map<String, Integer>>
        +resetFlashcards() ResponseEntity<List<Flashcard>>
    }
    class FlashcardService {
        -repository FlashcardRepository
        -apiService APIService
        +generateAndSaveFlashcards() List<Flashcard>
        +getAllFlashcards() List<Flashcard>
        +markFlashcard()
        +calculatePoints() int
        +resetAllFlashcards() List<Flashcard>
    }

    class APIService {
        -__LOGGER__: Logger <<final>>
        -__AIML_CHAT_COMPLETIONS_URL__: string <<final>>
        -__CONTENT__: string <<final>>
        -apiKey string 
        -httpClient <<final>>
        +generateFlashcards() List<Flashcard>
        -createApiRequest() Request
        -extractFlashcardsFromResponse() List<Flashcard>
        -extractJsonArray() string
        -getLanguageName() string
    }

    class Flashcard {
        -id String
        -question String
        -answer String
        -isCorrect boolean
        +getId() String
        +getQuestion() String
        +getAnswer() String
        +isCorrect() boolean
        +setId()
        +setCorrect()
        +toString() String
    }

    class FlashcardRepository {
        <<interface>>
    }

    FlashcardController --> FlashcardService
    FlashcardService --> FlashcardRepository
    FlashcardService --> APIService: uses
    FlashcardRepository --> Flashcard
    APIService ..> Flashcard: creates
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
            service["Services<br>FlashcardService<br>APIService"]
            repository["Repositories:<br>FlashcardRepository"]
            config["Configuration:<br>WebConfig"]
            models["Models<br>Flashcard"]
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
Testing Concept for QuizBot Application

[-> Testing Concept](Testing_Concept.md)
