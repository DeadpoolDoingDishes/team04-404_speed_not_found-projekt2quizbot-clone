# Testing Concept for QuizBot Application

## Backend Testing Concept
### Classes to Test

- APIService - External API integration for flashcard generation
- FlashcardService - Core flashcard functionality
- PointsService - Points management
- FlashcardController - REST endpoint for flashcard operations
- PointsController - REST endpoint for points operations

Equivalence Classes for Each Component

#### 1. APIService

API Integration

EC1: Successful API response with valid JSON

EC2: API error response (4xx/5xx)

EC3: Network failure

EC4: Invalid JSON in response

EC5: Response without required fields


Input Parameters

EC6: Valid topic, language, and count

EC7: Empty topic

EC8: Unsupported language


#### 2. FlashcardService

Flashcard Generation

EC1: Successful generation and storage

EC2: Empty result from AIMLService


Flashcard Marking

EC3: Mark existing flashcard as correct 

EC4: Mark existing flashcard as incorrect


#### 3. PointsService

Points Retrieval

EC1: Points record exists

EC2: No points record exists (creates new)

Points Addition

EC4: Adding positive points

EC5: Adding zero points

EC6: Adding negative points


#### 4. FlashcardController

Generate Flashcards Endpoint

EC1: Valid request body

EC2: Missing required fields

EC3: Invalid field types

EC4: Service exceptions

Get All Flashcards Endpoint

EC5: Service exceptions


Mark Flashcard Endpoint

EC6: Valid request for existing flashcard

EC7: Invalid flashcard ID

EC8: Missing fields in request

EC9: Service exceptions

#### 5. PointsController

Get Points Endpoint

EC1: Points exist

EC2: Points don't exist (creates new)

EC3: Service exceptions


Add Points Endpoint

EC4: Valid request body

EC5: Missing points value

EC6: Service exceptions
