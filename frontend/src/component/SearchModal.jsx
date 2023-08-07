import React, { useContext, useState } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import TextField from '@mui/material/TextField';
import List from '@mui/material/List';
import { IconButton, InputAdornment, ListItemButton } from '@mui/material';
import { styled } from '@mui/system'
import SearchIcon from '@mui/icons-material/Search';
import { Api } from '../api'
import LoadingSpinner from './LoadingSpinner'
import { NotificationContext } from '../contexts/NotificationProvider';
import { tryGetErrorMessage } from '../errorParser';

const ModalCustom = styled(Box)({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 300,
  bgcolor: 'background.paper',
  backgroundColor: 'white',
  padding: 20,
  borderRadius: '3%',
  boxShadow: 24
});

export default function BasicModal({ closeFunc, setContacts }) {
  const [open, setOpen] = useState(true);
  const [searchValue, setSearchValue] = useState('');
  const [options, setOptions] = useState([]);
  const [isLoading, setLoading] = useState(false);
  const { setAlert } = useContext(NotificationContext);

  const handleClose = () => {
    setOpen(false);
    closeFunc(false);
  };

  const handleSearch = () => {

    setLoading(true);
    Api.get(`/users/find/${searchValue}`)
      .then((res) => {
        if (res.status === 200) {
          setOptions([...res.data]);
        }
        setLoading(false);
      }).catch((error) => {
        setAlert({ 
          message: tryGetErrorMessage(error), 
          type: 'error'
        });
        setLoading(false);
      })

  };

  const handleOptionClick = index => {
    const selectedOption = options[index];

    setLoading(true);
    Api.get(`/users/retrieve_profile_image/${selectedOption.userName}`, { responseType: 'arraybuffer' })
      .then(res => {
        if (res.status === 200) {
          const image = URL.createObjectURL(new Blob([res.data], { type: 'image/png' }));
          setContacts(prev => [...prev, { ...selectedOption, image }])
          handleClose();
        }
      }).catch((error) => {
        setAlert({ 
          message: tryGetErrorMessage(error), 
          type: 'error'
        });
        setLoading(false);
      })

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
            label="Username"
            variant="outlined"
            value={searchValue}
            onChange={event => setSearchValue(event.target.value)}
            fullWidth
            sx={{ marginTop: 2 }}
            InputProps={{
              endAdornment: (
                <InputAdornment position='end'>
                  <IconButton onClick={handleSearch}>
                    <SearchIcon />
                  </IconButton>
                </InputAdornment>
              )
            }}
          />
          {!isLoading ?
            <List sx={{ marginTop: 2 }}>
              {options.map((option, index) => (
                <ListItemButton
                  key={index}
                  onClick={() => handleOptionClick(index)}
                >
                  <Typography>{option.formalName}</Typography>
                  <Typography sx={{ fontWeight: 'bold', marginLeft: '6px' }}>({option.userName})</Typography>
                </ListItemButton>
              ))}
            </List>
            :
            <LoadingSpinner sx={{ marginTop: 2 }} />
          }
        </ModalCustom>
      </Modal>
    </div>
  );
}
