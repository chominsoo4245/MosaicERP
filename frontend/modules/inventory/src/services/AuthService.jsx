import api from "./api.jsx";

export const refreshToken = async (tokens, loginId) => {
    const response = await api.post('/auth/refresh', tokens, {
        headers: {
            "X-User-Id": loginId
        }
    });
    return response.data.data;
};