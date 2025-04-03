export default function SidebarMenuItem({icon, label, active, onClick}){
    return (
        <li
            className={`flex items-center mb-7 p-3 cursor-pointer hover:bg-gray-100 ${active ? 'bg-gray-200': ''}`}
            onClick={onClick}
        >
            {icon && <span className="mr-2">{icon}</span>}
            <span>{label}</span>
        </li>
    )
}