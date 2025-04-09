import api from './api';

export const loginAPI = async (credentials) => {
    const response = await api.post('/login-service/login', credentials);
    return response.data.data;
}

export const logoutAPI = async (tokens, loginId) => {
    const response = await api.post('/login-service/logout', {...tokens});
    return response.data;
}
