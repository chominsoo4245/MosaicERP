import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import ItemForm from "./form/ItemForm.jsx"; // 등록/수정과 동일한 Form

export default function ItemDetailPage() {
    const { id } = useParams(); // /items/detail/:id
    const navigate = useNavigate();
    const [item, setItem] = useState(null);

    // 📦 임시 mock 데이터 (실제 API 대체 가능)
    const mockItems = [
        {
            id: 1,
            name: "고급 나사",
            code: "A-1001",
            stock: 320,
            unit: "EA",
            price: 1000,
            category: "나사류",
            supplier: "A상사",
            location: "창고 A-1",
            lot: "LOT-20240401",
            expirationDate: "2025-04-01",
            description: "튼튼한 나사입니다.",
        },
    ];

    useEffect(() => {
        const found = mockItems.find((i) => i.id === Number(id));
        if (found) setItem(found);
        else alert("해당 품목이 없습니다.");
    }, [id]);

    const actions = [
        { label: "수정", type: "update", to: `/items/update/${id}` },
        { label: "목록", type: "list", onClick: () => navigate("/items/list") },
    ];

    const handleDummyChange = () => {
        // detail 페이지에서는 수정 불가
    };

    if (!item) return null;

    return (
        <PageLayout
            title="📦 품목 상세 정보"
            breadcrumb={["품목 관리", "품목 상세"]}
            actions={<ButtonLayout actions={actions} />}
        >
            <ItemForm formData={item} onChange={handleDummyChange} />
        </PageLayout>
    );
}