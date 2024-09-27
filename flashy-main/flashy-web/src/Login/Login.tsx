import {useEffect, useRef, useState} from "react"
import {API} from "../API/API"
import {useNavigate} from "react-router"
import {LoginSignup} from "./LoginSignup"

export const Login = () => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")

    // Uses refs to manually do validation on objects
    const formRef = useRef(document.createElement("form"))
    const passwordRef = useRef(document.createElement("input"))

    const navigate = useNavigate()

    const login = async () => {
        const pb = API.getProvider()
        try {
            await pb.collection("users").authWithPassword(username, password)
            navigate("/")
        } catch (error) {
            setError("Feil brukernavn/passord")
        }
    }

    useEffect(() => {
        setError("")
    }, [username, password])

    return (
        <LoginSignup buttonText="Lag ny bruker istedenfor?" onClick={() => navigate("/signup")}>
            <div>
                <form className="spacedForm" ref={formRef}>
                    <h1 className="loginTitle">Logg inn</h1>

                    <input
                        type="text"
                        autoFocus
                        required
                        placeholder="Brukernavn"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <input
                        ref={passwordRef}
                        type="password"
                        minLength={5}
                        required
                        placeholder="Passord"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <div className="submitContainer">
                        <input
                            className="loginButton"
                            type="submit"
                            value="Logg inn"
                            onClick={(e) => {
                                e.preventDefault()
                                if (formRef.current.reportValidity()) login()
                            }}
                        />

                        <div className="error" style={{display: error.length > 0 ? undefined : "none"}}>
                            <p>{error}</p>
                        </div>
                    </div>
                </form>
            </div>
        </LoginSignup>
    )
}
