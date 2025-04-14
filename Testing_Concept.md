# Testing Concept for QuizBot Application

## Backend Testing Concept
### Classes to Test

- AIMLService - External API integration for flashcard generation
- FlashcardService - Core flashcard functionality
- PointsService - Points management
- FlashcardController - REST endpoint for flashcard operations
- PointsController - REST endpoint for points operations

Equivalence Classes for Each Component

#### 1. AIMLService

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
EC9: Count <= 0
EC10: Very large count value


#### 2. FlashcardService

Flashcard Generation

EC1: Successful generation and storage
EC2: Empty result from AIMLService
EC3: Database error during save


Flashcard Marking

EC4: Mark existing flashcard as correct
EC5: Mark existing flashcard as incorrect
EC6: Mark non-existent flashcard
EC7: Database error during update


#### 3. PointsService

Points Retrieval

EC1: Points record exists
EC2: No points record exists (creates new)
EC3: Database error during retrieval


Points Addition

EC4: Adding positive points
EC5: Adding zero points
EC6: Adding negative points
EC7: Database error during update

#### 4. FlashcardController

Generate Flashcards Endpoint

EC1: Valid request body
EC2: Missing required fields
EC3: Invalid field types
EC4: Service exceptions


Get All Flashcards Endpoint

EC5: Database has flashcards
EC6: Empty database
EC7: Service exceptions


Mark Flashcard Endpoint

EC8: Valid request for existing flashcard
EC9: Invalid flashcard ID
EC10: Missing fields in request
EC11: Service exceptions

#### 5. PointsController

Get Points Endpoint

EC1: Points exist
EC2: Points don't exist (creates new)
EC3: Service exceptions


Add Points Endpoint

EC4: Valid request body
EC5: Missing points value
EC6: Invalid points format
EC7: Service exceptions
