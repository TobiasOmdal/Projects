import {Menu} from "lucide-react"
import {useEffect, useState} from "react"
import {useNavigate} from "react-router"
import {API} from "../API/API"
import design from "./Header.module.css"

export const Header = () => {
    const navigate = useNavigate()
    const [isOpen, setIsMenuOpen] = useState(false)
    const [isAdmin, setIsAdmin] = useState(false)

    const logout = () => {
        const pb = API.getProvider()
        pb.authStore.clear()
        navigate("/login")
    }
    const goHome = () => {
        navigate("/")
    }

    const hamburgerMenu = () => {
        setIsMenuOpen(!isOpen)
    }

    const loadAdminRight = () => {
        const pb = API.getProvider()
        setIsAdmin(pb.authStore?.model?.admin ?? false)
    }

    const hamburgerContent = (
        <nav className={`${design.menu} ${isOpen ? design.isActive : ""}`}>
            {/* menu items are placed here */}
            <a href="/" className={design.menuItems}>
                {" "}
                Hjem{" "}
            </a>
            <a href="/profile" className={design.menuItems}>
                {" "}
                Profil{" "}
            </a>
            <a href="/create" className={design.menuItems}>
                {" "}
                Opprett nytt sett!{" "}
            </a>
            <a href="/user" className={design.menuItems}>
                {" "}
                Rediger min bruker{" "}
            </a>
            {isAdmin ? (
                <a href="/admin" className={design.menuItems}>
                    {" "}
                    Administrer adminbrukere{" "}
                </a>
            ) : null}
        </nav>
    )
    /*Test*/

    useEffect(() => {
        const pb = API.getProvider()
        if (!pb.authStore.isValid) {
            navigate("/login")
            return
        }

        loadAdminRight()
    }, [])

    return (
        <div>
            <header className={`${design.header} ${isAdmin ? design.adminMode : ""}`}>
                <div className={design.hamburgerBox}>
                    <Menu className={design.hamburger} onClick={hamburgerMenu} />
                    <div className={design.menu}>{isOpen && hamburgerContent}</div>
                </div>
                <div onClick={goHome} className={design.logoBox}>
                    <h1 className={design.logo}>FLASHY{isAdmin ? "(admin)" : ""} </h1>
                </div>

                <div className={design.logoutBox}>
                    <button onClick={logout} className={design.logout}>
                        Logg ut
                    </button>
                </div>
            </header>
        </div>
    )
}
