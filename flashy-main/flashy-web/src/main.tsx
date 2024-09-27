import React from "react"
import ReactDOM from "react-dom/client"
import {createBrowserRouter, RouterProvider} from "react-router-dom"
import {AdministerUser} from "./AdministerUser/AdministerUser"
import {Admin} from "./Adminpage/Adminpage"
import {FlashcardSet} from "./Flashcards/FlashcardSet"
import {Homepage} from "./Homepage/Homepage"
import {Login} from "./Login/Login"
import {Signup} from "./Login/Signup"
import {Profile} from "./Profile/Profile"
import {SetPlayer} from "./Sets/SetPlayer"
import "./styles/background.css"
import "./styles/main.css"
import "./styles/normalize.css"
import {Comments} from "./Comments/Comments"

const router = createBrowserRouter([
    {
        path: "/login",
        element: <Login />,
    },
    {
        path: "/signup",
        element: <Signup />,
    },
    {
        path: "/user",
        element: <AdministerUser />,
    },
    {
        path: "/play/:id",
        element: <SetPlayer />,
    },
    {
        path: "/create",
        element: <FlashcardSet />,
    },
    {
        path: "/update/:setid",
        element: <FlashcardSet />,
    },
    {
        path: "/profile",
        element: <Profile />,
    },
    {
        path: "/admin",
        element: <Admin />,
    },
    {
        path: "/comment/:setId",
        element: <Comments />,
    },
    {
        path: "/user/:userID",
        element: <AdministerUser />
    },
    {
        path: "/",
        element: <Homepage />,
    },
    {
        path: "/:page",
        element: <Homepage />,
    },
])

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>
)
