import React, { useEffect, useState, } from 'react'
import EditIcon from '@mui/icons-material/Edit';
import DoneIcon from '@mui/icons-material/Done';
import { TextField, InputAdornment, IconButton } from '@mui/material'

const EditableTextField = ({ handleUpdate, defaultValue, label, type }) => {

    const [editingName, setEditingName] = useState(false);
    const [value, setValue] = useState('');
    const [error, setError] = useState(undefined);

    useEffect(() => {
        if (defaultValue) setValue(defaultValue)
    }, [defaultValue])

    const handleKeyDown = (event) => {
        if (event.keyCode === 27) {
            setError(undefined);
            setEditingName(false);
            setValue(defaultValue);
        }
    };

    return (
        <TextField
            type={type}
            label={label}
            fullWidth
            onKeyDown={handleKeyDown}
            value={value ? value : ''}
            onChange={e => setValue(e.target.value)}
            disabled={!editingName}
            error={error ? true : false}
            helperText={error ? error : undefined}
            sx={{ marginTop: '25px' }}
            InputProps={{
                endAdornment: (
                    <InputAdornment position="end">
                        <IconButton onClick={e => {
                            if (editingName){
                                handleUpdate(value, setValue, setError, setEditingName);
                            }else{
                                setEditingName(true);
                            }
                        }}>
                            {!editingName ? <EditIcon sx={{ color: 'green' }} /> : <DoneIcon sx={{ color: 'green' }} />}
                        </IconButton>
                    </InputAdornment>
                ),
            }}
        />
    )
}

export default EditableTextField