import {ChangeEvent, useRef} from "react"
import {ImageUp, X} from "lucide-react"
import styles from "./ImageUpload.module.css"

interface IImageUpload {
    image: File | null;
    onImageChanged: (image: File | null) => any; 
}

export const ImageUpload = (props: IImageUpload) => {
    const onImageChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target?.files && event.target.files.length > 0) {
            const fileObj = event.target.files[0]
            props.onImageChanged(fileObj)
        }
    }

    let imageUrl: string | null = null
    if (props.image) {
        imageUrl = URL.createObjectURL(props.image)
    }

    return (
        <div className={styles.container}>
            {!imageUrl && (
                <label className={styles.uploadLabelBtn}>
                    <input style={{display: "none"}} onChange={onImageChange} type="file" accept="image/*" />
                    <ImageUp size={24} />
                </label>
            )}

            {imageUrl && (
                <>
                    <img className={styles.image} src={imageUrl} alt="uploaded" />
                    <button className={styles.removeBtn} onClick={e => {
                        e.preventDefault()
                        e.stopPropagation()
                        props.onImageChanged(null)
                    }}>
                        <X />
                    </button>
                </>
            )}
        </div>
    )
}
