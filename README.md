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

# QuizBot
## Welcome to QuizBot!
QuizBot is an interactive learning tool designed to help users learn and retain information through flashcards. The application leverages AI technology to generate flashcards based on user input, making the learning process more efficient and engaging.

## Flashcard Generation
Users can generate flashcards by providing a topic. The application uses an external AI service to create flashcards that include questions and answers. Users can then review these flashcards to enhance their learning experience.

## Points System
QuizBot includes a points system that rewards users for correctly answering flashcard questions. Users can track their progress and see how many points they have accumulated during their learning sessions. The points system encourages users to engage with the flashcards and reinforces their learning.

## User Interface
The user interface is designed to be intuitive and user-friendly. Users can easily navigate through the application, generate flashcards, and track their points. The frontend is built using React, providing a responsive and dynamic experience.

## Usage
1. **Generate Flashcards**: Enter a topic, enter the amount of flashcards you want to generate, and click the "Generate" button. The application will fetch flashcards from the AI service and display them.
2. **Practice Flashcards**: The generated flashcards will be displayed one by one. Users can mark them as correct or incorrect based on their knowledge. Correct answers will earn points.

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
