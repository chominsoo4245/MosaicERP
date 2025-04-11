import axios from "axios";
import {refreshTokenAPI} from "./AuthService.jsx";

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

            const accessToken = localStorage.getItem("accessToken");
            const refreshToken = localStorage.getItem("refreshToken");
            const loginId = localStorage.getItem("loginId");

            return new Promise(function (resolve, reject) {
                refreshTokenAPI({ accessToken, refreshToken }, loginId)
                    .then((result) => {
                        // 만약 result가 null이면 갱신 실패라고 판단하고, 로그아웃 처리
                        if (!result || !result.newAccessToken) {
                            // AuthContext의 logout()을 호출하거나, 직접 localStorage를 정리하고 리다이렉트
                            // 예시로 직접 logout 처리:
                            localStorage.removeItem("accessToken");
                            localStorage.removeItem("refreshToken");
                            localStorage.removeItem("loginId");
                            window.location.href = "/login"; // 로그인 페이지로 리다이렉트
                            return Promise.reject(new Error("Refresh token expired. Logging out."));
                        }
                        const { newAccessToken, refreshToken: newRefreshToken } = result;
                        localStorage.setItem("accessToken", newAccessToken);
                        localStorage.setItem("refreshToken", newRefreshToken);
                        api.defaults.headers.common['Authorization'] = 'Bearer ' + newAccessToken;
                        originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;
                        processQueue(null, newAccessToken);
                        resolve(api(originalRequest));
                    })
                    .catch((err) => {
                        processQueue(err, null);
                        // 실패 시에도 로그아웃 처리
                        localStorage.removeItem("accessToken");
                        localStorage.removeItem("refreshToken");
                        localStorage.removeItem("loginId");
                        window.location.href = "/login";
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