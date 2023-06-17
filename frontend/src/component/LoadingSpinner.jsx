import React from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';

const LoadingSpinner = ({ sx, iconSx }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100%', width: 'auto',
        ...sx
      }}
    >
      <CircularProgress sx={{...iconSx}}/>
    </Box>
  );
};

export default LoadingSpinner;
