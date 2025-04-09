import {createContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";


export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const navigate = useNavigate();
    const [auth, setAuth] = useState({
       accessToken: localStorage.getItem("accessToken"),
       refreshToken: localStorage.getItem("refreshToken"),
       loginId: localStorage.getItem("loginId"),
       isAuthenticated: false,
    });

    useEffect(() => {
        if (auth.accessToken) {
            setAuth((prev) => ({...prev, isAuthenticated: true}));
        }
    }, [auth.accessToken]);

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