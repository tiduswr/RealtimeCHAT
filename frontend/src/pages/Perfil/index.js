import React, { useContext, useRef } from 'react'
import Header from '../../component/Header'
import { Container, Paper, Box, Avatar, IconButton } from '@mui/material'
import { UserContext } from '../../contexts/UserProvider'
import EditIcon from '@mui/icons-material/Edit';
import EditableTextField from '../../component/EditableTextField';
import { Api } from '../../api';
import { AuthContext } from '../../contexts/AuthProvider';

const Perfil = () => {

  const { userImage, userData, updateImage, updateFormalName, userLoading } = useContext(UserContext);
  const { authLoading, setAlert } = useContext(AuthContext);
  const inputRef = useRef(null);
  
  const handleUpdateFormalName = (formalName, setFormalName, setError, setEditing) => {
    if(formalName !== undefined){
      Api.post('/users/edit/formalname', { formalName })
      .then( (res) => {
        if(res.status === 204){
          updateFormalName();
          setEditing(false);
          setError(undefined);
          setFormalName(formalName);
          setAlert({title: 'Atualizado', message: 'Nome atualizado', type: 'success', show: true})
        }
      }).catch( (error) => {
        const fieldError = error.response.data.error.formalName;
        setError(fieldError);
        setEditing(true);
      })
    }
  }
  
  const handleUpdatePassword = (password, setPassword, setError, setEditing) => {
    if(password !== undefined){
      Api.post('/users/edit/password', { password })
      .then( (res) => {
        if(res.status === 204){
          setEditing(false);
          setError(undefined);
          setPassword('');
          setAlert({title: 'Atualizado', message: 'Password atualizado', type: 'success', show: true})
        }
      }).catch( (error) => {
        const fieldError = error.response.data.error.password.replace(',', ', ');
        setError(fieldError);
        setEditing(true);
      })
    }
  }

  const handleUpdateUserImage = () => {
    const file = inputRef.current.files[0];
    
    if(file){
      const formData = new FormData();
      formData.append('image', file);

      Api.post('/users/upload_profile_image', formData)
        .then(res => {
          if(res.status === 200){
            updateImage();
            setAlert({title: 'Atualizado', message: 'Imagem atualizada', type: 'success', show: true})
          }
        }).catch(error => {
          setAlert({title: 'Erro!', message: error.response.data.error.image, type: 'success', show: true})
        })

    }
  }

  if(authLoading || userLoading) return null;

  return (
    <>
      <Header includeChatLink={true} />
      <Container component="main" maxWidth="xs"
        sx={{
          display: 'flex',
          alignItems: 'center'
        }}
      >
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            width: '100%'
          }}
        >
          <Paper
            sx={{
              width: '100%',
              padding: '30px',
              display: 'flex',
              alignItems: 'center',
              flexDirection: 'column'
            }}
          >
            <IconButton
              sx={{
                position: 'relative',
                marginBottom: '-70px',
                top: '-70px'
              }}
            >
              <label htmlFor="image-upload">
                <Avatar
                  src={userImage}
                  sx={{
                    width: 150,
                    height: 150,
                    cursor: 'pointer'
                  }}
                />
                <EditIcon
                  sx={{
                    position: 'absolute',
                    backgroundColor: 'white',
                    bottom: '15%',
                    left: '80%',
                    borderRadius: '50%',
                    border: '2px solid green',
                    padding: '5px',
                    color: 'green'
                  }}
                />
              </label>
              <input
                accept="image/*"
                id="image-upload"
                type="file"
                style={{ display: 'none' }}
                ref={inputRef}
                onChange={handleUpdateUserImage}
              />
            </IconButton>
            <EditableTextField 
              handleUpdate={handleUpdateFormalName} 
              defaultValue={userData?.formalName ? userData.formalName : ''} 
              label='Nome Formal'
            />
            <EditableTextField 
              handleUpdate={handleUpdatePassword} 
              defaultValue={''} 
              label='Password'
              type="password"
            />
          </Paper>
        </Box>
      </Container>
    </>
  )
}

export default Perfil