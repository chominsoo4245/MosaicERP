import "./index.css"
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import TopBar from './components/TopBar';
import Home from './pages/home/Home.jsx';
import React from "react";
import ItemListPage from "./pages/items/ItemListPage.jsx";
import ItemCreatePage from "./pages/items/ItemCreatePage.jsx";
import ItemDetailPage from "./pages/items/ItemDetailPage.jsx";
import {AuthProvider} from "./context/AuthContext.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import LoginPage from "./pages/login/LoginPage.jsx";
import InventoryListPage from "./pages/inventory/InventoryListPage.jsx";

function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/*" element={
                        <PrivateRoute>
                            <div className="flex h-screen">
                                <Sidebar />
                                <div className="flex-1 flex flex-col">
                                    <TopBar />
                                    <main className="flex-1 p-4 overflow-auto">
                                        <Routes>
                                            <Route path="/" element={<Home />} />
                                            <Route path="/items/list" element={<ItemListPage />} />
                                            <Route path="/items/register" element={<ItemCreatePage />} />
                                            <Route path="/items/detail/:id" element={<ItemDetailPage />} />
                                            <Route path="/stock-status/list" element={<InventoryListPage />} />
                                        </Routes>
                                    </main>
                                </div>
                            </div>
                        </PrivateRoute>
                    } />
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
}

export default App
