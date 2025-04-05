import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import {useCallback, useState} from "react";
import ItemRow from "./ItemRow.jsx";
import {useNavigate} from "react-router-dom";


const ItemListPage = ({items}) => {
    const navigate = useNavigate();

    const [onIndex, setOnIndex] = useState(null);
    const mockItems = items || [
        {id: 1, name: "고급 나사", code: "A-1001", unit: "EA", stock: 320},
        {id: 2, name: "산업용 볼트", code: "A-1002", unit: "EA", stock: 180},
        {id: 3, name: "산업용 나사", code: "A-1003", unit: "EA", stock: 130},
    ];
    const actions = [
        {label: "품목 등록", type: "create", to: "/items/register"},
        {label: "품목 수정", type: "update", to: `/items/update/${onIndex}`},
        {label: "엑셀 다운로드", type: "download", onClick: () => console.log("다운로드")},
    ];
    const handleOnClick = useCallback((itemId) => {
        navigate(`/items/detail/${itemId}`);
    }, [navigate]);

    return (
        <PageLayout
            title="📦 품목 목록"
            breadcrumb={["품목 관리", "품목 목록"]}
            actions={<ButtonLayout actions={actions}/>}
        >
            <table className="w-full table-auto border-collapse">
                <thead>
                <tr className="bg-zinc-100">
                    <th className="border p-2">No</th>
                    <th className="border p-2">품목명</th>
                    <th className="border p-2">품목 코드</th>
                    <th className="border p-2">재고 수량</th>
                    <th className="border p-2">단위</th>
                    <th className="border p-2">단가</th>
                    <th className="border p-2">공급업체</th>
                    <th className="border p-2">분류</th>
                </tr>
                </thead>
                <tbody>
                {mockItems.map((item, index) => (
                    <ItemRow
                        key={item.id}
                        index={index}
                        id={item.id}
                        name={item.name}
                        code={item.code}
                        stock={item.stock}
                        unit={item.unit}
                        price={item.price}
                        supplier={item.supplier}
                        category={item.category}
                        isActive={onIndex === item.id}
                        onClick={handleOnClick}
                    />
                ))}
                </tbody>
            </table>
        </PageLayout>
    );
}

export default ItemListPage;