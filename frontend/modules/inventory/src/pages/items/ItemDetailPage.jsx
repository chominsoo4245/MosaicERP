import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import ItemForm from "./form/ItemForm.jsx";
import {getFormInitDataAPI} from "../../services/ItemService.jsx";

export default function ItemDetailPage() {
    const { id } = useParams(); // /items/detail/:id
    const navigate = useNavigate();
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const actions = [
        { label: "수정", type: "update", to: `/items/update/${id}` },
        { label: "목록", type: "list", onClick: () => navigate("/items/list") },
    ];

    useEffect(() => {
        const fetchItem = async () => {
            try {
                 const response = await getFormInitDataAPI(id);
                if (response.success) {
                    setItem(response.data);
                } else {
                    setError(response.message || "품목 상세 정보를 불러오는데 실패했습니다.");
                }
            } catch (err) {
                setError(err.message || "품목 상세 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchItem();
    }, [id]);

    if (loading) return <PageLayout title="품목 상세 정보" breadcrumb={["품목 관리", "상세 보기"]}><p>Loading...</p></PageLayout>;
    if (error) return <PageLayout title="품목 상세 정보" breadcrumb={["품목 관리", "상세 보기"]}><p className="text-red-500">{error}</p></PageLayout>;
    if (!item) return null;

    return (
        <PageLayout title="📦 품목 상세 정보" breadcrumb={["품목 관리", "상세 보기"]} actions={<ButtonLayout actions={actions} />}>
            {/* ItemForm에 readOnly prop을 전달하여 수정 불가능하게 할 수 있습니다. */}
            <ItemForm formData={item} onChange={() => {}} readOnly />
        </PageLayout>
    );
}