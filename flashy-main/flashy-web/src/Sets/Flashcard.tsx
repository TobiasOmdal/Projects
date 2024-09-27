import React, {cloneElement, useEffect, useState} from "react"
import {Flag, FlagOff} from "lucide-react"
import styles from "./Flashcard.module.css"

export interface FlashcardProps {
    question: string
    answer: string
    isHard: boolean
    hardMode: boolean
    onMarkHard: (isHard: boolean) => void
    questionImage?: string
    answerImage?: string
}

export const Flashcard: React.FC<FlashcardProps> = ({
    question,
    answer,
    isHard,
    hardMode,
    onMarkHard,
    questionImage,
    answerImage,
}) => {
    const [flipped, setFlipped] = useState(false)

    const handleFlip = () => {
        setFlipped(!flipped)
    }

    return (
        <div className={styles.flashcardContainer} onClick={handleFlip}>
            <div className={`${styles.flashcard} ${flipped ? styles.flipped : ""}`}>
                <div className={styles.front}>
                    <div className={styles.header} onClick={(e) => e.stopPropagation()}>
                        {!hardMode ? (
                            <>
                                <span>Marker dette spørsmålet som vanskelig</span>
                                <button onClick={() => onMarkHard(!isHard)}>
                                    {isHard ? <Flag className={styles.flags} /> : <FlagOff color="white" />}
                                </button>
                            </>
                        ) : (
                            <div />
                        )}
                    </div>
                    {questionImage && <img src={questionImage} alt="Question" className={styles.flashcardImage} />}
                    <div className={styles.content}>{question}</div>
                </div>
                <div className={styles.back}>
                    {answerImage && <img src={answerImage} alt="Answer" className={styles.flashcardImage} />}
                    <div className={styles.content}>{answer}</div>
                </div>
            </div>
        </div>
    )
}
