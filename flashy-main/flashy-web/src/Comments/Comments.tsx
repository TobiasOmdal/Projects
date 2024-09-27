import styles from "../styles/Comments.module.css"
import {API} from "../API/API"
import {Header} from "../Header/Header"
import {useEffect, useState, useRef} from "react"
import {useNavigate, useParams} from "react-router"
import {Comment} from "./Comment"

import {Pencil, ThumbsUp, Trash2} from "lucide-react"
export const Comments = () => {
    const navigate = useNavigate()
    const {setId} = useParams()
    //test : tald71vs9rroxd6

    const ref = useRef(false)
    const [liked, setuserliked] = useState(false)
    const [likes, setLikes] = useState(0)
    const [input, setInput] = useState("")
    const [setname, setSetName] = useState("")
    const [content, setContent] = useState([])
    const [isAdmin, setIsAdmin] = useState(false)
    const [isOwner, setIsOwner] = useState(false)

    const CreateNewComment = async () => {
        const pb = API.getProvider()
        const comments = await pb.collection("comment").getList(1, 1000, {
            filter: pb.filter("set = {:setId}", {setId}),
            expand: "user",
        })

        if (comments === null) {
            return
        }
        let newContent: any = []
        comments.items.forEach((element) => {
            let timePosted: String = element["created"]
            timePosted = timePosted.split(" ")[0]
            const data = {
                id: element["id"],
                comment: element["comment"],
                userId: element["user"],
                username: element.expand?.user["username"],
                posted: timePosted,
            }
            newContent.push(data)
        })
        newContent = newContent.reverse()
        setContent(newContent)
    }

    const PostNewComment = async () => {
        if (input === "") return
        const pb = API.getProvider()
        const userId: string = pb.authStore?.model?.id ?? ""
        const data = {
            set: setId,
            user: userId,
            comment: input,
        }
        await pb.collection("comment").create(data)
        window.location.reload()
    }

    const Like = async () => {
        setuserliked(true)
        setLikes(likes + 1)
        const pb = API.getProvider()
        const userId: string = pb.authStore?.model?.id ?? ""
        const data = {
            set: setId,
            user: userId,
        }
        await pb.collection("like").create(data)
    }
    const UnLike = async () => {
        try {
            const pb = API.getProvider()
            const userId: string = pb.authStore?.model?.id ?? ""
            const userLike = await pb.collection("like").getList(1, 1, {
                filter: pb.filter("set = {:setId} && user = {:userId}", {setId, userId}),
            })
            if (userLike == null || userLike.totalItems == 0) return
            await pb.collection("like").delete(userLike.items[0]["id"])
        } catch {
            return
        }
        setuserliked(false)
        setLikes(likes - 1)
    }

    const LikeButton = async () => {
        if ((await GetUserLike()) === null) {
            Like()
            return
        }
        UnLike()
    }

    const GetUserLike = async () => {
        const pb = API.getProvider()
        const userId: string = pb.authStore?.model?.id ?? ""
        const userLike = await pb.collection("like").getList(1, 1, {
            filter: pb.filter("set = {:setId} && user = {:userId}", {setId, userId}),
        })

        if (userLike == null || userLike.totalItems == 0) {
            return null
        }
        return userLike.items[0]
    }

    const GetLikes = async () => {
        const pb = API.getProvider()
        const results = await pb.collection("like").getList(1, 1, {
            filter: pb.filter("set = {:setId}", {setId}),
        })
        if (results === null) return
        setLikes(results.totalItems)
        if ((await GetUserLike()) === null) return
        setuserliked(true)
    }
    const GetSetName = async () => {
        const pb = API.getProvider()

        try {
            const results = await pb.collection("sets").getList(1, 1, {
                filter: pb.filter("id = {:setId}", {setId}),
            })
            if (results.totalItems === 0 || results == null) return

            const setName: string = results.items[0]["title"]
            setSetName(setName)
        } catch {}
    }

    useEffect(() => {
        if (ref.current) {
            GetLikes()
            CreateNewComment()
            GetSetName()
        }
        return () => {
            ref.current = true
        }
    }, [])

    const loadAdminRight = () => {
        const pb = API.getProvider()
        setIsAdmin(pb.authStore?.model?.admin ?? false)
    }

    const loadSet = async () => {
        const pb = API.getProvider()
        const user = pb.authStore?.model
        const record = await pb.collection('sets').getOne(setId ?? "")
        setIsOwner(record.owner == user?.id)
    }

    useEffect(() => {
        const pb = API.getProvider()
        if (!pb.authStore.isValid) {
            navigate("/login")
            return
        }

        loadAdminRight()
        loadSet()
    }, [])

    const canChangeSet = isAdmin || isOwner

    return (
        <div className={`${styles.box} ${isAdmin ? styles.adminMode : ""}`}>
            <Header />

            <div className={styles.headerDiv}>
                <h1 className={styles.setname}>{setname}</h1>
                {canChangeSet && (

                <button className={styles.editBtn} onClick={() => navigate(`/update/${setId}`)}>
                    <Pencil size={36} />
                </button>
                )}
            </div>
            <div className={styles.btnContainer}>
                <button
                    className={styles.homeBtn}
                    onClick={() => {
                        navigate(`/`)
                    }}
                >
                    Hjem
                </button>

                <button
                    className={styles.playBtn}
                    onClick={() => {
                        navigate(`/play/${setId}`)
                    }}
                >
                    Spill
                </button>
            </div>

            <div className={styles.scrollcontainer}>
                <div className={styles.flexcontainer}>
                    {content.map((data: any, index) => (
                        <Comment
                            key={index}
                            comment={data.comment}
                            username={data.username}
                            userId={data.userId}
                            id={data.id}
                            posted={data.posted}
                        />
                    ))}
                </div>
            </div>

            <div className={styles.inputContainer}>
                <input
                    className={styles.inputField}
                    placeholder="Skriv en kommentar..."
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                ></input>

                <button
                    className={styles.publishButton}
                    onClick={(e) => {
                        e.preventDefault()
                        PostNewComment()
                    }}
                >
                    <i className="arrowIcon">→</i>{" "}
                    {/* You can replace this with an actual arrow icon from an icon library if you're using one */}
                </button>
            </div>
            <div className={styles.likecontainer}>
                <ThumbsUp className={liked ? styles.iconLikeDisabled : styles.iconLike} onClick={LikeButton} />
                <p className={styles.plikes}>Likes : {likes} </p>
            </div>
            
            {canChangeSet && (
                <button className={styles.deleteBtn} onClick={async (e) => {
                    e.preventDefault()
                    const shouldDelete = confirm("Vil du slette dette settet?")
                    if (!shouldDelete) return;

                    const pb = API.getProvider()
                    await pb.collection("sets").delete(setId ?? "")
                    navigate("/")
                }}>
                    <Trash2 />
                    Slett
                </button>
            )}
        </div>
    )
}
