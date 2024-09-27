import {Trash2} from "lucide-react"
import {RecordModel} from "pocketbase"
import {ChangeEvent, useEffect, useRef, useState} from "react"
import {useNavigate, useParams} from "react-router"
import {API} from "../API/API"
import {Header} from "../Header/Header"
import design from "./FlashcardSet.module.css"
import { ImageUpload } from "./ImageUpload"

const pb = API.getProvider()

interface Flashcard {
    question: string
    answer: string
    questionImage: File | null
    answerImage: File | null
}

const urlToFile = async (url: string): Promise<File | null> => {
    if (url.trim() === "") return null
    const response = await fetch(url).catch(() => {
        return null
    })

    if (!response) return null
    const blob = await response.blob()
    return new File([blob], "file.jpg")
}

export const FlashcardSet = () => {
    const navigate = useNavigate()
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [setImage, setSetImage] = useState<File | null>(null)
    const [flashcard, setFlashcard] = useState<Flashcard[]>([
        {question: "", answer: "", questionImage: null, answerImage: null},
    ])
    const [publicSet, setPublicSet] = useState(false)
    const [loading, setLoading] = useState(false)
    const formRef = useRef<HTMLFormElement>(document.createElement("form"))
    const [record, setRecord] = useState<RecordModel>()
    const {setid} = useParams()
    const [isAdmin, setIsAdmin] = useState(false)

    useEffect(() => {
        loadData()
    }, [])

    const loadData = async () => {
        if (!setid) return

        try {
            const record = await pb.collection("sets").getOne(setid, {requestKey: null})
            const setimgUrl = pb.files.getUrl(record, record.image, {requestKey: null})
            const setimg = await urlToFile(setimgUrl)

            setRecord(record)
            setTitle(record.title)
            setDescription(record.description)
            setPublicSet(record.public)
            setSetImage(setimg)

            const flashcardData = await pb.collection("flashcards").getList(0, 1000, {
                filter: pb.filter(`parent_set = {:setid}`, {setid}),
                requestKey: null,
            })

            const dataset = await Promise.all(
                flashcardData.items.map(async (f) => {
                    const questionImageUrl = pb.files.getUrl(f, f.questionImage, {requestKey: null})
                    const answerImageUrl = pb.files.getUrl(f, f.answerImage, {requestKey: null})
                    console.log(questionImageUrl)
                    const questionImage = await urlToFile(questionImageUrl)
                    const answerImage = await urlToFile(answerImageUrl)

                    return {
                        question: f.question,
                        answer: f.answer,
                        questionImage,
                        answerImage,
                    }
                })
            )

            setFlashcard(dataset)
        } catch (error) {
            alert("Klarte ikke laste sett")
        }
    }

    const createSet = async () => {
        setLoading(true)
        try {
            let owner = pb.authStore.model?.id
            const formData = new FormData()
            formData.append("title", title)
            formData.append("owner", owner)
            formData.append("public", JSON.stringify(publicSet))
            formData.append("category", "")
            formData.append("description", description)

            if (setImage) {
                formData.append("image", setImage)
            } else {
                formData.append("image", "")
            }

            let fSet: RecordModel
            if (setid) {
                formData.append("owner", record?.owner)
                fSet = await pb.collection("sets").update(setid, formData)
            } else {
                fSet = await pb.collection("sets").create(formData)
            }

            if (fSet) {
                if (setid) {
                    await deleteFlashcardsDb(fSet.id)
                }
                await createFlashcards(fSet.id)

                navigate(`/play/${fSet.id}`)
            } else {
                throw new Error("Kunne ikke lage sett")
            }
        } catch (err) {
            console.error("Error creating set:", err)
            alert("Klarte ikke lage settet, vennligst prøv igjen")
        } finally {
            setLoading(false)
        }
    }

    const deleteFlashcardsDb = async (parentId: string) => {
        const flashcardData = await pb.collection("flashcards").getList(0, 1000, {
            filter: pb.filter(`parent_set = {:parentId}`, {parentId}),
            requestKey: null,
        })

        for (const item of flashcardData.items) {
            await pb.collection("flashcards").delete(item.id)
        }
    }

    const createFlashcards = async (parent_id: string) => {
        for (const element of flashcard) {
            const formData = new FormData()
            formData.append("parent_set", parent_id)
            formData.append("difficult", "false")
            formData.append("question", element.question)
            formData.append("answer", element.answer)

            if (element.questionImage instanceof File) {
                formData.append("questionImage", element.questionImage)
            }

            if (element.answerImage instanceof File) {
                formData.append("answerImage", element.answerImage)
            }

            try {
                await pb.collection("flashcards").create(formData)
            } catch (err: any) {
                console.log("error", err)
            }
        }
    }

    const handleFormChange = (index: number, event: ChangeEvent<HTMLInputElement>) => {
        let data: any = [...flashcard]
        if (event.target === null) {
            return
        }
        data[index][event.target.name] = event.target.value
        setFlashcard(data)
    }

    const handleImageChange = (index: number, answer: boolean, image: File | null) => {
        let data = [...flashcard]

        data[index][answer ? "answerImage" : "questionImage"] = image
        setFlashcard(data)
    }

    const deleteFlashcard = (index: number) => {
        const data = [...flashcard]
        data.splice(index, 1)
        setFlashcard(data)
    }

    const addFlashcard = () => {
        let newFlashcard = {
            question: "",
            answer: "",
            questionImage: null,
            answerImage: null,
        }

        setFlashcard([...flashcard, newFlashcard])
    }

    const onlyOneflashcard = flashcard.length === 1

    /*  Ble dette riktig implementert, med usestate ? */

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
        /* La  til dette */
        <div className={`${design.layout} ${isAdmin ? design.adminMode : ""}`}>
            <form
                ref={formRef}
                onSubmit={(e) => e.preventDefault()}
                encType="multipart/form-data"
                className={design.form}
            >
                <h1 className={design.heading}>{setid ? "Oppdater sett" : "Opprett nytt sett"}</h1>

                <div className={design.titleContainer}>
                    <input
                        className={design.title}
                        type="text"
                        autoFocus
                        required
                        value={title}
                        placeholder="Tittel"
                        onChange={(e) => {
                            setTitle(e.target.value)
                        }}
                        maxLength={35}
                    />

                    <ImageUpload image={setImage} onImageChanged={(image) => setSetImage(image)} />
                </div>

                <div className={design.visibility}>
                    <label>
                        <input
                            type="radio"
                            required
                            name="visibility"
                            checked={!publicSet}
                            onChange={() => setPublicSet(false)}
                        />{" "}
                        Privat
                    </label>
                    <label>
                        <input
                            type="radio"
                            required
                            name="visibility"
                            checked={publicSet}
                            onChange={() => setPublicSet(true)}
                        />{" "}
                        Offentlig
                    </label>
                </div>

                <textarea
                    className={design.desc}
                    required
                    placeholder="Beskrivelse"
                    value={description}
                    onChange={(e) => {
                        setDescription(e.target.value)
                    }}
                    maxLength={50}
                />

                {flashcard.map((input, index) => {
                    return (
                        <div key={index} className={design.flashcardContainer}>
                            <Header />
                            <input
                                type="text"
                                required
                                name="question"
                                placeholder="Spørsmål"
                                value={input.question}
                                onChange={(e) => handleFormChange(index, e)}
                                className={design.flashcardInput}
                                maxLength={300}
                            />

                            <ImageUpload
                                image={input.questionImage}
                                onImageChanged={(image) => handleImageChange(index, false, image)}
                            />

                            <input
                                type="text"
                                required
                                name="answer"
                                placeholder="Svar"
                                value={input.answer}
                                onChange={(e) => handleFormChange(index, e)}
                                className={design.flashcardInput}
                                maxLength={300}
                            />

                            <ImageUpload
                                image={input.answerImage}
                                onImageChanged={(image) => handleImageChange(index, true, image)}
                            />

                            <button
                                disabled={onlyOneflashcard}
                                onClick={(e) => {
                                    e.preventDefault()
                                    deleteFlashcard(index)
                                }}
                                className={design.flashcardDeleteBtn}
                            >
                                <Trash2 />
                            </button>
                        </div>
                    )
                })}

                <input
                    type="button"
                    className={design.addFlashcardBtn}
                    onClick={addFlashcard}
                    value="Legg til flashcard"
                />

                <div className={design.submitContainer}>
                    <input
                        className={design.submitBtn}
                        type="submit"
                        value={setid ? "Oppdater sett" : "Opprett nytt sett"}
                        disabled={loading}
                        onClick={(e) => {
                            e.preventDefault()
                            if (formRef.current.reportValidity()) {
                                createSet()
                            }
                        }}
                    />
                </div>
            </form>
        </div>
    )
}
