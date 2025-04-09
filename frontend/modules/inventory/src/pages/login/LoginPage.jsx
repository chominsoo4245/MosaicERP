import {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import {loginAPI} from "../../services/LoginService.jsx";
import {AuthContext} from "../../context/AuthContext.jsx";

export default function LoginPage() {
    const [loginId, setLoginId] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError('');
        try {
            const data = await loginAPI({loginId, password});
            login({accessToken: data.accessToken, refreshToken: data.refreshToken, loginId});
            navigate('/');
        } catch (err) {
            setError('로그인 실패: ' + (err.response?.data?.message || err.message));
        }
    }
    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <div className="bg-white shadow-md rounded px-8 py-8 w-full max-w-md">
                <h2 className="text-2xl font-bold text-center mb-6">로그인</h2>
                {error && <div className="bg-red-100 text-red-700 p-2 mb-4 rounded">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="loginId">
                            아이디
                        </label>
                        <input
                            type="text"
                            id="loginId"
                            name="loginId"
                            value={loginId}
                            onChange={(e) => setLoginId(e.target.value)}
                            placeholder="아이디 입력"
                            className="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
                            비밀번호
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="비밀번호 입력"
                            className="shadow border rounded w-full py-2 px-3 text-gray-700 mb-3 focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>
                    <div className="flex items-center justify-between">
                        <button
                            type="submit"
                            className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                        >
                            로그인
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}