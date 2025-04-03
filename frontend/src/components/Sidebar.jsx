import SidebarMenuItem from "./SidebarMenuItem.jsx";
import {useState} from "react";

export default function Sidebar() {
    const [activeIndex, setActiveIndex] = useState(0);
    const menuItem = ["홈", "사용자 관리", "재고 관리", "주문 관리", "설정"]

    function handleMenuClick(index) {
        setActiveIndex(index);
    }

    return (
        <aside className="bg-white shadow-md py-4 px-6 w-64">
            <ul className="h-screen">
                {menuItem.map((label, index) => (
                    <SidebarMenuItem
                        key={label}
                        label={label}
                        active={index === activeIndex}
                        onClick={() => handleMenuClick(index)}
                    />
                ))}
            </ul>
        </aside>
    );
}