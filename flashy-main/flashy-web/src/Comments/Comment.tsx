import styles from "../styles/Comment.module.css"
import {API} from "../API/API"
import {useEffect, useState,useRef} from "react"
import {Trash2} from "lucide-react"

export function Comment(props: any) {
    const ref = useRef(false)
    const [candelete, setcandelete] = useState(false)

    const CheckForDeletionPrivliges = async () => {
        const pb = API.getProvider()
        //const admin: boolean = pb.authStore?.model?.admin ?? false
        //Hvorfor funker ikke denne?
        //const admin: boolean = pb.authStore?.model?.admin ?? false
        const userId: string = pb.authStore?.model?.id ?? ""
        const result = await pb.collection("users").getList(1, 1, {
            filter: pb.filter("id = {:userId}", {userId})
        })

        if (result.totalItems == 0 || result == null) return;

        const admin = result.items[0]["admin"]

        if (admin)
        {
            setcandelete(true)
            return;
        }

        
        if (userId === props.userId)
                setcandelete(true)       
    }


    useEffect(() => {
        if (ref.current) {
            CheckForDeletionPrivliges(); 
            }
        
        return () => {
            ref.current = true
        }
    }, [])

    const DeleteComment = async () => {
        if (!candelete) return;
        const pb = API.getProvider()
        const commentId = props.id;
        try
            {
            await pb.collection('comment').delete(commentId);
            }
            catch
            {
                return;
            }
        window.location.reload()
    }


    return (
        <div className={styles.Comment}>
            <h2 className={styles.h2}>{props.username}</h2>
            <h1 className={styles.h1}>{props.comment}</h1>
            {candelete && <Trash2 className={styles.delete} onClick={DeleteComment}></Trash2>}
            <h2 className={styles.posted}>{props.posted}</h2>
        </div>
    )
}
