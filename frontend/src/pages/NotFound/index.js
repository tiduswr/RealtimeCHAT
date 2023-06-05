import React from 'react';
import { Box, Typography } from '@mui/material';
import PublicHeader from '../../component/PublicHeader';

export default function NotFound() {
  return (
    <>
      <PublicHeader/>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          flexDirection: 'column',
          minHeight: '70vh'
        }}
      >
        <Typography variant="h1" style={{ color: 'blue' }}>
          404
        </Typography>
        <Typography variant="h6">
          Essa página não existe.
        </Typography>
      </Box>
    </>
  );
}