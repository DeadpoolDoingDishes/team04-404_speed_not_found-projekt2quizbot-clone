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
  Add as AddIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

interface FlashcardData {
  id: string;
  question: string;
  answer: string;
  isCorrect: boolean;
}

const Practice: React.FC = () => {
  const [flashcards, setFlashcards] = useState<FlashcardData[]>([]);
  const [allFlashcards, setAllFlashcards] = useState<FlashcardData[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showAnswer, setShowAnswer] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const fetchAllFlashcards = async () => {
    try {
      const response = await axios.get<FlashcardData[]>('http://localhost:8080/api/flashcards');
      setAllFlashcards(response.data);
    } catch (err) {
      console.error('Error fetching all flashcards:', err);
    }
finally {
      setLoading(false);
    }
  };



  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      await fetchAllFlashcards();

    };

    fetchData();
  }, []);

  const handleAnswer = async (isCorrect: boolean) => {
    if (currentFlashcard) {
      try {
        await axios.post('http://localhost:8080/api/flashcards/mark', {
          id: currentFlashcard.id,
          isCorrect,
        });

        // Update the allFlashcards array
        setAllFlashcards(prev =>
          prev.map(card =>
            card.id === currentFlashcard.id
              ? {...card, isCorrect}
              : card
          )
        );

        // Remove the answered flashcard from unpracticed cards
        setFlashcards(prev => prev.filter(card => card.id !== currentFlashcard.id));

        // Reset to first card if we removed the last card
        setCurrentIndex(0);
        setShowAnswer(false);
      } catch (err) {
        console.error('Error marking flashcard:', err);
      }
    }
  };

  const resetPractice = async () => {
    setLoading(true);
    try {
      // Call the reset endpoint
      await axios.post('http://localhost:8080/api/flashcards/reset');

      // Fetch all flashcards again
      await fetchAllFlashcards();
      const response = await axios.get<FlashcardData[]>('http://localhost:8080/api/flashcards');
      setFlashcards(response.data);

      // Reset local state
      setCurrentIndex(0);
      setShowAnswer(false);
    } catch (err) {
      console.error('Error resetting flashcards:', err);
      setError('Failed to reset flashcards. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const currentFlashcard = flashcards[currentIndex];

  // Calculate the correct counts
  const correctCount = allFlashcards.filter(card => card.isCorrect).length;
  const totalCount = allFlashcards.length;

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
      </Box>
    );
  }

  if (flashcards.length === 0) {
    return (
      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          No more cards to practice!
        </Typography>
        <Typography color="text.secondary" gutterBottom>
          You've completed all available flashcards. Great job!
        </Typography>
        <Typography variant="h6" sx={{ mt: 2 }}>
          Correct: {correctCount}/{totalCount}
        </Typography>
        <Box sx={{
          display: 'flex',
          flexDirection: { xs: 'column', sm: 'row' },
          gap: 2,
          justifyContent: 'center',
          mt: 4
        }}>
          <Button
            variant="contained"
            onClick={() => navigate('/generate')}
            startIcon={<AddIcon />}
          >
            Generate New Flashcards
          </Button>
          <Button
            variant="outlined"
            onClick={resetPractice}
            startIcon={<RefreshIcon />}
          >
            Reset All Cards
          </Button>
        </Box>
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
          Correct: {correctCount}/{totalCount}
        </Typography>
        <Typography color="text.secondary">
          Cards remaining: {flashcards.length}
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
          Reset All Cards
        </Button>
      </Box>
    </Box>
  );
};

export default Practice;