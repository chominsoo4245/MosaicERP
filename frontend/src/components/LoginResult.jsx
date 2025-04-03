import {useEffect, useState} from "react";

export default function LoginResult() {
    const [result, setResult] = useState(null);

    useEffect(() => {
        async function login() {
            try {
                const res = await fetch("http://localhost:8080/auth/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        loginId: "admin",
                        password: "1234"
                    })
                });

                if (!res.ok) {
                    throw new Error("Login failed");
                }

                const data = await res.json();
                setResult(data);
            } catch (err) {
                setResult({ error: err.message });
            }
        }

        login();
    }, []);

    return (
        <div>
            <h1>Login Result</h1>
            <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
    );
}