import {useLayoutEffect} from "react"
import {useNavigate} from "react-router"
import {API} from "../API/API"
import styles from "../styles/LoginSignup.module.css"

interface IProps extends React.PropsWithChildren {
    buttonText: string;
    onClick: () => void;
}

export const LoginSignup = (props: IProps) => {
    const navigate = useNavigate()

    useLayoutEffect(() => {
        const pb = API.getProvider()
        if (pb.authStore.isValid) {
            navigate("/")
        }
    }, [])

    return (
        <div className={styles.page}>
            <div className={styles.container}>
                <div className="flashyLogo">FLASHY</div>
                {props.children}
                <button className={styles.changeModeBtn} onClick={props.onClick}>
                    {props.buttonText}
                </button>
            </div>
        </div>
    )
}
