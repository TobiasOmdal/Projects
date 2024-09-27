import {Flag, Home, MoveLeft, MoveRight, RefreshCcw} from "lucide-react"
import {RecordModel} from "pocketbase"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router"
import {API} from "../API/API"
import {Header} from "../Header/Header"
import {shuffle} from "../Utils/shuffle"
import {Flashcard} from "./Flashcard"
import styles from "./SetPlayer.module.css"
//import questionImage from './citric-acid-teaser.png'; // Adjust the path as necessary

export const SetPlayer = () => {
    const [title, setTitle] = useState("")
    const [questions, setQuestions] = useState<RecordModel[]>([])
    const [questionIndex, setQuestionIndex] = useState<number>(0)
    const [hardMode, setHardMode] = useState<boolean>(false)
    const [errorText, setErrorText] = useState("")
    const navigate = useNavigate()
    let {id} = useParams()

    useEffect(() => {
        loadData()
    }, [])

    interface ProgressBarProps {
        current: number;
        total: number;
    }

const ProgressBar = ({ current, total }: ProgressBarProps) => {
  const percentage = (current / total) * 100;
  return (
    <div className={styles.progressBarContainer}>
      <div className={styles.progressBar} style={{ width: `${percentage}%` }}></div>
    </div>
  );
};


    const loadData = async () => {
        const pb = API.getProvider()
        try {
            const record = await pb.collection("sets").getOne(id ?? "")
            setTitle(record.title)

            const flashcards = await pb.collection("flashcards").getList(1, 1000, {
                filter: pb.filter(`parent_set = {:id}`, {id}),
            })

            const difficult = await loadDifficult()
            const copy = [...flashcards.items]

            for (let i = 0; i < copy.length; i++) {
                const element = copy[i]
                if (difficult.some((d) => d.flashcard === element.id)) {
                    element.difficult = true
                }
            }

            setQuestions(shuffle(flashcards.items))
            if (flashcards.items.length > 0) {
                setQuestionIndex(0)
            }
        } catch (error) {
            setErrorText("Kunne ikke finne sett")
        }
    }

    const goHome = () => {
        navigate("/")
    }

    const playSetAgain = () => {
        setHardMode(false)
        setQuestionIndex(0)
    }

    const practiceHard = () => {
        setHardMode(true)
        setQuestionIndex(0)
    }

    const loadDifficult = async () => {
        const pb = API.getProvider()
        const userId = pb.authStore?.model?.id ?? 0
        const markedHard = await pb.collection("flashcard_mark_difficult").getList(1, 1000, {
            filter: pb.filter("user = {:userId}", {
                userId,
            }),
        })

        return markedHard.items
    }

    const markHard = (isHard: boolean) => {
        if (questionIndex == null) return
        let copy = [...questions]

        const current = copy[questionIndex]
        current.difficult = isHard

        const pb = API.getProvider()
        const userId = pb.authStore?.model?.id ?? 0

        if (isHard) {
            try {
                pb.collection("flashcard_mark_difficult").create({user: userId, flashcard: current.id})
            } catch (error) {
                alert("Klarte ikke merke som favoritt")
            }
        } else {
            try {
                pb.collection("flashcard_mark_difficult")
                    .getList(1, 1, {
                        filter: pb.filter("user = {:userId} && flashcard = {:flashcardId}", {
                            userId,
                            flashcardId: current.id,
                        }),
                    })
                    .then((records: any) => {
                        pb.collection("flashcard_mark_difficult").delete(records.items[0].id)
                    })
            } catch (error) {
                alert("Klarte ikke avmerke som favoritt")
            }
        }

        setQuestions(copy)
    }

    const getImageUrl = (question: any, answer?: boolean): string => {
        const pb = API.getProvider()
        const url = pb.files.getUrl(question, answer ? question.answerImage : question.questionImage)
        return url
    }

    if (questions.length < 1) {
        return (
            <div className={styles.SetPlayer}>
                <h1>Ingen spørsmål funnet på settet</h1>
            </div>
        )
    }

    if (errorText.trim() !== "") {
        return <h1>{errorText}</h1>
    }

    let questionsOnDisplay = questions
    if (hardMode) {
        questionsOnDisplay = questionsOnDisplay.filter((q) => q.difficult)
    }

    const card = questionsOnDisplay[questionIndex]
    const previousEnabled = questionIndex > 0
    const nextEnabled = questionIndex + 1 < questionsOnDisplay.length
    const containsHardQuestions = questions.some((q) => q.difficult)

    return (
        <>
            <Header />
            <div className={`${styles.SetPlayer}`}>
                <div className={styles.titleOfCard}>
                    {hardMode && <Flag size={35} className={styles.flagHard} />}
                    <h1 className={`${styles.heading} ${hardMode ? styles.headingHard : ""}`}>{title}</h1>
                    {hardMode && <Flag size={35} className={styles.flagHard} />}
                </div>

                <h2 className={styles.numberLbl}>
                    {questionIndex + 1}/{questionsOnDisplay.length}
                </h2>
                <ProgressBar current={questionIndex + 1} total={questionsOnDisplay.length} />

                <div className={styles.mainPlayer}>
                    <button
                        onClick={() => setQuestionIndex((prev) => (prev ?? 0) - 1)}
                        disabled={!previousEnabled}
                        className={styles.changeFlashcardBtns}
                    >
                        <MoveLeft size={64} />
                    </button>

                    <Flashcard
                        hardMode={hardMode}
                        key={card.question}
                        question={card.question}
                        answer={card.answer}
                        isHard={card.difficult}
                        onMarkHard={markHard}
                        questionImage={getImageUrl(card)}
                        answerImage={getImageUrl(card, true)}
                    />

                    {nextEnabled ? (
                        <button
                            onClick={() => setQuestionIndex((prev) => (prev ?? 0) + 1)}
                            className={styles.changeFlashcardBtns}
                        >
                            <MoveRight size={64} />
                        </button>
                    ) : (
                        <div style={{width: "104px"}} className={styles.endFlashcardBtnContainer}>
                            <button
                                onClick={playSetAgain}
                                title="Spill sett på nytt"
                                className={styles.endFlashcardBtns}
                            >
                                <RefreshCcw size={48} />
                            </button>

                            {containsHardQuestions && !hardMode ? (
                                <button
                                    disabled={!containsHardQuestions}
                                    onClick={practiceHard}
                                    title="Øv på vanskelige sett"
                                    className={styles.endFlashcardBtns}
                                >
                                    <Flag size={48} />
                                </button>
                            ) : null}

                            <button onClick={goHome} title="Gå til hovedsiden" className={styles.endFlashcardBtns}>
                                <Home size={48} />
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </>
    )
}
