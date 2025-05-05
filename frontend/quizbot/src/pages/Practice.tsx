import React, {useState, useEffect, useCallback} from 'react';
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
  const [unpracticedFlashcards, setUnpracticedFlashcards] = useState<FlashcardData[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showAnswer, setShowAnswer] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();



  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const response = await axios.get<FlashcardData[]>('http://localhost:8080/api/flashcards');
      const allCards = response.data;

      setFlashcards(allCards);

      // Filter out the unpracticed flashcards
      const unpracticed = allCards.filter(card => !card.isCorrect);
      setUnpracticedFlashcards(unpracticed);
    } catch (err) {
      console.error('Error fetching flashcards:', err);
      setError('Failed to load flashcards. Please try again.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
      void fetchData();
  }, [fetchData]);

  const handleAnswer = async (isCorrect: boolean) => {
    if (unpracticedFlashcards.length === 0) return;

    const currentFlashcard = unpracticedFlashcards[currentIndex];

      try {
        await axios.post('http://localhost:8080/api/flashcards/mark', {
          id: currentFlashcard.id,
          isCorrect,
        });

        setFlashcards(prev =>
          prev.map(card =>
            card.id === currentFlashcard.id
              ? {...card, isCorrect}
              : card
          )
        );
        setUnpracticedFlashcards(prev =>
            prev.filter(card => card.id !== currentFlashcard.id)
        );

        setCurrentIndex(index =>
            index >= unpracticedFlashcards.length - 1 ? 0 : index
        );
        setShowAnswer(false);
      } catch (err) {
        console.error('Error marking flashcard:', err);
        setError('Failed to update flashcard. Please try again.');
      }
  };

  const resetPractice = async () => {
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/api/flashcards/reset');

      await fetchData();
      setCurrentIndex(0);
      setShowAnswer(false);
    } catch (err) {
      console.error('Error resetting flashcards:', err);
      setError('Failed to reset flashcards. Please try again.');
    }
  };

  const currentFlashcard = unpracticedFlashcards[currentIndex];

  // Calculate the correct counts
  const correctCount = flashcards.filter(card => card.isCorrect).length;
  const totalCount = flashcards.length;

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
              onClick={fetchData}
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
              onClick={() => navigate('/generate')}
              startIcon={<AddIcon />}
              sx={{ mt: 2 }}
          >
            Generate Flashcards
          </Button>
        </Box>
    );
  }
  if (unpracticedFlashcards.length === 0) {
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

        </Box>

        <Box sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          mb: 2
        }}>

          <Typography color="text.secondary">
            Cards remaining: {unpracticedFlashcards.length}
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