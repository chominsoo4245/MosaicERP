import React, {useEffect, useState, useCallback} from "react";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import GenericSearchLayout from "../../components/Layout/GenericSearchLayout.jsx";

const InventoryListPage = () => {
    const [inventoryList, setInventoryList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const searchFields = [
        { name: "itemId", label: "품목 ID", type: "number", placeholder: "예: 1001" },
        { name: "warehouseId", label: "창고 ID", type: "number", placeholder: "예: 1" },
        { name: "fromCreatedAt", label: "생성일 시작", type: "datetime-local", placeholder: "" },
        { name: "toCreatedAt", label: "생성일 종료", type: "datetime-local", placeholder: "" },
        { name: "keyword", label: "키워드 (LOT)", type: "text", placeholder: "검색어 입력" },
    ];

    const fetchInventory = async (searchParams) => {
        setLoading(true);
        try {
            response.success = false;
            if (response.success) {
                setInventoryList(response.data);
            } else {
                setError(response.message || "재고 데이터를 가져오는데 실패했습니다.");
            }
        } catch (err) {
            setError(err.message || "재고 데이터를 가져오는데 실패했습니다.");
        }
        setLoading(false);
    };
    useEffect(() => {
        fetchInventory({
            itemId: null,
            warehouseId: null,
            fromCreatedAt: null,
            toCreatedAt: null,
            keyword: null,
        });
    }, []);

    return (
        <PageLayout title="현재 재고" breadcrumb={["재고 관리", "현재 재고"]} actions={<ButtonLayout actions={[]} />}>
            <GenericSearchLayout fields={searchFields} onSearch={fetchInventory} />
            {loading && <p>Loading...</p>}
            {error && <p className="text-red-500">{error}</p>}
            {!loading && !error && inventoryList.length > 0 ? (
                <table className="w-full table-auto border-collapse">
                    <thead>
                    <tr className="bg-zinc-100">
                        <th className="border p-2">No</th>
                        <th className="border p-2">품목 ID</th>
                        <th className="border p-2">재고 수량</th>
                        <th className="border p-2">위치 정보</th>
                        <th className="border p-2">LOT 번호</th>
                        <th className="border p-2">유통기한</th>
                    </tr>
                    </thead>
                    <tbody>
                    {inventoryList.map((item, index) => (
                        <tr key={item.inventoryId}>
                            <td className="border p-2">{index + 1}</td>
                            <td className="border p-2">{item.itemId}</td>
                            <td className="border p-2">{item.currentQuantity}</td>
                            <td className="border p-2">{item.binId}</td>
                            <td className="border p-2">{item.lotNumber}</td>
                            <td className="border p-2">{item.expirationDate}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : !loading && <p>목록이 없습니다.</p>}
        </PageLayout>
    );
};

export default InventoryListPage;