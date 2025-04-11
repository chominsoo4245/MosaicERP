// src/components/TopBar.jsx
import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext.jsx";
import { refreshTokenAPI } from "../services/AuthService.jsx";

const TOPBAR_REFRESH_INTERVAL = 15 * 60 * 1000; // 15분

export default function TopBar() {
    const { auth, logout, login } = useContext(AuthContext);
    const [remainingTime, setRemainingTime] = useState(TOPBAR_REFRESH_INTERVAL);

    // 타이머: 1초마다 감소
    useEffect(() => {
        const interval = setInterval(() => {
            setRemainingTime((prev) => (prev > 1000 ? prev - 1000 : 0));
        }, 1000);
        return () => clearInterval(interval);
    }, []);

    // 사용자의 행동(클릭, 키 다운) 시 타이머를 리셋
    const resetTimer = () => {
        setRemainingTime(TOPBAR_REFRESH_INTERVAL);
    };

    useEffect(() => {
        window.addEventListener("click", resetTimer);
        window.addEventListener("keydown", resetTimer);
        return () => {
            window.removeEventListener("click", resetTimer);
            window.removeEventListener("keydown", resetTimer);
        };
    }, []);

    // 남은 시간을 mm:ss 형식으로 표시하는 함수
    const formatTime = (ms) => {
        const totalSeconds = Math.floor(ms / 1000);
        const minutes = String(Math.floor(totalSeconds / 60)).padStart(2, "0");
        const seconds = String(totalSeconds % 60).padStart(2, "0");
        return `${minutes}:${seconds}`;
    };

    // refresh 토큰 호출
    const handleRefreshToken = async () => {
        try {
            const accessToken = localStorage.getItem("accessToken");
            const refreshToken = localStorage.getItem("refreshToken");
            const loginId = localStorage.getItem("loginId");

            const result = await refreshTokenAPI({ accessToken, refreshToken }, loginId);
            // result가 null이면 갱신 실패로 간주하고 로그아웃 처리
            if (!result || !result.accessToken) {
                logout();
                return;
            }
            const { accessToken: newAccessToken, refreshToken: newRefreshToken } = result;
            localStorage.setItem("accessToken", newAccessToken);
            localStorage.setItem("refreshToken", newRefreshToken);
            login({ accessToken: newAccessToken, refreshToken: newRefreshToken, loginId });
            resetTimer();
        } catch (error) {
            console.error("토큰 갱신 실패:", error);
            logout();
        }
    };

    return (
        <div className="bg-gray-200 p-4 flex items-center justify-between">
            <div className="text-xl font-bold"></div>
            <div className="flex items-center space-x-4">
                <div>세션 남은 시간: {formatTime(remainingTime)}</div>
                <button
                    onClick={handleRefreshToken}
                    className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 focus:outline-none"
                >
                    토큰 갱신
                </button>
                <div>👤 {auth.loginId || "사용자"}</div>
                <button
                    onClick={logout}
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 focus:outline-none"
                >
                    로그아웃
                </button>
            </div>
        </div>
    );
}