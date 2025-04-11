import {createContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";


export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const navigate = useNavigate();
    const [auth, setAuth] = useState(() => {
        const accessToken = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");
        const loginId = localStorage.getItem("loginId");
        return {
            accessToken,
            refreshToken,
            loginId,
            isAuthenticated: accessToken ? true : false,
        };
    });

    const login = ({accessToken, refreshToken, loginId}) => {
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);
        localStorage.setItem("loginId", loginId);
        setAuth({ accessToken, refreshToken, loginId, isAuthenticated: true });
    };

    const logout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("loginId");
        setAuth({ accessToken: null, refreshToken: null, loginId: null, isAuthenticated: false });
        navigate("/login");
    };

    return (
        <AuthContext.Provider value={{ auth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}