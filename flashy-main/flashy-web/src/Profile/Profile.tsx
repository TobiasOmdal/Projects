import {CircleUserRound} from "lucide-react"
import {useNavigate} from "react-router"
import {Header} from "../Header/Header"
import design from "./Profile.module.css"

export const Profile = () => {
    const navigate = useNavigate()

    const homepageRouter = () => {
        navigate("/")
    }

    const redigerProfilRouter = () => {
        navigate("/user")
    }

    {
        /* todo: legge til router mine sett, rediger mine sett */
    }

    return (
        <div className={design.layout}>
            <Header />
            <div className={design.profilePictureBox}>
                <CircleUserRound className={design.profilePicture} />
            </div>
            <div className={design.myProfileBox}>
                <h1 className={design.myProfile}>MIN PROFIL</h1>
            </div>

            <div className={design.buttonBox}>
                <button onClick={homepageRouter} className={design.button}>
                    Hjem
                </button>
                <button className={design.button} onClick={() => navigate("/")}>Mine sett</button>
                <button className={design.button} onClick={() => navigate("/2")}>Favoritter</button>
                <button onClick={redigerProfilRouter} className={design.button}>
                    Rediger profil
                </button>
            </div>
        </div>
    )
}
