import {useEffect, useRef, useState} from "react"
import {useNavigate} from "react-router"
import {API} from "../API/API"
import {Header} from "../Header/Header"
import design from "./Adminpage.module.css"
import {UserRound} from "lucide-react"
import { RecordModel } from "pocketbase"

const pb = API.getProvider()

export const Admin = () => {
    const [admins, setAdmins] = useState<RecordModel[]>([]);
    const [users, setUsers] = useState<RecordModel[]>([]);
    const [filteredUsers, setFilteredUsers] = useState<RecordModel[]>([]); 
    const [filteredAdmins, setFilteredAdmins] = useState<RecordModel[]>([]); 
    const [inputFilter, setInputFilter] = useState("");
    const navigate = useNavigate();
    const ref = useRef(false);

    const loggedInUser: string = pb.authStore.model?.id; 
    
    const loadUsers = async () => {
        const fetchedUsers = await pb.collection("users").getList(1, 50, {
            expand: "user", 
            filter: "admin=false"
        });
        const fetchedAdmins = await pb.collection("users").getList(1, 50, {
            expand: "user", 
            filter: "admin=true"
        });
        const usersData = fetchedUsers.items.map(user => user);
        setUsers(usersData);
        setFilteredUsers(usersData);

        const adminsData = fetchedAdmins.items.map(admin => admin);
        setAdmins(adminsData);
        setFilteredAdmins(adminsData);

        let isAdmin = false;
        for (let admin of adminsData) {
            if (admin.id === pb.authStore.model?.id) {
                isAdmin = true;
            }
        }
        if (!pb.authStore.isValid || !isAdmin) navigate("/login");
    }

    useEffect(() => {
        if (ref.current) {
            loadUsers();
        }
        return () => {
            ref.current = true
        }

    }, [])

    const makeAdmin = async (element: React.MouseEvent) => {
        const user = users[element.target.name];

        const data = {
            name: user.name, 
            admin: !user.admin,
        }
        await pb.collection("users").update(user.id, data);

        loadUsers();
    }

    useEffect(() => {
        searchUser();
    }, [inputFilter])
    
    const searchUser = () => {
        const searchQuery = inputFilter.trim().toLowerCase()
        setFilteredUsers(users.filter((user) => user.username.toLowerCase().startsWith(searchQuery)));
        setFilteredAdmins(admins.filter((admin) => admin.username.toLowerCase().startsWith(searchQuery)));
    }

    return(
        <div className={design.layout}>
            <Header/>
            <div> 
                <h1 className={design.info}> ADMINTILGANGER </h1>
            </div>
            <input 
                type="text"  
                placeholder=" Søk etter brukernavn..." 
                className={design.searchField} 
                onChange={(e) => setInputFilter(e.target.value)}>
            </input>
            <div className = {design.usersContainer}>
                {filteredAdmins.map((admin, index) => {
                    return (
                        <div key={index} className={design.userContainer}>
                            <UserRound 
                                className={design.userLogo} 
                                onClick={() => navigate(`/user/${loggedInUser == admin.id ? "" : admin.id}`)}> 
                            </UserRound>
                            <h1 
                                className={design.username} 
                                onClick={() => navigate(`/user/${loggedInUser == admin.id ? "" : admin.id}`)}> 
                                {admin.username} {loggedInUser == admin.id ? "(deg)" : ""}
                            </h1>
                            <button 
                                className={design.adminButton} 
                                name={index.toString()} 
                                disabled={true} 
                                onClick={(e) => makeAdmin(e)}>
                                     Admin 
                            </button>
                        </div>
                    )
                })}

                {filteredUsers.map((user, index) => {
                    return (
                        <div 
                            key={user.id} 
                            className={design.userContainer}>
                                <UserRound 
                                    className ={design.userLogo} 
                                    onClick={() => navigate(`/user/${loggedInUser == user.id ? "" : user.id}`)}> 
                                </UserRound>
                                <h1 
                                    className={design.username}
                                    onClick={() => navigate(`/user/${loggedInUser == user.id ? "" : user.id}`)}> 
                                    {user.username} {loggedInUser == user.id ? "(deg)" : ""}
                                </h1>
                                <button 
                                    className = {design.button} 
                                    name={index.toString()} 
                                    onClick={(e) => makeAdmin(e)}> 
                                    Sett som admin 
                                </button>
                        </div>
                    )
                })}
            </div>
        </div>
    )
}