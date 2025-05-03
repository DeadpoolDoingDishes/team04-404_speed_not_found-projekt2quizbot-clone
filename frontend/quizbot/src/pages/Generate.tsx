import React, { useState } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
  SelectChangeEvent,
} from '@mui/material';

interface FlashcardData {
  id: string;
  question: string;
  answer: string;
  isCorrect: boolean;
}

const Generate = () => {
  const [topic, setTopic] = useState('');
  const [language, setLanguage] = useState('EN');
  const [count, setCount] = useState(3);
  const [loading, setLoading] = useState(false);
  const [flashcards, setFlashcards] = useState<FlashcardData[]>([]);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (topic.trim() === '') {
        setError('Please enter a topic.');
        return;
    }
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/flashcards/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          topic,
          language,
          count,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to generate flashcards');
      }

      const data: FlashcardData[] = await response.json();
      setFlashcards(data);
    } catch (err) {
      setError('Failed to generate flashcards. Please try again.');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };


  const handleLanguageChange = (e: SelectChangeEvent) => {
    setLanguage(e.target.value);
  };

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Generate Flashcards
      </Typography>
      <form onSubmit={handleSubmit}>
        <Box sx={{ 
          display: 'flex', 
          flexDirection: 'column',
          gap: 3
        }}>
          <TextField
            fullWidth
            label="Topic"
            value={topic}
            onChange={(e) => setTopic(e.target.value)}
            required
            slotProps={{ htmlInput: {maxLength: 50}}}
          />
          <Box sx={{ 
            display: 'flex', 
            flexDirection: { xs: 'column', sm: 'row' },
            gap: 3
          }}>
            <FormControl fullWidth>
              <InputLabel>Language</InputLabel>
              <Select
                value={language}
                onChange={handleLanguageChange}
                label="Language"
              >
                <MenuItem value="EN">English</MenuItem>
                <MenuItem value="DE">German</MenuItem>
              </Select>
            </FormControl>
            <TextField
              fullWidth
              type="number"
              label="Number of Flashcards"
              value={count}
              onChange={(e) => setCount(parseInt(e.target.value))}
              required
              inputProps={{ min: 1, max: 20 }}
            />
          </Box>
          <Button
            type="submit"
            variant="contained"
            fullWidth
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : 'Generate Flashcards'}
          </Button>
        </Box>
      </form>

      {error && (
        <Typography color="error" sx={{ mt: 2 }}>
          {error}
        </Typography>
      )}

      {flashcards.length > 0 && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h5" gutterBottom>
            Generated Flashcards
          </Typography>
          <Box sx={{ 
            display: 'flex', 
            flexDirection: 'column',
            gap: 2
          }}>
            {flashcards.map((flashcard) => (
              <Card key={flashcard.id}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    {flashcard.question}
                  </Typography>
                  <Typography color="text.secondary">
                    {flashcard.answer}
                  </Typography>
                </CardContent>
              </Card>
            ))}
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default Generate; 