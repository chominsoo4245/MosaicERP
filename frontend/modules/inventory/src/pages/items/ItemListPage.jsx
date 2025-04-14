import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import React, {useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getItemListAPI} from "../../services/ItemService.jsx";


const ItemListPage = () => {
    const navigate = useNavigate();
    const [itemList, setItemList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const actions = [
        {label: "품목 등록", type: "create", to: "/items/register"},
        {label: "품목 수정", type: "update", to: `/items/update/`},
        {label: "엑셀 다운로드", type: "download", onClick: () => console.log("다운로드")},
    ];

    const fetchItem = async () => {
        setLoading(true);
        const response = await getItemListAPI();
        console.log(response);
        try {
            if (response.success) {
                setItemList(response.data);
            } else {
                setError(response.message || "품목 데이터를 가져오는데 실패했습니다.");
            }
        } catch (err) {
            setError(err.message || "품목 데이터를 가져오는데 실패했습니다.");
        }
        setLoading(false);
    }
    useEffect(() => {
        fetchItem();
    }, []);

    return (
        <PageLayout
            title="📦 품목 목록"
            breadcrumb={["품목 관리", "품목 목록"]}
            actions={<ButtonLayout actions={actions}/>}
        >
            {loading && <p>Loading...</p>}
            {error && <p className="text-red-500">{error}</p>}
            {!loading && !error && itemList.length > 0 ? (
                <table className="w-full table-auto border-collapse">
                    <thead>
                    <tr className="bg-zinc-100">
                        <th className="border p-2">품목 ID</th>
                        <th className="border p-2">품목 코드</th>
                        <th className="border p-2">품목 명</th>
                        <th className="border p-2">품목 분류</th>
                        <th className="border p-2">단위</th>
                        <th className="border p-2">원가</th>
                        <th className="border p-2">판매 가격</th>
                        <th className="border p-2">LOT 관리여부</th>
                        <th className="border p-2">기본 공급업체</th>
                    </tr>
                    </thead>
                    <tbody>
                    {itemList.map((item, index) => (
                        <tr key={item.itemId}>
                            <th className="border p-2">{item.itemId}</th>
                            <th className="border p-2">{item.code}</th>
                            <th className="border p-2">{item.name}</th>
                            <th className="border p-2">{item.categoryName}</th>
                            <th className="border p-2">{item.unit}</th>
                            <th className="border p-2">{item.cost}</th>
                            <th className="border p-2">{item.price}</th>
                            <th className="border p-2">{item.isLotTracked ? "있음" :"없음"}</th>
                            <th className="border p-2">{item.supplierName}</th>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : !loading && <p>목록이 없습니다.</p>}
        </PageLayout>
    );
}

export default ItemListPage;