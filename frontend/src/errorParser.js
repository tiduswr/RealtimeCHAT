const tryGetErrorMessage = (error, defaultMessage = 'Não foi possivel recuperar os detalhes do erro.') => {
    return error.response?.data?.message ? error.response.data.message 
        : (error?.message ? error.message : defaultMessage)
}

export { tryGetErrorMessage };