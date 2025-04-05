import {useNavigate} from "react-router-dom";

export default function ButtonLayout ({actions}) {
    const navigate = useNavigate();

    const defaultStyle = "px-4 py-2 rounded text-sm font-medium";
    const typeStyles = {
        create: "bg-blue-600 text-white hover:bg-blue-700",
        update: "bg-blue-600 text-white hover:bg-blue-700",
        list: "bg-gray-500 text-white hover:bg-gray-600",
        delete: "bg-red-500 text-white hover:bg-red-600",
        download: "bg-green-500 text-white hover:bg-green-600",
    };

    return (
        <div className="flex gap-2">
            {actions.map((action, index) => {
                const {label, type, onClick, to} = action;
                const style = `${defaultStyle} ${typeStyles[type] || ""}`;

                const handleClick = () => {
                    if (to) navigate(to);
                    else if (onClick) onClick();
                };

                return (
                    <button key={index} className={style} onClick={handleClick}>
                        {label}
                    </button>
                );
            })}
        </div>
    );
}