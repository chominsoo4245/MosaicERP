import { memo } from "react";

const ItemRow = memo(({ id, name, code, unit, stock, price, supplier, category, index, isActive, onClick }) => {
    console.log("렌더링:", name);

    return (
        <tr
            className={`hover:bg-zinc-50 ${isActive ? "bg-green-300" : ""}`}
            onClick={() => onClick(id)}
        >
            <td className="border p-2 text-center">{index + 1}</td>
            <td className="border p-2">{name}</td>
            <td className="border p-2">{code}</td>
            <td className="border p-2 text-right">{stock.toLocaleString()}</td>
            <td className="border p-2">{unit}</td>
            <td className="border p-2">{price}</td>
            <td className="border p-2">{supplier}</td>
            <td className="border p-2">{category}</td>

        </tr>
    );
});
export default ItemRow;