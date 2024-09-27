import {Globe, Heart, Lock, MessageCircle, Star} from "lucide-react"
import styles from "./FrontPageSet.module.css"

export interface IFrontPageSetProps {
    name: string
    description: string
    isPublic: boolean
    ownerName: string
    isFavorite: boolean
    likesCount: number
    commentsCount: number
    imageUrl: string | null;
    onVisibilityChange: () => any
    onFavoriteChange: () => any
    onClick: () => any
}

export const FrontPageSet = (props: IFrontPageSetProps) => {
    let headerImageUrl = "src/Sets/defaultHeader.png";

    if (props.imageUrl && props.imageUrl.trim() !== "") {
        headerImageUrl = props.imageUrl
    }

    console.log(props.imageUrl)
    return (
        <div className={styles.FrontPageSet} onClick={props.onClick}>

            <img src={ headerImageUrl} alt="Header" className={styles.headerImage} />
            
            <div className={styles.heading}>
                <h1 className={styles.name}>{props.name}</h1>

                <div className={styles.rightButtonDiv}>
                    <button
                        className={styles.visibilityChangeBtn}
                        onClick={(e) => {
                            e.stopPropagation()
                            props.onFavoriteChange()
                        }}
                    >
                        {props.isFavorite ? <Star fill={"#fff"} /> : <Star />}
                    </button>

                    <button
                        className={styles.visibilityChangeBtn}
                        onClick={(e) => {
                            e.stopPropagation()
                            props.onVisibilityChange()
                        }}
                    >
                        {props.isPublic ? <Globe /> : <Lock />}
                    </button>
                </div>
            </div>

            <div>
                <p className={styles.descriptionText}>{props.description}</p>
                <div className={styles.socialInfo}>
                    <Heart className={styles.heartIcon} /> <span className={styles.iconText}>{props.likesCount} likes</span>
                    <MessageCircle className={ styles.commentIcon} /> <span className={styles.iconText}>{props.commentsCount} kommentarer</span>
                </div>
            </div>
        </div>
    )
}
