import React from 'react';
import { ListItemAvatar, Avatar } from '@mui/material';

const AvatarWithInitial = ({ senderName, image }) => {
  const renderAvatar = () => {
    if (image) {
      return <Avatar alt={senderName} src={image} />;
    } else {
      const initials = senderName?.charAt(0).toUpperCase();
      return <Avatar>{initials}</Avatar>;
    }
  };

  return <ListItemAvatar>{renderAvatar()}</ListItemAvatar>;
};

export default AvatarWithInitial;