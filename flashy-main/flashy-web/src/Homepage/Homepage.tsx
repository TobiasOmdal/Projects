import Fuse from "fuse.js"
import {Map as MapIcon, Plus} from "lucide-react"
import {AuthModel, ListResult, RecordModel} from "pocketbase"
import {useEffect, useState} from "react"
import {useNavigate, useParams} from "react-router"
import {API} from "../API/API"
import {Header} from "../Header/Header"
import {FrontPageSet} from "../Sets/FrontPageSet"
import design from "./Homepage.module.css"

enum Route {
    MySets = 0,
    PublicSets = 1,
    Favorites = 2,
}

enum Filters {
    None = 0,
    Likes = 1,
    Comments = 2,
    Name = 3,
    Date = 4,
}

interface FilterMapping {
    name: string;
    type: Filters;
    id: number;
}

const filterTypes: FilterMapping[] = [
    {
        name: "Ingen filter",
        type: Filters.None,
        id: 0,
    },
    {
        name: "Mest populær",
        type: Filters.Likes,
        id: 1,
    },
    {
        name: "Flest kommentarer",
        type: Filters.Comments,
        id: 2,
    },
    {
        name: "Alfabetisk",
        type: Filters.Name,
        id: 3,
    },
    {
        name: "Nyeste først",
        type: Filters.Date,
        id: 4,
    },
]

const fuseOptions: any = {
    keys: ["title"],
    shouldSort: true,
    threshold: 0.3,
}

export const Homepage = () => {
    const navigate = useNavigate()
    const [listPage, setListPage] = useState(Route.MySets)
    const [mySets, setMySets] = useState<RecordModel[]>()
    const [publicSets, setPublicSets] = useState<RecordModel[]>()
    const [user, setUser] = useState<AuthModel>()
    const {page} = useParams()
    const [query, setQuery] = useState("")
    const [filter, setFilter] = useState(Filters.None)

    useEffect(() => {
        const pageAny: any = page
        const currentRoute: Route = pageAny
        setListPage(currentRoute ?? Route.MySets)
        loadData()
        history.pushState({}, "/", "/")
    }, [])

    const loadData = async () => {
        const pb = API.getProvider()
        if (!pb.authStore.isValid) navigate("/login")

        const userIdLocal = pb.authStore.model?.id ?? 0
        setUser(pb.authStore.model)

        const mySets = await pb.collection("sets").getList(1, 5000, {
            filter: pb.filter("owner={:userIdLocal}", {userIdLocal}),
            expand: "owner",
        })

        const publicSets = await pb.collection("sets").getList(1, 5000, {
            filter: "public=true",
            expand: "owner",
        })

        publicSets.items = publicSets.items.map((x) => ({...x, imageUrl: pb.files.getUrl(x, x.image)}))
        mySets.items = mySets.items.map((x) => ({...x, imageUrl: pb.files.getUrl(x, x.image)}))

        const LikesAndComments: Map<string, number[]> = await LoadLikesAndComments()
        SetLikesAndComments(mySets, LikesAndComments)
        SetLikesAndComments(publicSets, LikesAndComments)

        const likes = await pb.collection("like").getFullList()
        const comments = await pb.collection("comment").getFullList()
        console.log(likes)
        console.log(comments)

        const favorites = await loadFavorites()

        for (let set of mySets.items) {
            set.favorite = favorites.some((f) => f.set === set.id)
        }

        for (let set of publicSets.items) {
            set.favorite = favorites.some((f) => f.set === set.id)
        }

        setMySets(mySets.items)
        setPublicSets(publicSets.items)
    }

    const LoadLikesAndComments = async () => {
        let LikesAndComments: Map<string, number[]> = new Map()
        const pb = API.getProvider()
        let id = ""

        const comments = await pb.collection("comment").getList(1, 10000, {})
        console.log(comments)
        comments.items.forEach((element: RecordModel) => {
            id = element["set"]

            if (LikesAndComments.has(id)) {
                let values: any = LikesAndComments.get(id)
                values[0] += 1
                LikesAndComments.set(id, values)
            } else {
                LikesAndComments.set(id, [1, 0])
            }
        })

        const likes = await pb.collection("like").getList(1, 10000, {})
        likes.items.forEach((element: RecordModel) => {
            id = element["set"]
            if (LikesAndComments.has(id)) {
                let values: any = LikesAndComments.get(id)
                values[1] += 1
                LikesAndComments.set(id, values)
            } else {
                LikesAndComments.set(id, [0, 1])
            }
        })
        return LikesAndComments
    }

    const SetLikesAndComments = async (data: ListResult<RecordModel>, LikesAndComments: Map<string, number[]>) => {
        const pb = API.getProvider()
        await data.items.forEach(async (element: RecordModel) => {
            const id = element["id"]
            if (LikesAndComments.has(id)) {
                element["comments"] = LikesAndComments.get(id)?.[0] ?? 0
                element["likes"] = LikesAndComments.get(id)?.[1] ?? 0
            } else {
                element["comments"] = 0
                element["likes"] = 0
            }
        })
    }

    const deleteSet = async (setId: string) => {
        const pb = API.getProvider()
        await pb.collection("sets").delete(setId)
    }

    const setVisibility = async (setId: string, isPublic: boolean) => {
        const pb = API.getProvider()
        await pb.collection("sets").update(setId, {public: isPublic})
        loadData()
    }

    const setFavorite = async (setId: string, isFavorite: boolean) => {
        const pb = API.getProvider()
        const userId = pb.authStore?.model?.id ?? 0

        if (isFavorite) {
            try {
                pb.collection("favorite_set").create({user: userId, set: setId})
                loadData()
            } catch (error) {
                alert("Klarte ikke merke som favoritt")
            }
        } else {
            try {
                pb.collection("favorite_set")
                    .getList(1, 1, {
                        filter: pb.filter("user = {:userId} && set = {:setId}", {
                            userId,
                            setId,
                        }),
                    })
                    .then((records: any) => {
                        pb.collection("favorite_set").delete(records.items[0].id)
                        loadData()
                    })
            } catch (error) {
                alert("Klarte ikke avmerke som favoritt")
            }
        }
    }

    const handleOnSearch = (value: any) => {
        setQuery(value)
    }

    const search = (data: any, searchQuery: string) => {
        if (searchQuery.trim() === "") return data

        const fuse = new Fuse(data, fuseOptions)
        const results = fuse.search(searchQuery)
        return results.map((result: any) => result.item)
    }

    const loadFavorites = async () => {
        const pb = API.getProvider()
        const userId = pb.authStore?.model?.id ?? 0
        const favorites = await pb.collection("favorite_set").getList(1, 1000, {
            filter: pb.filter("user = {:userId}", {
                userId,
            }),
        })

        return favorites.items
    }

    //Filter funksjoner

    const filterList = (data: any) => {
        if (filter === Filters.Likes) {
            data.sort((a: any, b: any) => SortByNumberedParameter(a, b, "likes"))
        }

        if (filter === Filters.Comments) {
            data.sort((a: any, b: any) => SortByNumberedParameter(a, b, "comments"))
        }

        if (filter === Filters.Name) {
            data.sort((a: any, b: any) => a.title.localeCompare(b.title))
        }

        if (filter === Filters.Date) {
            console.log(data)
            data.sort((a: any, b: any) => new Date(b.created).getTime() - new Date(a.created).getTime())
        }

        return data
    }

    const SortByNumberedParameter = (a: any, b: any, parameter: string) => {
        if (a[parameter] > b[parameter]) return -1
        if (a[parameter] < b[parameter]) return 1
        return 0
    }

    let listData: any[] = mySets ?? []
    if (listPage == Route.PublicSets) {
        listData = publicSets ?? []
    }
    if (listPage == Route.Favorites) {
        const publicSetsFiltered = publicSets?.filter((s) => s.favorite && s.owner !== user?.id) ?? []
        listData = mySets?.filter((s) => s.favorite).concat(publicSetsFiltered) ?? []
    }
    listData = search(listData, query)
    listData = filterList(listData)

    return (
        <div>
            <Header />
            <div className={design.helloMessageBox}>
                <div className={design.topButtons}>
                    <div className={design.topLeftButtons}>
                        <button
                            className={listPage == Route.MySets ? design.selectedBtn : ""}
                            onClick={() => setListPage(Route.MySets)}
                        >
                            Mine sett
                        </button>

                        <button
                            className={listPage == Route.PublicSets ? design.selectedBtn : ""}
                            onClick={() => setListPage(Route.PublicSets)}
                        >
                            Offentlige sett
                        </button>

                        <button
                            className={listPage == Route.Favorites ? design.selectedBtn : ""}
                            onClick={() => setListPage(Route.Favorites)}
                        >
                            Favoritter
                        </button>
                    </div>

                    <div className={design.topRightButtons}>
                        <div className={design.searchBar}>
                            <input
                                className={design.searchInput}
                                type="text"
                                value={query}
                                placeholder="Søk her..."
                                onChange={(e) => handleOnSearch(e.target.value)}
                            />

                            <select onChange={e => {
                                const id = parseInt(e.target.value)
                                const type = filterTypes.find(x => x.id === id);
                                if (!type) return;
                                setFilter(type.type)
                            }} className={design.filterSelect} name="cars" id="cars">
                                {filterTypes.map(type => <option value={type.id}>{type.name}</option>)}
                            </select>
                        </div>

                        <button className={design.addSetBtn}>
                            <Plus
                                onClick={() => {
                                    navigate("/create")
                                }}
                                size={42}
                            />
                        </button>
                    </div>
                </div>
            </div>
            <ul className={design.setsContainer}>
                {listData?.map((set, index) => {
                    const canEdit = user?.admin || set.owner === user?.id
                    return (
                        <div className={design.setContainer} key={index}>
                            <FrontPageSet
                                onClick={() => {
                                    navigate(`/comment/${set.id}`)
                                }}
                                isFavorite={set.favorite}
                                onFavoriteChange={async () => {
                                    await setFavorite(set.id, !set.favorite)
                                }}
                                onVisibilityChange={async () => {
                                    if (!canEdit) return
                                    await setVisibility(set.id, !set.public)
                                }}
                                description={set.description}
                                isPublic={set.public}
                                commentsCount={set.comments}
                                likesCount={set.likes}
                                imageUrl={set.imageUrl}
                                name={set.title}
                                ownerName={set.expand?.owner.name}
                            />
                        </div>
                    )
                })}
            </ul>
        </div>
    )
}
