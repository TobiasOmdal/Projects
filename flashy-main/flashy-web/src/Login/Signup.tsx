import {useEffect, useRef, useState} from "react"
import {API} from "../API/API"
import {useNavigate} from "react-router"
import {LoginSignup} from "./LoginSignup"

export const Signup = () => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [passwordConfirm, setPasswordConfirm] = useState("")

    const [formError, setFormError] = useState("")
    const formRef = useRef(document.createElement("form"))
    const passwordRef = useRef(document.createElement("input"))

    const navigate = useNavigate()

    const signup = async () => {
        if (password !== passwordConfirm) {
            setFormError("De to passordene må være like")
            return;
        }

        const pb = API.getProvider()

        const data = {
            username: username,
            password: password,
            passwordConfirm: passwordConfirm,
        }

        try {
            await pb.collection("users").create(data)
            await pb.collection("users").authWithPassword(username, password)
            navigate("/")
        } catch (err: any) {
            setFormError("Brukernavn allerede tatt")
        }
    }

    useEffect(() => {
        setFormError("")
    }, [name, username, password, passwordConfirm])

    return (
        <LoginSignup buttonText="Logg inn istedenfor?" onClick={() => navigate("/login")}>
            <div>
                <form className="spacedForm" ref={formRef}>
                    <h1>Signup</h1>

                    <input
                        type="text"
                        required
                        autoFocus
                        placeholder="Username"
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

                        <input
                            ref={passwordRef}
                            type="password"
                            minLength={5}
                            required
                            placeholder="Bekreft passord"
                            value={passwordConfirm}
                            onChange={(e) => setPasswordConfirm(e.target.value)}
                        />

                    <div className="submitContainer">
                    <input
                        value="Signup"
                        type="submit"
                        onClick={(e) => {
                            e.preventDefault()
                            if (formRef.current.reportValidity()) signup()
                        }}
                    />
                        <div className="error" style={{display: formError.length > 0 ? undefined : "none"}}>
                            <p>{formError}</p>
                        </div>
                    </div>
                </form>
            </div>
        </LoginSignup>
    )
}
