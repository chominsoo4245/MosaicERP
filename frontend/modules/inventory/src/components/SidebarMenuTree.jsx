import { Link, useLocation } from "react-router-dom";
import { useState } from "react";

export default function SidebarMenuTree({ items, level = 1 }) {
    const [openIndex, setOpenIndex] = useState(null);
    const location = useLocation();

    const handleToggle = (index) => {
        setOpenIndex((prev) => (prev === index ? null : index));
    };

    const liClasses = ({ isActive, isOpen }) => {
        let base = `flex flex-col mb-2 rounded p-2 transition-colors duration-150 ml-${level * 2}`;

        if (isOpen) {
            base += " bg-gray-200 font-semibold";
        } else {
            base += " bg-gray-100 hover:bg-gray-300";
        }

        return base;
    };

    function getTextSize(level){
        switch (level) {
            case 1:
                return "text-base";
            case 2:
                return "text-sm";
            case 3:
            default:
                return "text-xs";
        }
    }

    return (
        <>
            {items.map((item, index) => {
                const isActive = location.pathname.startsWith(item.path);
                const hasChildren = item.children && item.children.length > 0;
                const isOpen = openIndex === index;
                const textSize = getTextSize(level);

                return (
                    <li key={item.label} className={liClasses({ isActive, isOpen })}>
                        <div className="flex justify-between items-center">
                            <Link
                                to={item.path}
                                className={textSize+" flex-1"}
                                onClick={() => hasChildren && handleToggle(index)}
                            >
                                {item.label}
                            </Link>
                            {hasChildren && (
                                <button
                                    onClick={() => handleToggle(index)}
                                    className="text-lg text-gray-500 ml-2"
                                >
                                    {isOpen ? "▾" : "▸"}
                                </button>
                            )}
                        </div>

                        {hasChildren && isOpen && (
                            <ul>
                                <SidebarMenuTree items={item.children} level={level + 1} />
                            </ul>
                        )}
                    </li>
                );
            })}
        </>
    );
}
