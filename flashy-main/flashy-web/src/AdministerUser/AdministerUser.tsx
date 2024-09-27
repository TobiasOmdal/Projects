import styles from "../styles/AdministerUser.module.css"
import {API} from "../API/API"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router"
import {Header} from "../Header/Header"

export const AdministerUser = () => {
    const [username, setUsername] = useState("")
    const navigate = useNavigate()
    const {userID} = useParams()
    const [isAdmin, setIsAdmin] = useState(false) 

    useEffect(() => {
        setName();
    }, [])

    const setName = async () => {
        const pb = API.getProvider()
        if (userID) {
            const user = await pb.collection("users").getOne(userID);
            setUsername(user.username);
        }
        else 
            setUsername(pb.authStore.model?.username ?? "")
    }

    const ChangeUserName = async () => {
        const newUsername: string | null = prompt("Skriv inn nytt brukernavn")

        if (!ValidateInput(newUsername)) return
        if (newUsername === null) return

        const pb = API.getProvider()

        try {
            const userId = userID ? userID : pb.authStore?.model?.id ?? "";
            const isAdmin = await pb.collection("users").getOne(userId).then(item => item.admin == true); 
            if (isAdmin && userID) {
                alert("Kan ikke oppdatere brukernavn til adminbrukere!");
                return;
            }
            await pb.collection("users").update(userId, {
            username: newUsername,
            })
        } catch (err: any) {
            alert("Brukernavn er allerede i bruk")
            return
        }
        setUsername(newUsername)
        alert("Brukernavn Endret")
    }

    const ChangePassword = async () => {
        const oldPassword: string | null = prompt("Skriv ditt nåværende passord")
        if (!ValidateInput(oldPassword)) return
        const newPassword: string | null = prompt("Skriv inn nytt passord")
        if (!ValidateInput(newPassword)) return
        //Piss som ikke gjør noe men vi trenger for at kode skal kompilere!
        if (newPassword === null) return
        const passwordConfirm: string | null = prompt("Skriv inn passordet på nytt")
        if (!ValidateInput(passwordConfirm)) return

        if (newPassword !== passwordConfirm) {
            alert("Passordene er ikke like")
            return
        }
        if (oldPassword === newPassword) {
            alert("Ditt nye passord kan ikke være likt som det gamle")
            return
        }

        if (newPassword.length < 8) {
            alert("Passordet må ha 8 eller flere tegn")
            return
        }

        const pb = API.getProvider()
        const username = pb.authStore.model?.username ?? ""

        //Endre passord
        //Token blir slettet om passordet endres
        try {
            const userId = userID ? userID : pb.authStore?.model?.id ?? "";
            const isAdmin = await pb.collection("users").getOne(userId).then(item => item.admin == true); 
            if (isAdmin && userID) {
                alert("Kan ikke endre passord til adminbrukere!");
                return;
            }

            await pb.collection("users").update(userId, {
                password: newPassword,
                passwordConfirm: passwordConfirm,
                oldPassword: oldPassword,
            })
        } catch (err: any) {
            alert("Passordet er feil")
            return
        }

        alert("Passord endret")

        //Skaffe ny token
        try {
            await pb.collection("users").authWithPassword(username, newPassword)
        } catch (err: any) {
            pb.authStore.clear()
            navigate("/login")
        }
    }

    const DeleteUser = async () => {
        const deleteUser: boolean | null = confirm("Vil du slette brukeren?")

        if (!deleteUser || deleteUser == null) {
            return
        }

        try {
            const pb = API.getProvider()
            const userId = userID ? userID : pb.authStore?.model?.id ?? "";
            const isAdmin = await pb.collection("users").getOne(userId).then(item => item.admin == true); 
            if (isAdmin && userID) {
                alert("Kan ikke slette adminbrukere!");
                return;
            }
            await pb.collection("users").delete(userId)
            pb.authStore.clear()
        } catch {
            alert("En feil har skjedd")
            return
        }
        alert("Bruker din er slettet")
        navigate("/login")
    }

    const loadAdminRight = () => {
        const pb = API.getProvider()
        setIsAdmin(pb.authStore?.model?.admin ?? false)
    }

    useEffect(() => {
        const pb = API.getProvider()
        if (!pb.authStore.isValid) {
            navigate("/login")
            return
        }

        loadAdminRight()
    }, [])

    return (
        <div className={`${styles.AdministerUser} ${isAdmin ? styles.adminMode : ""}`}>
            <Header />
            <div className={styles.box}>
                <h1 className={styles.h1}>{username}</h1>
                <button className={styles.button} onClick={ChangeUserName}>
                    Endre Brukernavn
                </button>
                <button className={styles.button} onClick={ChangePassword}>
                    Endre passord
                </button>
                <button className={styles.button} onClick={DeleteUser}>
                    Slett bruker
                </button>
            </div>
        </div>
    )
}

function ValidateInput(input: string | null): boolean {
    if (input === null) return false
    if (input === "") {
        alert("Feltet kan ikke stå tomt")
        return false
    }
    return true
}
