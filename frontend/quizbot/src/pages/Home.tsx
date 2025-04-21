import { useNavigate } from 'react-router-dom';
import Logo from "../assets/logo.png";
import {
    Box,
    Button,
    Card,
    CardContent,
    Typography,
} from '@mui/material';
import {
    Create as CreateIcon,
    School as SchoolIcon,

} from '@mui/icons-material';

const Home = () => {
    const navigate = useNavigate();
    return (
        <Box>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'row',   // ← row layout
                    alignItems: 'center',   // ← vertical centering
                    justifyContent: 'center', // ← horizontal centering
                    p: 2,
                }}
            >
                <Box
                    component="img"     // ← must be the tag name
                    src={Logo}          // ← your imported URL
                    alt="QuizBot logo"
                    sx={{
                        mr: 2,
                        width: 100,
                        height: 100,
                        objectFit: 'contain',
                    }}
                ></Box>
                <Typography variant="h1" component="h1" gutterBottom align="center" marginBottom={0}>
                    QuizBot
                </Typography>

            </Box>

            <Box sx={{
                display: 'flex',
                flexDirection: { xs: 'column', md: 'row' },
                gap: 4,
                mt: 4,
                '& > *': {
                    flex: { xs: '1 1 100%', md: '1 1 50%' }
                }
            }}>
                <Card sx={{ borderRadius: 9}}>
                    <CardContent sx={{ textAlign: 'center', py: 4 }}>
                        <CreateIcon sx={{ fontSize: 48, mb: 2, color: 'primary.main' }} />
                        <Typography variant="h5" gutterBottom>
                            Generate Flashcards
                        </Typography>
                        <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                            Create custom flashcards on any topic using AI technology
                        </Typography>
                        <Button
                            variant="contained"
                            size="large"
                            onClick={() => navigate('/generate')}
                        >
                            Get Started
                        </Button>
                    </CardContent>
                </Card>
                <Card sx={{ borderRadius: 9}}>
                    <CardContent sx={{ textAlign: 'center', py: 4 }}>
                        <SchoolIcon sx={{ fontSize: 48, mb: 2, color: 'secondary.main' }} />
                        <Typography variant="h5" gutterBottom>
                            Practice Mode
                        </Typography>
                        <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                            Review your flashcards and track your progress
                        </Typography>
                        <Button
                            variant="contained"
                            color="secondary"
                            size="large"
                            onClick={() => navigate('/practice')}
                        >
                            Start Learning
                        </Button>
                    </CardContent>
                </Card>
            </Box>
        </Box>
    );
};

export default Home;