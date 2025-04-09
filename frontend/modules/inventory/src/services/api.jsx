import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://192.168.219.102:8080',
    headers: {
        'Content-Type': 'application/json'
    }
});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

api.interceptors.response.use(
    response => response,
    error => {
        const originalRequest = error.config;
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise(function(resolve, reject) {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers.Authorization = 'Bearer ' + token;
                    return api(originalRequest);
                }).catch(err => {
                    return Promise.reject(err);
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            const refreshToken = localStorage.getItem("refreshToken");
            const loginId = localStorage.getItem("loginId");

            return new Promise(function (resolve, reject) {
                refreshTokenApi({ refreshToken }, loginId)
                    .then(({ accessToken, refreshToken: newRefreshToken }) => {
                        // 저장소 업데이트: 새 토큰을 localStorage에 저장
                        localStorage.setItem("accessToken", accessToken);
                        localStorage.setItem("refreshToken", newRefreshToken);
                        api.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;
                        originalRequest.headers.Authorization = 'Bearer ' + accessToken;
                        processQueue(null, accessToken);
                        resolve(api(originalRequest));
                    })
                    .catch((err) => {
                        processQueue(err, null);
                        reject(err);
                    })
                    .finally(() => {
                        isRefreshing = false;
                    });
            });
        }
        return Promise.reject(error);
    }
);


export default api;