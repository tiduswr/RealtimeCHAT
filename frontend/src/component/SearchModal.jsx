import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import TextField from '@mui/material/TextField';
import List from '@mui/material/List';
import ListItemText from '@mui/material/ListItemText';
import { IconButton, InputAdornment, ListItemButton } from '@mui/material';
import { styled } from '@mui/system'
import SearchIcon from '@mui/icons-material/Search';

const ModalCustom = styled(Box)({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  backgroundColor: 'white',
  padding: 20,
  borderRadius: '3%',
  boxShadow: 24
});

export default function BasicModal({ closeFunc }) {
  const [open, setOpen] = useState(true);
  const [searchValue, setSearchValue] = useState('');
  const [options, setOptions] = useState(['Option 1', 'Option 2', 'Option 3']);

  const handleClose = () => {
    setOpen(false);
    closeFunc(false);
  };

  const handleSearch = () => {
    // Implement your search logic here
    console.log('Searching for:', searchValue);
    // Update the options list based on the search results
    // For now, let's just filter the options based on the search value
    const filteredOptions = options.filter(option =>
      option.toLowerCase().includes(searchValue.toLowerCase())
    );
    console.log('Filtered options:', filteredOptions);
    // Update the options state
    setOptions(filteredOptions);
  };

  const handleOptionClick = index => {
    const selectedOption = options[index];
    console.log(selectedOption);
    setOpen(false);
    closeFunc(false);
  };

  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <ModalCustom>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Buscar usuário
          </Typography>
          <TextField
            label="Buscar"
            variant="outlined"
            value={searchValue}
            onChange={event => setSearchValue(event.target.value)}
            fullWidth
            sx={{ marginTop: 2 }}
            InputProps={{
                endAdornment: (
                    <InputAdornment position='end'>
                        <IconButton onClick={handleSearch}>
                            <SearchIcon/>
                        </IconButton>
                    </InputAdornment>
                )
            }}
          />
          <List sx={{ marginTop: 2 }}>
            {options.map((option, index) => (
              <ListItemButton
                key={index}
                onClick={() => handleOptionClick(index)}
              >
                <ListItemText primary={option} />
              </ListItemButton>
            ))}
          </List>
        </ModalCustom>
      </Modal>
    </div>
  );
}
