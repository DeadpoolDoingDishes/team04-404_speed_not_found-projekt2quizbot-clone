import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Typography,
} from '@mui/material';
import {
  Refresh as RefreshIcon,
} from '@mui/icons-material';

interface FlashcardData {
  id: string;
  question: string;
  answer: string;
  isCorrect: boolean;
}

interface PointsResponse {
  points: number;
}

const Practice = () => {
  const [flashcards, setFlashcards] = useState<FlashcardData[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showAnswer, setShowAnswer] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [points, setPoints] = useState(0);

  const fetchFlashcards = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/flashcards');
      if (!response.ok) {
        throw new Error('Failed to load flashcards');
      }
      const data: FlashcardData[] = await response.json();
      setFlashcards(data);
    } catch (err) {
      setError('Failed to load flashcards. Please try again.');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };


  const fetchPoints = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/points');
      if (!response.ok) {
        throw new Error('Failed to fetch points');
      }
      const data: PointsResponse = await response.json();
      setPoints(data.points);
    } catch (err) {
      console.error('Error fetching points:', err);
    }
  };


  useEffect(() => {
    fetchFlashcards();
    fetchPoints();
  }, []);

  const handleAnswer = async (isCorrect: boolean) => {
    if (currentFlashcard) {
      try {
        await fetch('http://localhost:8080/api/flashcards/mark', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            id: currentFlashcard.id,
            isCorrect,
          }),
        });
        await fetchPoints();
        nextCard();
      } catch (err) {
        console.error('Error marking flashcard:', err);
      }
    }
  };


  const nextCard = () => {
    setShowAnswer(false);
    setCurrentIndex((prevIndex) => (prevIndex + 1) % flashcards.length);
  };

  const resetPractice = () => {
    setCurrentIndex(0);
    setShowAnswer(false);
    fetchFlashcards();
  };

  const currentFlashcard = flashcards[currentIndex];

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ mt: 4 }}>
        <Typography color="error">{error}</Typography>
        <Button
          variant="contained"
          onClick={fetchFlashcards}
          startIcon={<RefreshIcon />}
          sx={{ mt: 2 }}
        >
          Retry
        </Button>
      </Box>
    );
  }

  if (flashcards.length === 0) {
    return (
      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          No flashcards available
        </Typography>
        <Typography color="text.secondary" gutterBottom>
          Generate some flashcards first to start practicing
        </Typography>
        <Button
          variant="contained"
          onClick={fetchFlashcards}
          startIcon={<RefreshIcon />}
          sx={{ mt: 2 }}
        >
          Refresh
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ 
        display: 'flex', 
        flexDirection: 'column',
        gap: 2,
        mb: 4
      }}>
        <Typography variant="h4" component="h1">
          Practice Mode
        </Typography>
        <Typography variant="h6">
          Points: {points}
        </Typography>
      </Box>
      
      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            {currentFlashcard.question}
          </Typography>
          {showAnswer && (
            <Typography variant="body1" color="text.secondary">
              {currentFlashcard.answer}
            </Typography>
          )}
        </CardContent>
      </Card>

      <Box sx={{ 
        display: 'flex', 
        flexDirection: 'column',
        gap: 2,
        mt: 2
      }}>
        {!showAnswer ? (
          <Button
            variant="contained"
            fullWidth
            onClick={() => setShowAnswer(true)}
          >
            Show Answer
          </Button>
        ) : (
          <Box sx={{ 
            display: 'flex', 
            gap: 2
          }}>
            <Button
              variant="contained"
              color="error"
              fullWidth
              onClick={() => handleAnswer(false)}
            >
              Incorrect
            </Button>
            <Button
              variant="contained"
              color="success"
              fullWidth
              onClick={() => handleAnswer(true)}
            >
              Correct
            </Button>
          </Box>
        )}
        <Button
          variant="outlined"
          fullWidth
          onClick={resetPractice}
        >
          Reset Practice
        </Button>
      </Box>
    </Box>
  );
};

export default Practice; 